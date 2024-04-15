import { v4 as uuid } from 'uuid';
import { NodeRole, NodeStatus } from './node.types';
import { CommandOperations } from './transaction/transaction.types';
import { Cluster } from '../cluster/cluster';
import { NodeLogger } from './node.logger';

export class Node {
	id: string;
	name: string;
	role: NodeRole;
	state: Map<string, any>;
	status: NodeStatus;
	votesCount: number;
	cluster: Cluster;
	leader: Node | null;
	interval: number;
	transactionList: Map<CommandOperations, any[]>;
	timerId: NodeJS.Timer;
	timerIdLeader: NodeJS.Timer | null;
	logger: NodeLogger;

	constructor(nodeName: string, cluster: Cluster, leader: Node | null) {
		this.id = uuid();
		this.name = nodeName;
		this.role = NodeRole.Follower;
		this.state = new Map<string, any>();
		this.status = NodeStatus.Active;
		this.votesCount = 0;
		this.cluster = cluster;
		this.leader = leader ?? null;
		this.logger = new NodeLogger();
		this.transactionList = new Map<CommandOperations, any[]>();

		const baseInterval = parseInt(process.env.BASE_INTERVAL!, 10);
		this.interval = Math.floor(
			(Math.ceil(baseInterval / 2 / 1000) * 1000) +
			Math.random() * (baseInterval - (Math.ceil(baseInterval / 2 / 1000) * 1000))
		);
		this.timerId = setInterval(this.emitCandidateMessage.bind(this), this.interval);
		this.timerIdLeader = null;

		this.subscribeEvents();

		this.logger.logSuccessfullyCreatedNode(this.name, this.interval);
	}

	getValue(key: string) {
		return this.state.get(key);
	}

	setValue(key: string, value: any) {
		if (this.role !== NodeRole.Leader) {
			this.logger.logWrongOperation(this.name, 'SET');
			return;
		}

		this.setValuePrivate(key, value);
		return this.getValue(key);
	}

	deleteValue(key: string) {
		if (this.role !== NodeRole.Leader) {
			this.logger.logWrongOperation(this.name, 'DELETE');
			return;
		}

		const value = this.getValue(key);
		this.deleteValuePrivate(key);
		return value;
	}

	deleteNode() {
		this.logger.logDeleteNodeInfo(this.name);

		if (this.role === NodeRole.Leader) {
			this.cluster.eventEmitter.emit('consistency', this.name, this.transactionList);
		}

		this.state = new Map<string, any>();
		this.transactionList = new Map<CommandOperations, any[]>();
		this.status = NodeStatus.Deleted;
		clearInterval(this.timerId);
		this.timerIdLeader && clearInterval(this.timerIdLeader);
		this.timerIdLeader = null;
		this.logger.logDeleteNode(this.name);
	}

	private setValuePrivate(key: string, value: any) {
		this.state.set(key, value);
		this.transactionList.set('Set', [key, value]);
	}

	private deleteValuePrivate(key: string) {
		this.state.delete(key);
		this.transactionList.set('Del', [key]);
	}

	private changeNodeToFollower() {
		clearInterval(this.timerId);
		this.timerIdLeader && clearInterval(this.timerIdLeader);
		this.timerId = setInterval(this.emitCandidateMessage.bind(this), this.interval);
		this.timerIdLeader = null;
	}

	private changeNodeToCandidate() {
		this.role = NodeRole.Candidate;
		this.timerIdLeader && clearInterval(this.timerIdLeader);
	}

	private changeNodeToLeader() {
		this.role = NodeRole.Leader;
		this.leader = this;
		const baseInterval = parseInt(process.env.BASE_INTERVAL!, 10);
		this.interval = Math.floor(baseInterval / 2 / 1000) * 1000;
		clearInterval(this.timerId);
		this.timerId = setInterval(this.emitLeaderMessage.bind(this), this.interval);

		const consistencyInterval = parseInt(process.env.CONSISTENCY_INTERVAL!, 10);
		this.timerIdLeader && clearInterval(this.timerIdLeader);
		this.timerIdLeader = setInterval(this.emitConsistencyInterval.bind(this), consistencyInterval);
	}

	private emitCandidateMessage() {
		this.changeNodeToCandidate();
		this.cluster.eventEmitter.emit(`candidate`, this.name);
	}

	private emitLeaderMessage() {
		this.cluster.eventEmitter.emit(`confirmation-leader`, this.name);
	}

	private emitConsistencyInterval() {
		this.cluster.eventEmitter.emit(`consistency`, this.name, this.transactionList);
	}

	private subscribeEvents() {
		this.cluster.eventEmitter.on('candidate', this.candidate.bind(this));
		this.cluster.eventEmitter.on('vote-candidate', this.voteCandidate.bind(this));
		this.cluster.eventEmitter.on('leader', this.onLeader.bind(this));
		this.cluster.eventEmitter.on('confirmation-leader', this.onLeaderConfirmation.bind(this));
		this.cluster.eventEmitter.on('consistency', this.consistency.bind(this));
	}

	private candidate(nodeName: string) {
		if (this.status === NodeStatus.Active) {
			this.logger.logVoteForNode(this.name, nodeName);
			this.cluster.eventEmitter.emit('vote-candidate', nodeName);
		}
	}

	private onLeader(nodeName: string) {
		if (this.status === NodeStatus.Active) {
			this.leader = this.cluster.getNode(nodeName) ?? null;
			this.logger.logAcceptNodeAsLeader(this.name, nodeName);

			if (this.name === nodeName) {
				this.changeNodeToLeader();
			}

			if (this.name !== nodeName) {
				this.changeNodeToFollower();
			}
		}
	}

	private onLeaderConfirmation(nodeName: string) {
		if (this.status === NodeStatus.Active) {
			this.leader = this.cluster.getNode(nodeName) ?? null;
			this.logger.logConfirmNodeAsLeader(this.name, nodeName);

			if (this.name !== nodeName) {
				this.changeNodeToFollower();
			}
		}
	}

	private voteCandidate(nodeName: string) {
		if (this.status === NodeStatus.Active) {
			if (this.name === nodeName) {
				this.votesCount++;
				this.logger.logVoteReceived(this.name, this.votesCount);

				if (this.votesCount === this.cluster.nodes.length) {
					this.logger.logBecomeLeader(this.name);
					this.cluster.eventEmitter.emit(`leader`, this.name);
				}
			}
		}
	}

	private consistency(leaderNodeName: string, map: Map<CommandOperations, any[]>) {
		if (this.status === NodeStatus.Active) {
			if (this.name !== leaderNodeName) {
				this.logger.logConsistency(this.name);
				const differenceArray = Array.from(map).slice(map.size - this.transactionList.size - 1);
				for (const differenceOperation of differenceArray) {
					switch (differenceOperation[0]) {
						case 'Set': {
							const [key, value, ...rest] = differenceOperation[1];
							this.setValuePrivate(key, value);
							break;
						}
						case 'Del': {
							const [key, ...rest] = differenceOperation[1];
							this.deleteValuePrivate(key);
							break;
						}
						default:
							break;
					}
				}
			}
		}
	}
}
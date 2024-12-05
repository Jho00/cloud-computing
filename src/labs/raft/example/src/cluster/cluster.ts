import { Node } from '../node/node';
import { v4 as uuid } from 'uuid';
import { ClusterLogger } from './cluster.logger';
import * as dotenv from 'dotenv';
import { NodeRole } from '../node/node.types';
import { EventEmitter } from 'events';

dotenv.config();

export class Cluster {
	readonly id: string;
	clusterName: string;
	nodes: Node[];
	eventEmitter: EventEmitter;
	logger: ClusterLogger;

	constructor(clusterName?: string) {
		this.id = uuid();
		this.clusterName = clusterName ?? `cluster_${this.id}`;
		this.nodes = [];
		this.eventEmitter = new EventEmitter();
		this.logger = new ClusterLogger();
		setInterval(this.analytics.bind(this), 5000);
	}

	appendNode(nodeName: string): Node | void {
		let leader: Node | null = null;

		for (const node of this.nodes) {
			if (node.name === nodeName) {
				this.logger.logNodeWithSameName(nodeName);
				return;
			}

			if (node.role === NodeRole.Leader) {
				leader = node;
			}
		}

		const newNode = new Node(nodeName, this, leader);
		this.nodes.push(newNode);
		this.logger.logSuccessfullyAddedNode(newNode.name, this.clusterName);

		return newNode;
	}

	getNode(nodeName: string) {
		const node = this.nodes.find(item => item.name === nodeName);
		if (!node) {
			this.logger.logNodeNotFound(nodeName);
			return;
		}

		return node;
	}

	deleteNode(nodeName: string) {
		let node = this.nodes.find(item => item.name === nodeName);
		if (!node) {
			this.logger.logNodeNotFound(nodeName);
			return;
		}

		node.deleteNode();
		this.nodes = this.nodes.filter(item => item.name !== nodeName);
	}

	private analytics() {
		let message = '-----------------------------------------\n';
		this.nodes.forEach(item => {
			message += `Node ${item.name}; Role: ${item.role}; Status: ${item.status}; Leader: ${item.leader?.name}; Interval: ${item.interval};\n`;
		});
		message += '-----------------------------------------';
		this.logger.log(message);
	}
}
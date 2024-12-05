import { LoggerBase } from '../logger/logger.base';

export class NodeLogger extends LoggerBase {
	logSuccessfullyCreatedNode(nodeName: string, interval: number) {
		this.logSuccess(`Created node \"${nodeName}\". Interval: ${interval}`);
	}

	logVoteForNode(emitterNodeName: string, nodeName: string) {
		this.logInfo(`From node \"${emitterNodeName}\". Vote for node: \"${nodeName}\"`);
	}

	logAcceptNodeAsLeader(emitterNodeName: string, nodeName: string) {
		this.logInfo(`From node \"${emitterNodeName}\". Accept node \"${nodeName}\" as leader`);
	}

	logConfirmNodeAsLeader(emitterNodeName: string, nodeName: string) {
		this.logInfo(`From node \"${emitterNodeName}\". Confirm node \"${nodeName}\" as leader`);
	}

	logVoteReceived(emitterNodeName: string, voteCount: number) {
		this.logInfo(`From node \"${emitterNodeName}\". Vote received. Current votes: ${voteCount}`);
	}

	logBecomeLeader(emitterNodeName: string) {
		this.logInfo(`From node \"${emitterNodeName}\". Become a leader`);
	}

	logWrongOperation(emitterNodeName: string, operationName: string) {
		this.logError(`From node \"${emitterNodeName}\". Wrong operation: \"${operationName}\". Only read mode`);
	}

	logConsistency(emitterNodeName: string) {
		this.logInfo(`From node \"${emitterNodeName}\". Making consistency`);
	}

	logDeleteNodeInfo(emitterNodeName: string) {
		this.logInfo(`Deleting node \"${emitterNodeName}\"`);
	}

	logDeleteNode(emitterNodeName: string) {
		this.logSuccess(`Delete node \"${emitterNodeName}\"`);
	}
}
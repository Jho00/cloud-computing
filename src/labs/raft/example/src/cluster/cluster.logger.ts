import { LoggerBase } from '../logger/logger.base';

export class ClusterLogger extends LoggerBase {
	logNodeWithSameName(nodeName: string) {
		this.logError(`Node with same name: \"${nodeName}\" already exist`);
	}

	logNodeNotFound(nodeName: string) {
		this.logError(`Node with name: \"${nodeName}\" not found`);
	}

	logSuccessfullyAddedNode(nodeName: string, clusterName: string) {
		this.logSuccess(`Added node \"${nodeName}\" to cluster \"${clusterName}\"`);
	}
}
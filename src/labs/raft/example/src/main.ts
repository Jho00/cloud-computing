import { Cluster } from './cluster/cluster';
import inquirer from 'inquirer';

function bootstrap() {
	const cluster1 = new Cluster('dick');

	cluster1.appendNode('1');
	cluster1.appendNode('2');
	cluster1.appendNode('3');

	const takeOperation = () => {
		inquirer.prompt([{
			name: 'operation',
			type: 'list',
			message: 'Choose an operation',
			choices: ['appendNode', 'deleteNode', 'getNodeValue', 'setNodeValue', 'deleteNodeValue']
		}]).then(answers => {
			switch (answers.operation) {
				case 'appendNode': {
					inquirer.prompt([{
						name: 'nodeName',
						type: 'input',
						message: 'Enter a new node name. [nodeName]'
					}]).then(answers => {
						cluster1.appendNode(answers.nodeName.match(/\w/gm).join(''));
						takeOperation();
					});
					break;
				}
				case 'deleteNode': {
					inquirer.prompt([{
						name: 'nodeName',
						type: 'input',
						message: 'Enter a deleting node name. [nodeName]'
					}]).then(answers => {
						cluster1.deleteNode(answers.nodeName.match(/\w/gm).join(''));
						takeOperation();
					});
					break;
				}
				case 'getNodeValue': {
					inquirer.prompt([{
						name: 'nodeNameAndKey',
						type: 'input',
						message: 'Enter a node name & key. [nodeName] [key]'
					}]).then(answers => {
						const [nodeName, key] = answers.nodeNameAndKey.split(' ');
						const node = cluster1.getNode(nodeName);
						console.log(node?.getValue(key));
						takeOperation();
					});
					break;
				}
				case 'setNodeValue': {
					inquirer.prompt([{
						name: 'nodeNameKeyAndValue',
						type: 'input',
						message: 'Enter a node name & key & value. [nodeName] [key] [value]'
					}]).then(answers => {
						const [nodeName, key, value] = answers.nodeNameKeyAndValue.split(' ');
						const node = cluster1.getNode(nodeName);
						console.log(node?.setValue(key, value));
						takeOperation();
					});
					break;
				}
				case 'deleteNodeValue': {
					inquirer.prompt([{
						name: 'nodeNameAndKey',
						type: 'input',
						message: 'Enter a node name & key & value. [nodeName] [key]'
					}]).then(answers => {
						const [nodeName, key] = answers.nodeNameAndKey.split(' ');
						const node = cluster1.getNode(nodeName);
						console.log(node?.deleteValue(key));
						takeOperation();
					});
					break;
				}
				default:
					takeOperation();
					break;
			}
		});
	};
	takeOperation();
}

bootstrap();

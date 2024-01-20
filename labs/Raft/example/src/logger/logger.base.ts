import { ILogger } from './logger.interface';
import { path } from 'app-root-path';
import { appendFile, ensureDir } from 'fs-extra';

export class LoggerBase implements ILogger {
	async log(message: string, topic?: string) {
		// console.log(dedent`
		// 	[${topic}] [${this.getTimeString()}] ${message}
		// `);
		const logsDir = `${path}/src/logs`;
		await ensureDir(logsDir);
		await appendFile(`${logsDir}/logs.txt`, `${topic ? `[${topic}] ` : ``}[${this.getTimeString()}] ${message} \n`);
	}

	logError(message: string): void {
		this.log(message, 'ERROR');
	}

	logInfo(message: string): void {
		this.log(message, 'INFO');
	}

	logSuccess(message: string): void {
		this.log(message, 'SUCCESS');
	}

	private getTimeString(): string {
		const date = new Date();
		const changeTimeVisual = (num: number) => num > 9 ? `${num}` : `0${num}`;
		return `${changeTimeVisual(date.getHours())}:${changeTimeVisual(date.getMinutes())}:${changeTimeVisual(date.getSeconds())}`;
	}
}
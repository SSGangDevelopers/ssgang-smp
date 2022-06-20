import path from 'path';
import url from 'url';
import fs from 'fs';
import { exec } from 'child_process';

const task = process.argv[2];
const args = process.argv.slice(3);
const dirName = url.fileURLToPath(new URL('.', import.meta.url));
const taskDir = path.join(dirName, task);
if (fs.existsSync(taskDir)) {
	exec(`npm i -y --prefix ${taskDir}`, async (error, stdout, stderr) => {
		if (error) {
			console.error(`error: ${error.message}`);
		}
		if (stderr) {
			console.error(`stderr: ${stderr}`);
		}
		console.log("Installed task's dependencies: ", stdout);

		const run = (await import(url.pathToFileURL(path.join(dirName, task, 'main.js'))))['default'];

		let rootDir = path.dirname(path.dirname(dirName));
		let taskRootDir = path.join(rootDir, 'tasks', task);

		run({ rootDir, taskRootDir, args });
	});
} else {
	console.error(`Task ${task} is not exist!`);
	process.exit(1);
}

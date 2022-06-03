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
			process.exit(1);
		}
		if (stderr) {
			console.error(`stderr: ${stderr}`);
			process.exit(1);
		}
		console.log("Installed task's dependencies: ", stdout);

		const run = (await import(`./${task}/main.js`))['default'];
		run(args);
	});
} else {
	console.error(`Task ${task} is not exist!`);
	process.exit(1);
}

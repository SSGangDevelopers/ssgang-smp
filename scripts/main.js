const fs = require("fs");
const path = require("path");
const args = process.argv.slice(2, process.argv.length);

const tasksPath = path.join(__dirname, "tasks");
const tasks = fs
	.readdirSync(path.join(tasksPath))
	.filter((file) => file.endsWith(".js"))
	.map((file) => file.replace(".js", ""));

const task = args[0];
if (tasks.includes(task)) {
	const { run } = require(path.join(tasksPath, task));
	if (!run || !(run instanceof Function)) {
		console.error(`Task ${task} is invalid`);
		process.exit();
	}
	run(args.slice(1, args.length));
} else {
	console.error("Task not found");
	process.exit();
}

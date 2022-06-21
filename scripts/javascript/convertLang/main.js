import { existsSync, mkdirSync, readdirSync, readFileSync, writeFileSync } from "fs";
import path from "path";
import YAML from "yaml";
import config from "./config.json" assert { type: "json" };

export default function run(options) {
	const taskRootDir = options.taskRootDir;

	const inputDir = path.join(taskRootDir, "input");
	const outputDir = path.join(taskRootDir, "output");

	const join = (target, data) => {
		if (!config.blacklistedPaths.includes(data.root)) {
			// if (!target[data.root]) {
			// 	if (typeof target === "string") {
			// 		let backup = target;
			// 		target = {};
			// 		target["default"] = backup;
			// 	}

			// 	target[data.root] = data.object;
			// } else {
			// 	let root_1 = Object.keys(data.object)[0];

			// 	let result_1 = target[data.root];

			// 	let result_2 = join(result_1, {
			// 		root: root_1,
			// 		object: data.object[root_1],
			// 	});

			// 	target[data.root] = result_2;
			// }
			let child_root = Object.keys(data.object)[0];
			if (!target[data.root]) {
				if (typeof target === "string") {
					let backup = target;
					target = {};
					target["default"] = backup;
				}

				if (typeof data.object === "string") {
					target[data.root] = data.object;
				} else {
					target[data.root] = join(
						{},
						{
							root: child_root,
							object: data.object[child_root],
						}
					);
				}
			} else {
				let child_result = join(target[data.root], {
					root: child_root,
					object: data.object[child_root],
				});

				target[data.root] = child_result;
			}
		}

		return target;
	};

	function convert(langName) {
		const lang = JSON.parse(readFileSync(path.join(inputDir, `${langName}.json`)));

		const keys = Object.keys(lang).filter((key) => {
			let i = 0;
			while (i < config.reqPaths.length) {
				if (key.startsWith(config.reqPaths[i])) return true;
				i++;
			}
			return false;
		});
		keys.sort();

		let result = {};

		keys.forEach((key) => {
			const paths = key.split(".");
			const value = lang[key];

			let data = [];
			data.push(...paths);
			data.push(value);
			data.reverse();

			for (let i = 1; i < paths.length; i++) {
				let backup = data[i];
				data[i] = {};
				data[i][backup] = data[i - 1];
			}

			data = {
				root: data[data.length - 1],
				object: data[data.length - 2],
			};

			result = join(result, data);
		});

		const outputDir_1 = path.join(outputDir, langName);

		if (!existsSync(outputDir_1)) mkdirSync(outputDir_1);

		writeFileSync(path.join(outputDir_1, "output.yaml"), YAML.stringify(result));

		const outputJSON = {};
		keys.forEach((key) => {
			outputJSON[key] = lang[key];
		});

		writeFileSync(path.join(outputDir_1, "output.json"), JSON.stringify(outputJSON));
	}

	if (!existsSync(outputDir)) mkdirSync(outputDir);
	readdirSync(inputDir, {
		withFileTypes: true,
	})
		.filter((e) => e.isFile() && e.name.endsWith(".json"))
		.forEach((file) => convert(file.name.substring(0, file.name.lastIndexOf("."))));
}

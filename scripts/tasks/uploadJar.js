const Client = require("ftp");
const path = require("path");
const fs = require("fs");

/**
 * @param {string[]} args
 */
module.exports.run = (args) => {
	console.log("Running tasks uploadJar");

	const ftp = new Client();

	ftp.on("ready", () => {
		console.log("Connected to FTP server!");
		const stagingFolder = path.join(path.dirname(path.dirname(__dirname)), "staging");
		const jarFile = fs.readdirSync(stagingFolder).filter((file) => file.endsWith(".jar"))[0]; // Only generate 1 jar file
		ftp.cwd("plugins", (err, dir) => {
			if (err) {
				console.error("Task uploadJar caugth an error!");
				console.error(err);
				process.exit();
			}
		});
	});

	ftp.connect({
		host: args[0],
		port: args[1],
		user: args[2],
		password: args[3],
	});
};

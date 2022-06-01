import Client from "ssh2-sftp-client";
import path from "path";
import fs from "fs";
import * as url from "url";
const __dirname = url.fileURLToPath(new URL(".", import.meta.url));
const sftp = new Client();
const args = process.argv.slice(2);

sftp
	.connect({
		host: args[0],
		port: args[1],
		username: args[2],
		password: args[3],
	})
	.then(() => {
		console.log("Connected to SFTP server");
		sftp.list("/plugins").then((list) => {
			const files = list
				.filter((e) => e.type == "-")
				.filter((file) => file.name.endsWith(".jar"))
				.filter((file) => file.name.startsWith("ssgang-smp-"));

			// files.forEach((file) => sftp.delete(`/plugins/${file.name}`));

			if (files.length > 0) {
				let i = 0;
				function deleteRemoteFile(fileName) {
					sftp.delete(`/plugins/${fileName}`).then(() => {
						i++;
						let file = files[i];
						if (!file) {
							upload();
						} else {
							deleteRemoteFile(file.name);
						}
					});
				}
				deleteRemoteFile(files[i].name);
			} else {
				upload();
			}
		});
	})
	.catch((e) => {
		console.error(e);
		process.exit(1);
	});

function upload() {
	const projectRootDir = path.dirname(path.dirname(__dirname));
	const libs = path.join(path.join(projectRootDir, "build"), "libs");
	const jarFile = fs
		.readdirSync(libs)
		.filter((file) => file.endsWith(".jar"))
		.filter((file) => file.startsWith("ssgang-smp-"))[0];

	sftp.put(path.join(libs, jarFile), `/plugins/${jarFile}`).then(() => {
		console.log("Jar file upload successfully!");
		process.exit(0);
	});
}

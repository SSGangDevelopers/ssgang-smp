import Client from 'ssh2-sftp-client';
import path from 'path';
import fs from 'fs';
import url from 'url';
const dirName = url.fileURLToPath(new URL('.', import.meta.url));
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
		console.log('Connected to SFTP server');

		sftp.list('/plugins').then((list) => {
			const files = list
				.filter((e) => e.type == '-')
				.filter((file) => file.name.endsWith('.jar'))
				.filter((file) => file.name.startsWith('ssgang-smp-'));

			files.forEach(async (file) => {
				await sftp.delete(`/plugins/${file.name}`);
				console.log(`Deleted ${file.name}`);
			});

			console.log(`Deleted ${files.length} old file(s)`);

			const projectRootDir = path.dirname(path.dirname(dirName));
			const libs = path.join(path.join(projectRootDir, 'build'), 'libs');
			const jarFile = fs
				.readdirSync(libs)
				.filter((file) => file.endsWith('.jar'))
				.filter((file) => file.startsWith('ssgang-smp-'))[0];

			sftp.put(path.join(libs, jarFile), `/plugins/${jarFile}`).then(() => {
				console.log('Jar file upload successfully!');
				process.exit(0);
			});
		});
	})
	.catch((e) => {
		console.error(e);
		process.exit(1);
	});

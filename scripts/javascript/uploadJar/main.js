import Client from 'ssh2-sftp-client';
import path from 'path';
import fs from 'fs';

export default function run(options) {

	let args = options.args;
	let projectRootDir = options.rootDir;

	let sftp = new Client();

	sftp
	.connect({
		host: args[0],
		port: args[1],
		username: args[2],
		password: args[3],
	})
	.then(() => {
		console.log('Connected to SFTP server');

		sftp.list('/plugins').then(async list => {
			let files = list
				.filter(e => e.type == '-')
				.filter(file => file.name.endsWith('.jar'))
				.filter(file => file.name.startsWith('ssgang-smp-'));

			let i = 0;
			while (i < files.length) {
				await sftp.delete(`/plugins/${files[i].name}`);
				console.log(`Deleted ${files[i].name}`);
				i++;
			}

			console.log(`Deleted ${files.length} old file(s)`);

			
			let libs = path.join(path.join(projectRootDir, 'build'), 'libs');
			let jarFile = fs
				.readdirSync(libs)
				.filter(file => file.endsWith('.jar'))
				.filter(file => file.startsWith('ssgang-smp-'))[0];

			await sftp.put(path.join(libs, jarFile), `/plugins/${jarFile}`);
			console.log('Jar file upload successfully!');
			process.exit(0);
		});
	})
	.catch(e => {
		console.error(e);
		process.exit(1);
	});
}
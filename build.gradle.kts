plugins {
	id("java") // Plugin mainly written in Java
	id("com.github.johnrengelman.shadow") version "7.1.2" // Include JDA into jar file
}

group = "io.github.ssgangdevelopers" // Organization link: https://github.com/SSGangDevelopers
version = "1.0.0_dev" // Semantic versioning: https://semver.org/

repositories {
	mavenCentral()
	maven(
		url = uri("https://papermc.io/repo/repository/maven-public/")
	)
	maven(
		url = uri("https://m2.dv8tion.net/releases/")
	)
}

dependencies {
	compileOnly("io.papermc.paper:paper-api:1.19-R0.1-SNAPSHOT") // API used for development
	compileOnly("org.apache.logging.log4j:log4j-core:2.17.2") // For logs filter
	compileOnly("org.projectlombok:lombok:1.18.24") // For code shorten
	annotationProcessor("org.projectlombok:lombok:1.18.24")

	implementation("net.dv8tion:JDA:4.4.0_352") {
		exclude("club.minnced", "opus-java") // Bot is text-only
		exclude("org.jetbrains", "annotations") // PaperMC already included it
		exclude("org.slf4j", "slf4j-api") // PaperMC already included it
		exclude("com.google.code.findbugs", "jsr305") // PaperMC already included it
	}
}

java {
	toolchain.languageVersion.set(JavaLanguageVersion.of(17)) // Minecraft server's Java version
}

tasks {
	processResources {
		val props = mapOf("version" to version)
		filteringCharset = "UTF-8"
		filesMatching("plugin.yml") {
			expand(props)
		}
	}

	compileJava {
		options.encoding = "UTF-8"
	}

	shadowJar {
		archiveFileName.set(rootProject.name + "-" + rootProject.version + ".jar")
	}
}
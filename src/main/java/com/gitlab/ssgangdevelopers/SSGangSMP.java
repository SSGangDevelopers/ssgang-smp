package com.gitlab.ssgangdevelopers;

import com.gitlab.ssgangdevelopers.utils.LangUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;

import java.io.File;

public class SSGangSMP extends JavaPlugin {

	private static SSGangSMP INSTANCE;
	private static FileConfiguration configuration;

	@Override
	public void onEnable() {
		long startTime = System.currentTimeMillis();
		Logger logger = getSLF4JLogger(); // Prefer using SLF4J

		logger.info("Starting plugin initialization...");

		logger.info("Loading configuration...");
		getConfig().options().copyDefaults();
		saveDefaultConfig();
		configuration = getConfig();

		logger.info("Loading locales...");
		this.saveResource("messages-en.yml", false);
		LangUtils.load(new File(getDataFolder(), "messages-" + getConfig().getString("language") + ".yml"));

		logger.info(LangUtils.get("plugin.start.localeComplete"));

		logger.info(LangUtils.get(
						"plugin.start.done",
						new String[]{"time", (System.currentTimeMillis() - startTime) + "ms"}
		));
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}

	public SSGangSMP() {
		INSTANCE = this;
	}

	public static SSGangSMP getInstance() {
		return INSTANCE;
	}

	public static FileConfiguration getConfiguration() {
		return configuration;
	}

	/**
	 * Disables the plugin.
	 */
	public static void selfDestruct() {
		Bukkit.getPluginManager().disablePlugin(getInstance());
	}
}
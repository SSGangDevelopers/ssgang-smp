package com.gitlab.ssgangdevelopers;

import com.gitlab.ssgangdevelopers.abc.Locale;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("unused") // Will remove in the future
public class SSGangSMP extends JavaPlugin {

	private static SSGangSMP INSTANCE;
	private static YamlConfiguration config;

	@Override
	public void onEnable() {
		boolean useless;
		long startTime = System.currentTimeMillis();
		Logger l = getLogger();
		l.log(Level.INFO, "Starting plugin initialization...");
		l.log(Level.INFO, "Loading locales...");
		Locale lang;
		if (!getDataFolder().exists()) {
			useless = getDataFolder().mkdirs();
		}
		this.saveResource("config.yml", false);
		this.saveResource("messages-en.yml", false);

		l.log(Level.INFO, "Loading configuration...");
		try {
			File configFile = new File(getDataFolder(), "config.yml");
			if (!configFile.exists()) {
				useless = configFile.createNewFile();
			}
			config = new YamlConfiguration();
			config.load(configFile);
		} catch (IOException | InvalidConfigurationException e) {
			l.log(Level.SEVERE, "Error loading configuration. The plugin will shut down.");
			e.printStackTrace();
			selfDestruct();
			return;
		}
		l.log(Level.INFO, "Configuration loaded.");

		l.log(Level.INFO, "Loading locales '" + config.get("language") + "'...");
		try {
			lang = new Locale(new File(getDataFolder(), "messages-" + config.get("language") + ".yml"));
		} catch (IOException | InvalidConfigurationException e) {
			l.log(Level.SEVERE, "Error loading locale. The plugin will shut down.");
			e.printStackTrace();
			selfDestruct();
			return;
		}
		l.log(Level.INFO, lang.get("plugin.start.localeComplete"));

		l.log(Level.INFO,
			lang.get(
				"plugin.start.done",
				new String[]{"time", (System.currentTimeMillis() - startTime) + "ms"}
			)
		);
		INSTANCE = this;
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}

	public static SSGangSMP getInstance() {
		return INSTANCE;
	}

	public static YamlConfiguration getConfiguration() {
		return config;
	}

	/**
	 * Disables the plugin.
	 */
	public static void selfDestruct() {
		Bukkit.getPluginManager().disablePlugin(getInstance());
	}
}
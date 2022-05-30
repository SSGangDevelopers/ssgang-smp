package io.github.ssgangdevelopers;

import io.github.ssgangdevelopers.listeners.ChatBridging;
import io.github.ssgangdevelopers.logs.JDALogFormatter;
import io.github.ssgangdevelopers.utils.ColorUtils;
import io.github.ssgangdevelopers.utils.LangUtils;
import lombok.Getter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class SSGangSMP extends JavaPlugin {

	@Getter
	private JDA jda;
	@Getter
	private TextChannel chatChannel;

	@Override
	public void onEnable() {
		long startTime = System.currentTimeMillis();
		Logger logger = getSLF4JLogger(); // Prefer using SLF4J

		logger.info("Starting plugin initialization...");

		// Load config - Start
		logger.info("Loading configuration...");
		getConfig().options().copyDefaults();
		saveDefaultConfig();
		// Load config - End

		// Load locale - Start
		logger.info("Loading locales...");
		initLocaleFiles();
		File messagesFolder = new File(getDataFolder(), "messages");
		LangUtils.load(new File(messagesFolder, "messages-" + getConfig().getString("language") + ".yml"));

		if (!this.isEnabled()) return; // Stop onEnable() if locale file fails to load

		logger.info(LangUtils.get("plugin.start.localeComplete"));
		// Load locale - End

		// Load JDA - Start
		initDiscord();

		// Plugin initialization complete
		logger.info(LangUtils.get(
						"plugin.start.done",
						new String[]{"time", (System.currentTimeMillis() - startTime) + "ms"}
		));
	}

	@Override
	public void onDisable() {
		EmbedBuilder embedBuilder = new EmbedBuilder();
		embedBuilder.setColor(ColorUtils.DISCORD.RED);
		embedBuilder.setTitle(LangUtils.get("bot.server.stop"));
		chatChannel.sendMessageEmbeds(embedBuilder.build()).queue();
		jda.shutdown();
	}

	/**
	 * Initialize Discord bot.
	 */
	public void initDiscord() {
		if (getConfig().get("botToken") == "") {
			getSLF4JLogger().error("Bot token is not set in config.yml, ALL DISCORD INTEGRATION WILL BE DISABLED!");
			return;
		}
		getServer().getScheduler().runTask(this, () -> {
			String token = getConfig().getString("botToken");
			assert token != null;
			String chatChannelId = getConfig().getString("chatChannelId");
			assert chatChannelId != null;

			JDALogFormatter.register();
			try {
				jda = JDABuilder
								.create(GatewayIntent.GUILD_MESSAGES)
								.disableCache(Arrays.asList(CacheFlag.values()))
								.setChunkingFilter(ChunkingFilter.NONE)
								.setLargeThreshold(20)
								.setToken(token)
								.build()
								.awaitReady();
			} catch (LoginException e) {
				getSLF4JLogger().error(LangUtils.get("bot.start.loginError"), e);
				selfDestruct();
			} catch (InterruptedException e) {
				getSLF4JLogger().warn(LangUtils.get("plugin.error.threadInterrupted"));
			}
			if (!this.isEnabled()) return; // Bot login failed
			chatChannel = jda.getTextChannelById(chatChannelId);
			ChatBridging.registerListeners();

			// Discord bot init complete
			EmbedBuilder embedBuilder = new EmbedBuilder();
			embedBuilder.setColor(ColorUtils.DISCORD.GREEN);
			embedBuilder.setTitle(LangUtils.get("bot.server.start"));
			chatChannel.sendMessageEmbeds(embedBuilder.build()).queue();
			getSLF4JLogger().info(
							LangUtils.get(
											"bot.start.done",
											new String[]{"name", jda.getSelfUser().getName()},
											new String[]{"id", "#" + jda.getSelfUser().getId()}
							));
		});
	}

	/**
	 * Ensures the languages files are present.
	 */
	public void initLocaleFiles() {
		File messagesFolder = new File(getDataFolder(), "messages");
		if (!messagesFolder.exists()) {
			boolean dumpster = messagesFolder.mkdirs();
		}

		try {
			if (new File(messagesFolder, "messages-en.yml").createNewFile()) {
				this.saveResource("messages/messages-en.yml", true);
			}
			if (new File(messagesFolder, "messages-vi.yml").createNewFile()) {
				this.saveResource("messages/messages-vi.yml", true);
			}
			if (new File(messagesFolder, "messages-vicc.yml").createNewFile()) {
				this.saveResource("messages/messages-vicc.yml", true);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}


	public static SSGangSMP getInstance() {
		return getPlugin(SSGangSMP.class);
	}

	/**
	 * Disables the plugin.
	 */
	public static void selfDestruct() {
		Bukkit.getPluginManager().disablePlugin(getInstance());
	}
}
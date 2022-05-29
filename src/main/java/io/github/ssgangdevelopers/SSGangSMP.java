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

		// Load lang - Start
		logger.info("Loading locales...");
		try {
			if (new File(getDataFolder(), "messages-en.yml").createNewFile()) {
				this.saveResource("messages-en.yml", true);
			}
			LangUtils.load(new File(getDataFolder(), "messages-" + getConfig().getString("language") + ".yml"));
		} catch (IOException e) {
			logger.error("An error occurred when save default language file, disabling plugin...", e);
			selfDestruct();
		}

		if (!this.isEnabled()) return; // Stop onEnable() if lang file is loaded failed

		logger.info(LangUtils.get("plugin.start.localeComplete"));
		// Load lang - End

		// Setup Discord bot - Start
		Runnable discordBotSetup = () -> {
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
		};
		getServer().getScheduler().runTask(this, discordBotSetup);
		// Setup Discord bot - End

		// Plugin init complete
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
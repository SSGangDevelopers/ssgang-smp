package io.github.ssgangdevelopers;

import io.github.ssgangdevelopers.listeners.ChatBridging;
import io.github.ssgangdevelopers.listeners.EventsBridging;
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
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.util.Arrays;

public class SSGangSMP extends JavaPlugin {

	@Getter
	private JDA jda;
	@Getter
	private TextChannel chatChannel;

	@Override
	public void onEnable() {
		long startTime = System.currentTimeMillis();
		Logger logger = getSLF4JLogger();

		logger.info("Starting plugin initialization...");

		// Load config - Start
		logger.info("Loading configuration...");
		getConfig().options().copyDefaults();
		saveDefaultConfig();
		// Load config - End

		// Load locale - Start
		logger.info("Loading language file...");
		initLanguageFiles();
		String language = getConfig().getString("language");
		if (language == null || language.length() == 0) {
			logger.warn("'language' property is not specified, using default language file");
			language = "en";
		}
		File messagesFolder = new File(new File(getDataFolder(), "lang"), language);
		LangUtils.init(new File(messagesFolder, "plugin-lang.yml"));
		if (!this.isEnabled()) return;
		LangUtils.init(new File(messagesFolder, "minecraft-lang.yml"));
		if (!this.isEnabled()) return;

		logger.info(LangUtils.get("plugin.start.localeComplete"));
		// Load locale - End

		// Load JDA - Start
//		Thread initDiscord = new Thread(this::initDiscord, "SSGangSMP - Discord initialization");
//		initDiscord.start();
		initDiscord();
		// Load JDA - End

		// Plugin initialization complete
		logger.info(LangUtils.get(
						"plugin.start.done",
						new String[]{"time", (System.currentTimeMillis() - startTime) + "ms"}
		));
	}

	@Override
	public void onDisable() {
		if (chatChannel != null && chatChannel.canTalk()) {
			EmbedBuilder embedBuilder = new EmbedBuilder();
			embedBuilder.setColor(ColorUtils.DISCORD.RED);
			embedBuilder.setTitle(LangUtils.get("bot.server.stop"));
			chatChannel.sendMessageEmbeds(embedBuilder.build()).queue();
		}
		if (jda != null) jda.shutdown();
	}

	/**
	 * Initialize Discord bot.
	 */
	private void initDiscord() {
		Logger logger = getSLF4JLogger();
		String botToken = getConfig().getString("botToken");

		if (botToken == null || botToken.length() == 0) {
			logger.error(LangUtils.get("bot.start.error.token"));
			return;
		}

		String chatChannelId = getConfig().getString("chatChannelId");
		assert chatChannelId != null;

		try {
			jda = JDABuilder
							.create(GatewayIntent.GUILD_MESSAGES)
							.disableCache(Arrays.asList(CacheFlag.values()))
							.setChunkingFilter(ChunkingFilter.NONE)
							.setLargeThreshold(20)
							.setToken(botToken)
							.build()
							.awaitReady();
		} catch (LoginException e) {
			logger.error(LangUtils.get("bot.start.error.login"), e);
			selfDestruct();
		} catch (InterruptedException e) {
			logger.warn(LangUtils.get("plugin.error.threadInterrupted"));
		}
		if (!this.isEnabled()) return; // Bot login failed
		chatChannel = jda.getTextChannelById(chatChannelId);

		if (chatChannel == null) {
			logger.warn(LangUtils.get(
							"bot.start.error.channelNotFound",
							new String[]{"id", chatChannelId}
			));
		} else {
			if (!chatChannel.canTalk()) {
				logger.warn(LangUtils.get(
								"bot.start.error.channelCantTalkIn",
								new String[]{"name", "#" + chatChannel.getName()},
								new String[]{"id", chatChannelId}
				));
			} else {
				ChatBridging.registerListeners();
				EventsBridging.registerListeners();

				EmbedBuilder embedBuilder = new EmbedBuilder();
				embedBuilder.setColor(ColorUtils.DISCORD.GREEN);
				embedBuilder.setTitle(LangUtils.get("bot.server.start"));
				chatChannel.sendMessageEmbeds(embedBuilder.build()).queue();
			}
		}

		logger.info(LangUtils.get(
						"bot.start.done",
						new String[]{"name", jda.getSelfUser().getName()},
						new String[]{"id", "#" + jda.getSelfUser().getId()}
		));
	}

	/**
	 * Ensures the languages files are present.
	 */
	private void initLanguageFiles() {
		File langDir = new File(getDataFolder(), "lang");
		if (langDir.mkdir()) {
			File en_lang_dir = new File(langDir, "en");
			if (en_lang_dir.mkdir()) {
				this.saveResource("lang/en/plugin-lang.yml", true);
				this.saveResource("lang/en/minecraft-lang.yml", true);
			}

			File vi_lang_dir = new File(langDir, "vi");
			if (vi_lang_dir.mkdir()) {
				this.saveResource("lang/vi/plugin-lang.yml", true);
				this.saveResource("lang/vi/minecraft-lang.yml", true);
			}
		}
	}

	@NotNull
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
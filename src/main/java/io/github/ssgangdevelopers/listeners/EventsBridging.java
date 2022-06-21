package io.github.ssgangdevelopers.listeners;

import io.github.ssgangdevelopers.SSGangSMP;
import io.github.ssgangdevelopers.utils.ColorUtils;
import io.github.ssgangdevelopers.utils.StringUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

public class EventsBridging implements Listener {

	public static void registerListeners() {
		SSGangSMP plugin = SSGangSMP.getInstance();
		Bukkit.getPluginManager().registerEvents(
						new EventsBridging(plugin.getChatChannel()),
						plugin
		);
	}

	private final TextChannel channel;

	private EventsBridging(TextChannel channel) {
		this.channel = channel;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(@NotNull PlayerJoinEvent e) {
		Component message = e.joinMessage();
		if (message == null) return;
		EmbedBuilder builder = new EmbedBuilder()
						.setTitle(StringUtils.serializeComponent(message))
						.setColor(ColorUtils.DISCORD.GREEN);
		channel.sendMessageEmbeds(builder.build()).queue();
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerQuit(@NotNull PlayerQuitEvent e) {
		Component message = e.quitMessage();
		if (message == null) return;
		EmbedBuilder builder = new EmbedBuilder()
						.setTitle(StringUtils.serializeComponent(message))
						.setColor(ColorUtils.DISCORD.RED);
		channel.sendMessageEmbeds(builder.build()).queue();
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onAdvancementDone(@NotNull PlayerAdvancementDoneEvent e) {
		Component message = e.message();
		if (message == null) return;
		EmbedBuilder builder = new EmbedBuilder()
						.setTitle(StringUtils.serializeComponent(message))
						.setColor(ColorUtils.DISCORD.GREEN);
		channel.sendMessageEmbeds(builder.build()).queue();
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerDeath(@NotNull PlayerDeathEvent e) {
		Component message = e.deathMessage();
		if (message == null) return;
		EmbedBuilder builder = new EmbedBuilder()
						.setTitle(StringUtils.serializeComponent(message))
						.setColor(ColorUtils.DISCORD.BLACK);
		channel.sendMessageEmbeds(builder.build()).queue();
	}
}

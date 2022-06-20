package io.github.ssgangdevelopers.listeners;

import io.github.ssgangdevelopers.SSGangSMP;
import io.github.ssgangdevelopers.utils.ColorUtils;
import io.github.ssgangdevelopers.utils.StringUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Component joinMsgComponent = e.joinMessage();
		if (joinMsgComponent == null) return;
		EmbedBuilder builder = new EmbedBuilder()
						.setTitle(StringUtils.serializeComponent(joinMsgComponent))
						.setColor(ColorUtils.DISCORD.GREEN);
		channel.sendMessageEmbeds(builder.build()).queue();
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		Component quitMsgComponent = e.quitMessage();
		if (quitMsgComponent == null) return;
		EmbedBuilder builder = new EmbedBuilder()
						.setTitle(StringUtils.serializeComponent(quitMsgComponent))
						.setColor(ColorUtils.DISCORD.RED);
		channel.sendMessageEmbeds(builder.build()).queue();
	}
}

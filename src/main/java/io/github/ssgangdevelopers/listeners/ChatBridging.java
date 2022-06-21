package io.github.ssgangdevelopers.listeners;

import io.github.ssgangdevelopers.SSGangSMP;
import io.github.ssgangdevelopers.utils.ColorUtils;
import io.github.ssgangdevelopers.utils.DiscordUtils;
import io.github.ssgangdevelopers.utils.LangUtils;
import io.github.ssgangdevelopers.utils.StringUtils;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageType;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ChatBridging extends ListenerAdapter implements Listener {

	public static void registerListeners() {
		SSGangSMP plugin = SSGangSMP.getInstance();
		ChatBridging instance = new ChatBridging(plugin.getChatChannel());
		plugin.getServer().getPluginManager().registerEvents(instance, plugin);
		plugin.getJda().addEventListener(instance);
	}

	private final TextChannel channel;

	private ChatBridging(TextChannel chatChannel) {
		this.channel = chatChannel;
	}


	@Override
	public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent e) {
		final Component MINECRAFT_PREFIX = Component.text(LangUtils.get("gateway.prefix")).color(ColorUtils.MINECRAFT.AQUA);
		final Component MINECRAFT_ARROW = Component.text(LangUtils.get("gateway.arrow")).color(ColorUtils.MINECRAFT.YELLOW);

		if (checkMsgFromDiscord(e.getMessage())) return;
		String msg = e.getMessage().getContentDisplay();
		String sender;
		if (e.getMember() != null && e.getMember().getNickname() != null) {
			sender = e.getMember().getNickname();
		} else {
			sender = e.getAuthor().getName();
		}

		Bukkit.getServer().broadcast(
						Component.empty()
										.append(MINECRAFT_PREFIX)
										.append(Component.space())
										.append(Component.text(sender))
										.append(Component.space())
										.append(MINECRAFT_ARROW)
										.append(Component.space())
										.append(Component.text(msg))
		);
	}

	private boolean checkMsgFromDiscord(@NotNull Message msg) {
		if (msg.getTextChannel() != this.channel) return true;
		if (!List.of(MessageType.DEFAULT, MessageType.INLINE_REPLY).contains(msg.getType())) return true;
		if (msg.getAuthor().isBot() || msg.isWebhookMessage()) return true;

		return false;
	}


	@EventHandler(priority = EventPriority.MONITOR)
	public void onNewChat(@NotNull AsyncChatEvent e) {
		final String DISCORD_ARROW = "**" + LangUtils.get("gateway.arrow") + "**";

		String msg = processMsg(StringUtils.serializeComponent(e.message()));
		String sender = StringUtils.serializeComponent(e.getPlayer().displayName());

		channel.sendMessage(sender + " " + DISCORD_ARROW + " " + msg).queue();
	}

	private String processMsg(String original) {
		String output = original;
		output = output.replace("@everyone", "`@everyone`");
		List<String> separatedText = List.of(output.split(" "));

		for (String i : separatedText) {
			if (i.startsWith("@")) {
				String userID = DiscordUtils.getIdFromUsername(i.substring(1));
				if (userID != null) {
					output = output.replace(i, "<@" + userID + ">");
				}
			}

			if (i.startsWith("#")) {
				String channelID = DiscordUtils.getIdFromChannelName(i.substring(1));
				if (channelID != null) {
					output = output.replace(i, "<#" + channelID + ">");
				}
			}
		}
		return output;
	}
}

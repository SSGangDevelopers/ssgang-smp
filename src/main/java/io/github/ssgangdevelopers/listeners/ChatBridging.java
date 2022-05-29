package io.github.ssgangdevelopers.listeners;

import io.github.ssgangdevelopers.SSGangSMP;
import io.github.ssgangdevelopers.utils.ColorUtils;
import io.github.ssgangdevelopers.utils.StringUtils;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageType;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ChatBridging extends ListenerAdapter implements Listener {

	public static void registerListeners() {
		SSGangSMP plugin = SSGangSMP.getInstance();
		new ChatBridging(
						SSGangSMP.getInstance(),
						plugin.getJda(),
						plugin.getChatChannel()
		);
	}

	private final TextChannel channel;
	private final SSGangSMP plugin;

	private ChatBridging(SSGangSMP plugin, JDA jda, TextChannel chatChannel) {
		this.channel = chatChannel;
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		jda.addEventListener(this);
	}

	/* From Discord to Minecraft - Start */

	@Override
	public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent e) {
		final Component MINECRAFT_PREFIX = Component.text("[From Discord]").color(ColorUtils.MINECRAFT.AQUA);
		final Component MINECRAFT_ARROW = Component.text(">").color(ColorUtils.MINECRAFT.YELLOW);

		if (checkMsgFromDiscord(e.getMessage())) return;
		String msg = e.getMessage().getContentDisplay();
		String sender = e.getMember().getNickname() != null ? e.getMember().getNickname() : e.getAuthor().getName();

		plugin.getServer().broadcast(
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

	private boolean checkMsgFromDiscord(Message msg) {
		if (msg.getTextChannel() != this.channel) return true;
		if (!List.of(MessageType.DEFAULT, MessageType.INLINE_REPLY).contains(msg.getType())) return true;
		if (msg.getAuthor().isBot() || msg.isWebhookMessage()) return true;

		return false;
	}

	/* From Discord to Minecraft - End */

	/* From Minecraft to Discord - Start */

	@EventHandler(priority = EventPriority.MONITOR)
	public void onNewChat(AsyncChatEvent e) {
		final String DISCORD_ARROW = "**>**";

		String msg = processMsg(StringUtils.serializeComponent(e.message()));
		String sender = StringUtils.serializeComponent(e.getPlayer().displayName());

		channel.sendMessage(sender + " " + DISCORD_ARROW + " " + msg).queue();
	}

	private String processMsg(String original) {
		// Override to original goes here
		// TODO Filter some message like <@id>, <@!id>, ...
		return original;
	}

	/* From Minecraft to Discord - End */
}

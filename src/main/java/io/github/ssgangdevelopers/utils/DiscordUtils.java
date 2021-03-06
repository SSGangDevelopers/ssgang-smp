package io.github.ssgangdevelopers.utils;

import io.github.ssgangdevelopers.SSGangSMP;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Member;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class DiscordUtils {

	private static final AtomicReference<Guild> guild = new AtomicReference<>();

	public static void init() {
		SSGangSMP plugin = SSGangSMP.getInstance();
		guild.set(plugin.getChatChannel().getGuild());
		plugin.getServer().getScheduler().runTaskTimer(
						plugin,
						() -> guild.set(plugin.getJda().getGuildById(guild.get().getId())),
						60 * 20, // Run per minute
						60
		);
	}

	private static void check() {
		if (guild.get() == null) {
			init();
		}
	}

	/**
	 * Gets the user ID with the provided username.
	 *
	 * @param username the username to get the ID of.
	 * @return the user ID.
	 */
	@Nullable
	public static String getIdFromUsername(String username) {
		check();
		List<Member> userList = guild.get().getMembers();
		for (Member member : userList) {
			if ((member.getNickname() != null && member.getNickname().equals(username)) || member.getUser().getName().equals(username))
				return member.getId();
		}
		return null;
	}

	/**
	 * Get ID of channel from its name
	 *
	 * @param name The name to get the ID of.
	 * @return The id of channel.
	 */
	@Nullable
	public static String getIdFromChannelName(String name) {
		check();
		List<GuildChannel> channels = guild.get().getChannels();
		for (GuildChannel channel : channels) {
			if (channel.getName().equals(name))
				return channel.getId();
		}
		return null;
	}
}

package io.github.ssgangdevelopers.utils;

import io.github.ssgangdevelopers.SSGangSMP;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Member;

import javax.annotation.Nullable;
import java.util.List;

public class DiscordUtils {

	private static Guild guild;

	private static void checkAndUpdateGuild() {
		if (guild == null) {
			guild = SSGangSMP.getInstance().getChatChannel().getGuild();
		}
		guild = SSGangSMP.getInstance().getJda().getGuildById(guild.getId()); // Stay up-to-date
	}

	/**
	 * Gets the user ID with the provided username.
	 *
	 * @param username the username to get the ID of.
	 * @return the user ID.
	 */
	@Nullable
	public static String getIdFromUsername(String username) {
		checkAndUpdateGuild();
		List<Member> userList = guild.getMembers();
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
		checkAndUpdateGuild();
		List<GuildChannel> channels = guild.getChannels();
		for (GuildChannel channel : channels) {
			if (channel.getName().equals(name))
				return channel.getId();
		}
		return null;
	}
}

package io.github.ssgangdevelopers.utils;

import io.github.ssgangdevelopers.SSGangSMP;
import net.dv8tion.jda.api.entities.User;

import javax.annotation.Nullable;
import java.util.List;

public class DiscordUtils {

	/**
	 * Gets the user ID with the provided username.
	 *
	 * @param username the username to get the ID of.
	 * @return the user ID.
	 */
	@Nullable
	public static String getIdFromUsername(String username) {
		List<User> userList = SSGangSMP.getInstance().getJda().getUsers();
		for (User user : userList) {
			if (user.getName().equals(username)) {
				return user.getId();
			}
		}
		return null;
	}
}

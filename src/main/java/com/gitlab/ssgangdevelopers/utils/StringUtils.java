package com.gitlab.ssgangdevelopers.utils;

import org.jetbrains.annotations.NotNull;

public class StringUtils {
	/**
	 * Replaces the placeholder in string with their respective provided values.
	 *
	 * @param s the input string.
	 * @param keypair the keypair, organized like this: {@code {"key1", "val1"}, {"key2", "val2"}, ...}
	 * @return the string with all placeholders replaced to its respective values.
	 */
	@NotNull
	public static String replacePlaceholders(@NotNull String s, @NotNull String[]... keypair) {
		for (String[] kp : keypair) {
			if (kp.length < 2) {
				continue;
			}
			s = s.replace(String.format("{%s}", kp[0]), kp[1]);
		}
		return s;
	}
}

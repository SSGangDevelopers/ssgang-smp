package com.gitlab.ssgangdevelopers.utils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

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
		String res = s;
		for (String[] kp : keypair) {
			List<String> rkp = List.of(kp);
			if (kp.length < 2) {
				continue;
			}
			res = res.replace(String.format("{%s}", rkp.get(0)), rkp.get(1));
		}
		return res;
	}
}

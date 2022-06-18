package io.github.ssgangdevelopers.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.jetbrains.annotations.NotNull;

/**
 * Some methods for easier string processing
 */
public class StringUtils {
	/**
	 * Replaces the placeholder in string with their respective provided values.
	 *
	 * @param s       the input string.
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

	/**
	 * Extract text from {@link net.kyori.adventure.text.Component}.
	 *
	 * @param component The component which need to be extracted.
	 * @return The text extracted from provided component.
	 */
	@NotNull
	public static String serializeComponent(Component component) {
		String output;

		// May add some login in the future
		output = PlainTextComponentSerializer.plainText().serialize(component);

		return output;
	}
}

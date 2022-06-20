package io.github.ssgangdevelopers.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Some methods for easier string processing
 */
public class StringUtils {
	/**
	 * Replaces the placeholder in string with their respective provided values.
	 *
	 * @param input   the input string.
	 * @param keypair the keypair, organized like this: {@code {"key1", "val1"}, {"key2", "val2"}, ...}
	 * @return the string with all placeholders replaced to its respective values.
	 */
	@Contract(pure = true)
	@NotNull
	public static String replacePlaceholders(@NotNull String input, @NotNull String[]... keypair) {
		for (String[] kp : keypair) {
			if (kp.length < 2) {
				continue;
			}
			input = input.replace("{" + kp[0] + "}", kp[1]);
		}
		return input;
	}

	/**
	 * Extract text from {@link net.kyori.adventure.text.Component}.
	 *
	 * @param component The component which need to be extracted.
	 * @return The text extracted from provided component.
	 */
	@NotNull
	public static String serializeComponent(@NotNull Component component) {
		String output;

		if (component instanceof TextComponent) {
			output = PlainTextComponentSerializer.plainText().serialize(component);
		} else {
			if (component instanceof TranslatableComponent transComp) {
				String rawValue = LangUtils.get(transComp.key());
				List<String> args = new ArrayList<>(3);
				transComp.args().forEach((comp) -> args.add(serializeComponent(comp)));
				output = String.format(rawValue, args.toArray());
			} else {
				output = PlainTextComponentSerializer.plainText().serialize(component);
			}
		}

		return output;
	}
}

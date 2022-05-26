package io.github.ssgangdevelopers.utils;

import io.github.ssgangdevelopers.SSGangSMP;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * Represents a LangUtils to get localized display messages.
 */
public class LangUtils {
	private static final String DEFAULT_FILE_NAME = "messages-en.yml";
	private static final HashMap<String, Object> data = new HashMap<>();

	public static void load(@NotNull File file) {
		SSGangSMP.getInstance().getSLF4JLogger().info("Loading language properties from {}", file.getName());
		mainHandle(file);
	}

	private static void mainHandle(@NotNull File file) {
		SSGangSMP plugin = SSGangSMP.getInstance();
		try {
			data.putAll(new Yaml().load(new FileReader(file, StandardCharsets.UTF_8)));
		} catch (IOException e) {
			if (!file.getName().equals(DEFAULT_FILE_NAME)) {
				plugin.getSLF4JLogger().warn("Preferred lang file could not loaded, using default lang file.");
				mainHandle(new File(plugin.getDataFolder(), DEFAULT_FILE_NAME));
			} else {
				plugin.getSLF4JLogger().error("Could not load default lang file, disabling plugin...");
				SSGangSMP.selfDestruct();
			}
		}
	}

	/**
	 * Gets the localized string with its identifier.
	 *
	 * @param identifier the string's identifier.
	 * @return the string got from the language file.
	 */
	@SuppressWarnings("unchecked")
	@NotNull
	public static String get(@NotNull String identifier) {
		String[] sections = identifier.split("\\.");
		Object output = data;

		for (String section : sections) {
			if (output == null) break;
			output = ((HashMap<String, Object>) output).get(section);
		}

		return output != null ? (String) output : identifier;
		// Return identifier in case null for easier error tracking
	}

	/**
	 * Gets the localized string with its identifier.
	 *
	 * @param identifier the string's identifier.
	 * @param keypair    the keypair, organized like this: {@code {"key1", "val1"}, {"key2", "val2"}, ...}
	 * @return the string got from the language file.
	 */
	@NotNull
	public static String get(@NotNull String identifier, String[]... keypair) {
		String original = get(identifier);
		return StringUtils.replacePlaceholders(original, keypair);
	}

}

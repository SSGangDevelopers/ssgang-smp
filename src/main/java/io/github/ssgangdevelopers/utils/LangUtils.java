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
	private static final String DEFAULT_FILE_NAME = "messages/messages-en.yml";
	private static final HashMap<String, Object> data = new HashMap<>();

	/**
	 * Set language file for this util.
	 *
	 * @param file Language file to load.
	 */
	public static void init(@NotNull File file) {
		SSGangSMP.getInstance().getSLF4JLogger().info("Loading language properties from {}", file.getName());
		checkAndLoad(file);
	}

	private static void checkAndLoad(@NotNull File file) {
		SSGangSMP plugin = SSGangSMP.getInstance();
		try {
			data.putAll(new Yaml().load(new FileReader(file, StandardCharsets.UTF_8)));
		} catch (IOException e) {
			if (!file.getName().equals(DEFAULT_FILE_NAME)) {
				plugin.getSLF4JLogger().warn("Preferred lang file could not loaded, using default lang file.");
				checkAndLoad(new File(plugin.getDataFolder(), DEFAULT_FILE_NAME));
			} else {
				plugin.getSLF4JLogger().error("Could not load default language file, the plugin will be disabled as a result.");
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
	 * @return the string got from the language file with replaced placeholder.
	 */
	@NotNull
	public static String get(@NotNull String identifier, @NotNull String[]... keypair) {
		String original = get(identifier);
		return StringUtils.replacePlaceholders(original, keypair);
	}

}

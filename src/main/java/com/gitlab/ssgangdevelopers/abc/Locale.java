package com.gitlab.ssgangdevelopers.abc;

import com.gitlab.ssgangdevelopers.utils.StringUtils;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;

/**
 * Represents a Locale to get localized display messages.
 */
public class Locale {
	private final YamlConfiguration data;

	public Locale(@NotNull File f) throws IOException, InvalidConfigurationException {
		if (!f.exists()) {
			// Silently creates the file, but no warning (the first time the plugin is run)
			boolean a = f.createNewFile();
		}
		this.data = new YamlConfiguration();
		this.data.load(f);
	}

	/**
	 * Gets the localized string with its identifier.
	 *
	 * @param identifier the string's identifier.
	 * @return the string got from the language file.
	 */
	@Nullable
	public String get(@NotNull String identifier) {
		return (String) this.data.get(identifier);
	}

	/**
	 * Gets the localized string with its identifier.
	 *
	 * @param identifier the string's identifier.
	 * @param keypair the keypair, organized like this: {@code {"key1", "val1"}, {"key2", "val2"}, ...}
	 * @return the string got from the language file.
	 */
	@Nullable
	public String get(@NotNull String identifier, String[]... keypair) {
		String original =  (String) this.data.get(identifier);
		if (original == null) return null;
		return StringUtils.replacePlaceholders(original, keypair);
	}

}

package io.github.ssgangdevelopers.utils;

import net.kyori.adventure.text.format.TextColor;

import java.awt.*;

/**
 * The best value for each color on different platforms
 */
public class ColorUtils {
	public static final class MINECRAFT {
		public static final TextColor AQUA = TextColor.color(85, 255, 255);
		public static final TextColor YELLOW = TextColor.color(255, 255, 85);
	}

	public static final class DISCORD {
		public static final Color GREEN = new Color(87, 242, 137);
		public static final Color BLACK = new Color(0, 0, 0);
		public static final Color RED = new Color(237, 66, 69);
	}
}

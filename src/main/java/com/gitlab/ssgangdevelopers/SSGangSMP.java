package com.gitlab.ssgangdevelopers;

import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused") // Will remove in the future
public class SSGangSMP extends JavaPlugin {

	@Override
	public void onEnable() {
		super.onEnable();
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}

	public SSGangSMP() {
		super();
		INSTANCE = this;
	}

	private static SSGangSMP INSTANCE;

	public static SSGangSMP getInstance() {
		return INSTANCE;
	}
}
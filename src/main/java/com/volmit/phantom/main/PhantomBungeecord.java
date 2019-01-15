package com.volmit.phantom.main;

import java.io.File;

import com.volmit.phantom.api.config.Configurator;
import com.volmit.phantom.main.proxy.PhantomBungeeConfig;

import net.md_5.bungee.api.plugin.Plugin;

public class PhantomBungeecord extends Plugin
{
	@Override
	public void onEnable()
	{
		Configurator.DEFAULT.load(PhantomBungeeConfig.class, new File(getDataFolder(), "config.yml"));
	}

	@Override
	public void onDisable()
	{

	}
}

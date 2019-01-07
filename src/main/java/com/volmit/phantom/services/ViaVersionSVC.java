package com.volmit.phantom.services;

import org.bukkit.entity.Player;

import com.volmit.phantom.plugin.SimpleService;
import com.volmit.phantom.util.Protocol;

import us.myles.ViaVersion.api.Via;

public class ViaVersionSVC extends SimpleService
{
	@SuppressWarnings("unchecked")
	public Protocol getVersion(Player p)
	{
		return Protocol.getProtocolVersion(Via.getAPI().getPlayerVersion(p));
	}

	@Override
	public void onStart()
	{

	}

	@Override
	public void onStop()
	{

	}
}

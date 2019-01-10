package com.volmit.phantom.lib.service;

import org.bukkit.entity.Player;

import com.volmit.phantom.api.protocol.Protocol;
import com.volmit.phantom.api.service.IService;

import us.myles.ViaVersion.api.Via;

public class ViaVersionSVC implements IService
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

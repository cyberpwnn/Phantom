package com.volmit.phantom.plugin;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public enum TestType
{
	ANY,
	PLAYER,
	CONSOLE;

	public boolean supports(CommandSender sender)
	{
		return this.equals(ANY) || ((sender instanceof Player && this.equals(PLAYER)) || (!(sender instanceof Player) && this.equals(CONSOLE)));
	}
}

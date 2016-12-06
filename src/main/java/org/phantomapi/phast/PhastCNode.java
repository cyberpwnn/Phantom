package org.phantomapi.phast;

import org.bukkit.Bukkit;
import org.bukkit.event.server.ServerCommandEvent;
import org.phantomapi.Phantom;
import org.phantomapi.lang.GList;

/**
 * A Node for running console commands
 * 
 * @author cyberpwn
 */
public class PhastCNode extends PhastNode
{
	public PhastCNode()
	{
		super("c");
	}
	
	@Override
	public String phastHelp()
	{
		return "c [command] - Run console command";
	}
	
	@Override
	public void on(String[] args)
	{
		String command = new GList<String>(args).toString(" ");
		Phantom.instance().callEvent(new ServerCommandEvent(Bukkit.getConsoleSender(), command));
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
	}
}

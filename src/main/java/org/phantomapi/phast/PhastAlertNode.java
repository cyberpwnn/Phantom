package org.phantomapi.phast;

import org.bukkit.entity.Player;
import org.phantomapi.Phantom;
import org.phantomapi.lang.GList;
import org.phantomapi.util.F;

/**
 * A Node for broadcasting messages
 * 
 * @author cyberpwn
 */
public class PhastAlertNode extends PhastNode
{
	public PhastAlertNode()
	{
		super("alert");
	}
	
	@Override
	public String phastHelp()
	{
		return "alert [msg] - Broadcast message";
	}
	
	@Override
	public void on(String[] args)
	{
		String command = F.color(new GList<String>(args).toString(" "));
		
		for(Player i : Phantom.instance().onlinePlayers())
		{
			i.sendMessage(command);
		}
	}
}

package org.phantomapi.phast;

import org.bukkit.entity.Player;
import org.phantomapi.Phantom;
import org.phantomapi.lang.GList;
import org.phantomapi.nms.NMSX;
import org.phantomapi.util.F;

/**
 * A Node for sending title messages
 * 
 * @author cyberpwn
 */
public class PhastTitleNode extends PhastNode
{
	public PhastTitleNode()
	{
		super("title");
	}
	
	@Override
	public String phastHelp()
	{
		return "title [title//subtitle] - Broadcast title command";
	}
	
	@Override
	public void on(String[] args)
	{
		String command = new GList<String>(args).toString(" ");
		
		String title = command;
		String sub = "";
		
		if(command.contains("//"))
		{
			title = command.split("//")[0];
			sub = command.split("//")[1];
		}
		
		for(Player i : Phantom.instance().onlinePlayers())
		{
			NMSX.sendTitle(i, 5, 30, 60, F.color(title), F.color(sub));
		}
	}
}

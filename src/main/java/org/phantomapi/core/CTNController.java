package org.phantomapi.core;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.server.ServerCommandEvent;
import org.phantomapi.command.PhantomSender;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Controller;
import org.phantomapi.lang.GTime;
import org.phantomapi.ppa.PPA;
import org.phantomapi.ppa.PPAEndPoint;
import org.phantomapi.util.C;
import org.phantomapi.util.M;

public class CTNController extends Controller
{
	private PPAEndPoint e;
	
	public CTNController(Controllable parentController)
	{
		super(parentController);
	}
	
	@Override
	public void onStart()
	{
		e = new PPAEndPoint("ctn")
		{
			@Override
			public void onResponded(PPA packet)
			{
				String command = packet.getString("command");
				String name = packet.getString("from");
				w("Command Packet Sent " + " /" + command + " through " + name);
				
				for(Player i : onlinePlayers())
				{
					if(i.getName().equals(name))
					{
						i.sendMessage(C.GRAY + "Executed on " + C.WHITE + packet.getSource() + C.GRAY + " " + new GTime(M.ms() - packet.getTime()).ago());
					}
				}
			}
			
			@Override
			public PPA onReceived(PPA packet)
			{
				String command = packet.getString("command");
				String name = packet.getString("from");
				w("Command Packet Received from " + packet.getSource() + " /" + command + " through " + name);
				callEvent(new ServerCommandEvent(Bukkit.getConsoleSender(), command));
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
				PPA r = packet.createResponse();
				r.set("command", command);
				r.set("from", name);
				r.set("ok", true);
				r.set("ppat", packet.getTime());
				
				return r;
			}
		};
	}
	
	@Override
	public void onStop()
	{
		e.close();
	}
	
	public void dispatchCommandOverNetwork(String command, String server, PhantomSender sender)
	{
		PPA ppa = new PPA("ctn", server);
		ppa.set("command", command);
		ppa.set("from", sender.getName());
		ppa.send();
		w("Sending Command Packet to " + server + " /" + command + " through " + sender.getName());
		
		if(server.equals("all"))
		{
			callEvent(new ServerCommandEvent(Bukkit.getConsoleSender(), command));
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
		}
	}
}

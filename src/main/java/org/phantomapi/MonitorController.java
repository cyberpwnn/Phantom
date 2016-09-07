package org.phantomapi;

import org.bukkit.entity.Player;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Controller;
import org.phantomapi.construct.Ticked;
import org.phantomapi.lang.GMap;
import org.phantomapi.nms.NMSX;
import org.phantomapi.statistics.Monitorable;
import org.phantomapi.util.C;

@Ticked(3)
public class MonitorController extends Controller
{
	private GMap<String, Monitorable> samplers;
	private GMap<Player, String> monitors;
	
	public MonitorController(Controllable parentController)
	{
		super(parentController);
		
		samplers = new GMap<String, Monitorable>();
		monitors = new GMap<Player, String>();
	}
	
	@Override
	public void onStart()
	{
		
	}
	
	@Override
	public void onStop()
	{
		
	}
	
	public void unPlug(Player p)
	{
		monitors.remove(p);
		p.sendMessage(C.DARK_GRAY + "[" + C.LIGHT_PURPLE + "Phantom" + C.DARK_GRAY + "]: " + C.GRAY + "Unplugged");
	}
	
	public void getPlug(Player p)
	{
		p.sendMessage(C.DARK_GRAY + "[" + C.LIGHT_PURPLE + "Phantom" + C.DARK_GRAY + "]: " + C.GRAY + "Plugs: " + C.WHITE + C.BOLD + samplers.k().toString(", "));
	}
	
	public void hotPlug(Player p, String tag)
	{
		if(samplers.containsKey(tag))
		{
			monitors.put(p, tag);
			p.sendMessage(C.DARK_GRAY + "[" + C.LIGHT_PURPLE + "Phantom" + C.DARK_GRAY + "]: " + C.GRAY + "Hotplugged " + C.BOLD + C.WHITE + tag);
		}
		
		else
		{
			p.sendMessage(C.DARK_GRAY + "[" + C.LIGHT_PURPLE + "Phantom" + C.DARK_GRAY + "]: " + C.RED + "Cannot find plug: " + C.BOLD + C.WHITE + tag);
		}
	}
	
	public void onTick()
	{
		if(!monitors.isEmpty())
		{
			for(Player i : monitors.k())
			{
				NMSX.sendTitle(i, 0, 10, 10, C.DARK_GRAY + monitors.get(i), C.DARK_GRAY + samplers.get(monitors.get(i)).getMonitorableData());
			}
		}
	}
	
	public void register(Controllable c)
	{
		if(c instanceof Monitorable)
		{
			samplers.put(c.getName(), (Monitorable) c);
		}
	}
}

package org.phantomapi.core;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Controller;
import org.phantomapi.ext.Protocol;
import org.phantomapi.nms.EntityHider;
import org.phantomapi.nms.EntityHider.Policy;
import org.phantomapi.sync.TaskLater;

public class ProtocolController extends Controller
{
	private EntityHider entityHider;
	
	public ProtocolController(Controllable parentController)
	{
		super(parentController);
	}
	
	@Override
	public void onStart()
	{
		entityHider = new EntityHider(getPlugin(), Policy.BLACKLIST);
	}
	
	public EntityHider getHider()
	{
		return entityHider;
	}
	
	@Override
	public void onStop()
	{
		
	}
	
	@EventHandler
	public void on(PlayerJoinEvent e)
	{
		new TaskLater()
		{
			@Override
			public void run()
			{
				w(e.getPlayer().getName() + " <> Protocol " + Protocol.getProtocol(e.getPlayer()));
			}
		};
	}
}

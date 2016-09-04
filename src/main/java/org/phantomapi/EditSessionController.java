package org.phantomapi;

import org.bukkit.Location;
import org.bukkit.World;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Controller;
import org.phantomapi.construct.Ticked;
import org.phantomapi.lang.GMap;
import org.phantomapi.util.S;
import org.phantomapi.world.MaterialBlock;
import org.phantomapi.world.PPAFormat;
import org.phantomapi.world.PhantomEditSession;
import org.phantomapi.world.PhantomWorld;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;

@Ticked(0)
public class EditSessionController extends Controller
{
	private GMap<World, PhantomEditSession> sessions;
	private static GMap<Location, MaterialBlock> queue;
	
	public EditSessionController(Controllable parentController)
	{
		super(parentController);
		
		sessions = new GMap<World, PhantomEditSession>();
		queue = new GMap<Location, MaterialBlock>();
	}
	
	@Override
	public void onStart()
	{
		ClipboardFormat.addFormat(new PPAFormat());
	}
	
	public void onTick()
	{
		flush();
	}
	
	@Override
	public void onStop()
	{
		
	}
	
	public static void queue(Location location, MaterialBlock mb)
	{
		queue.put(location, mb);
	}
	
	public static void flush()
	{
		if(queue.isEmpty())
		{
			return;
		}
		
		for(Location i : queue.k())
		{
			Phantom.instance().getEditSessionController().getSession(i.getWorld()).set(i, queue.get(i));
		}
		
		queue.clear();
		
		new S()
		{
			@Override
			public void sync()
			{
				for(World i : Phantom.instance().getEditSessionController().sessions.k())
				{
					Phantom.instance().getEditSessionController().sessions.get(i).flush();
				}
			}
		};
	}
	
	public PhantomEditSession getSession(World w)
	{
		if(!sessions.containsKey(w))
		{
			sessions.put(w, new PhantomEditSession(new PhantomWorld(w)));
		}
		
		return sessions.get(w);
	}
}

package org.phantomapi;

import org.bukkit.World;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Controller;
import org.phantomapi.lang.GMap;
import org.phantomapi.world.PPAFormat;
import com.boydti.fawe.util.EditSessionBuilder;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;

public class EditSessionController extends Controller
{
	private GMap<World, EditSession> sessions;
	
	public EditSessionController(Controllable parentController)
	{
		super(parentController);
		
		sessions = new GMap<World, EditSession>();
	}

	@Override
	public void onStart()
	{
		ClipboardFormat.addFormat(new PPAFormat());
	}

	@Override
	public void onStop()
	{
		
	}
	
	public EditSession getSession(World w)
	{
		if(!sessions.containsKey(w))
		{
			sessions.put(w, new EditSessionBuilder(w.getName()).autoQueue(false).fastmode(true).build());
		}
		
		return sessions.get(w);
	}
}

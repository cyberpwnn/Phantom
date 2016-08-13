package org.phantomapi;

import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Controller;
import org.phantomapi.nms.EntityHider;
import org.phantomapi.nms.EntityHider.Policy;

public class ProtocolController extends Controller
{
	private EntityHider entityHider;
	
	public ProtocolController(Controllable parentController)
	{
		super(parentController);
	}
	
	public void onStart()
	{
		this.entityHider = new EntityHider(getPlugin(), Policy.BLACKLIST);
	}
	
	public EntityHider getHider()
	{
		return entityHider;
	}

	@Override
	public void onStop()
	{
		
	}
}

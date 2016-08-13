package org.cyberpwn.phantom;

import org.cyberpwn.phantom.construct.Controllable;
import org.cyberpwn.phantom.construct.Controller;
import org.cyberpwn.phantom.nms.EntityHider;
import org.cyberpwn.phantom.nms.EntityHider.Policy;

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

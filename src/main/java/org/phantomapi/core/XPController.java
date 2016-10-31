package org.phantomapi.core;

import org.phantomapi.clust.Comment;
import org.phantomapi.clust.ConfigurableController;
import org.phantomapi.clust.Keyed;
import org.phantomapi.construct.Controllable;
import org.phantomapi.xp.ExperienceDataController;

public class XPController extends ConfigurableController
{
	private ExperienceDataController edc;
	
	@Comment("Enable the experience system")
	@Keyed("enabled")
	public boolean enabled = false;
	
	public XPController(Controllable parentController)
	{
		super(parentController, "experience");
		
		edc = new ExperienceDataController(this);
		
		loadCluster(this);
		
		if(enabled)
		{
			register(edc);
		}
	}
	
	@Override
	public void onStart()
	{
		
	}
	
	@Override
	public void onStop()
	{
		
	}
}

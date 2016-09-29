package org.phantomapi.game;

import org.phantomapi.util.ResourcePack;

/**
 * Resource pack profiling
 * 
 * @author cyberpwn
 */
public class ResourcePackProfile
{
	private ResourcePack resourcePack;
	private ResourcePackMode mode;
	
	public ResourcePackProfile()
	{
		this.resourcePack = null;
		this.mode = ResourcePackMode.NONE;
	}

	public ResourcePack getResourcePack()
	{
		return resourcePack;
	}

	public void setResourcePack(ResourcePack resourcePack)
	{
		this.resourcePack = resourcePack;
	}

	public ResourcePackMode getMode()
	{
		return mode;
	}

	public void setMode(ResourcePackMode mode)
	{
		this.mode = mode;
	}
}

package com.volmit.phantom.stack.permissions;

import com.volmit.phantom.plugin.PhantomPermission;

public class PermissionRiftDelete extends PhantomPermission
{
	@Override
	protected String getNode()
	{
		return "destroy";
	}

	@Override
	public String getDescription()
	{
		return "Destroy rifts";
	}

	@Override
	public boolean isDefault()
	{
		return false;
	}
}

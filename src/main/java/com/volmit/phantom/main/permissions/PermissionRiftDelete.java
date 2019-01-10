package com.volmit.phantom.main.permissions;

import com.volmit.phantom.imp.plugin.PhantomPermission;

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

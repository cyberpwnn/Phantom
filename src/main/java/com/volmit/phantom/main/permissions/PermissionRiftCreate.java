package com.volmit.phantom.main.permissions;

import com.volmit.phantom.imp.plugin.PhantomPermission;

public class PermissionRiftCreate extends PhantomPermission
{
	@Override
	protected String getNode()
	{
		return "create";
	}

	@Override
	public String getDescription()
	{
		return "Create temporary rifts";
	}

	@Override
	public boolean isDefault()
	{
		return false;
	}
}

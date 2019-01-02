package com.volmit.phantom.stack.permissions;

import com.volmit.phantom.plugin.PhantomPermission;

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

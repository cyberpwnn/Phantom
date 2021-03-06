package com.volmit.phantom.main.permissions;

import com.volmit.phantom.api.command.PhantomPermission;

public class PermissionServices extends PhantomPermission
{
	@Override
	protected String getNode()
	{
		return "services";
	}

	@Override
	public String getDescription()
	{
		return "Permission to services";
	}

	@Override
	public boolean isDefault()
	{
		return false;
	}
}

package com.volmit.phantom.main.permissions;

import com.volmit.phantom.api.command.PhantomPermission;

public class PermissionModules extends PhantomPermission
{
	@Override
	protected String getNode()
	{
		return "modules";
	}

	@Override
	public String getDescription()
	{
		return "Permission to modules";
	}

	@Override
	public boolean isDefault()
	{
		return false;
	}
}

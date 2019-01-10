package com.volmit.phantom.main.permissions;

import com.volmit.phantom.api.command.PhantomPermission;

public class PermissionRiftReboot extends PhantomPermission
{
	@Override
	protected String getNode()
	{
		return "reload";
	}

	@Override
	public String getDescription()
	{
		return "Reload rifts";
	}

	@Override
	public boolean isDefault()
	{
		return false;
	}
}

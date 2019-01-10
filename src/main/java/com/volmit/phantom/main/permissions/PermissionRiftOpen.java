package com.volmit.phantom.main.permissions;

import com.volmit.phantom.api.command.PhantomPermission;

public class PermissionRiftOpen extends PhantomPermission
{
	@Override
	protected String getNode()
	{
		return "open";
	}

	@Override
	public String getDescription()
	{
		return "Open rifts";
	}

	@Override
	public boolean isDefault()
	{
		return false;
	}
}

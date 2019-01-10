package com.volmit.phantom.main.permissions;

import com.volmit.phantom.api.command.PhantomPermission;

public class PermissionRiftVisit extends PhantomPermission
{
	@Override
	protected String getNode()
	{
		return "visit";
	}

	@Override
	public String getDescription()
	{
		return "Visit rifts";
	}

	@Override
	public boolean isDefault()
	{
		return false;
	}
}

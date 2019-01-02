package com.volmit.phantom.stack.permissions;

import com.volmit.phantom.plugin.PhantomPermission;

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

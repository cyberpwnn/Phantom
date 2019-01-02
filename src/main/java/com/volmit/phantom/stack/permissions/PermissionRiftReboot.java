package com.volmit.phantom.stack.permissions;

import com.volmit.phantom.plugin.PhantomPermission;

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

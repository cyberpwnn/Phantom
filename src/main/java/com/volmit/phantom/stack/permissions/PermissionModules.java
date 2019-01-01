package com.volmit.phantom.stack.permissions;

import com.volmit.phantom.plugin.PhantomPermission;

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
		return "List active modules";
	}

	@Override
	public boolean isDefault()
	{
		return false;
	}
}

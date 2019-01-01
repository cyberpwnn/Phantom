package com.volmit.phantom.stack.permissions;

import com.volmit.phantom.plugin.PhantomPermission;

public class PermissionRiftClose extends PhantomPermission
{
	@Override
	protected String getNode()
	{
		return "close";
	}

	@Override
	public String getDescription()
	{
		return "Close rifts";
	}

	@Override
	public boolean isDefault()
	{
		return false;
	}
}

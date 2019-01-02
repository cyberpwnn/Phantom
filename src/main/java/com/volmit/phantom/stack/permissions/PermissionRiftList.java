package com.volmit.phantom.stack.permissions;

import com.volmit.phantom.plugin.PhantomPermission;

public class PermissionRiftList extends PhantomPermission
{
	@Override
	protected String getNode()
	{
		return "list";
	}

	@Override
	public String getDescription()
	{
		return "List rifts";
	}

	@Override
	public boolean isDefault()
	{
		return false;
	}
}

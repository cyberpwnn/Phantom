package com.volmit.phantom.main.permissions;

import com.volmit.phantom.api.command.PhantomPermission;
import com.volmit.phantom.api.module.Permission;

public class PermissionPhantom extends PhantomPermission
{
	@Permission
	public PermissionModules modules;

	@Permission
	public PermissionServices services;

	@Override
	protected String getNode()
	{
		return "phantom";
	}

	@Override
	public String getDescription()
	{
		return "Permission to everything";
	}

	@Override
	public boolean isDefault()
	{
		return false;
	}
}

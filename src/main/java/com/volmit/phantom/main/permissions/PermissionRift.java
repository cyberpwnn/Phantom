package com.volmit.phantom.main.permissions;

import com.volmit.phantom.imp.plugin.PhantomPermission;
import com.volmit.phantom.imp.plugin.Scaffold.Permission;

public class PermissionRift extends PhantomPermission
{
	@Permission
	public PermissionRiftOpen open;

	@Permission
	public PermissionRiftList list;

	@Permission
	public PermissionRiftClose close;

	@Permission
	public PermissionRiftVisit visit;

	@Permission
	public PermissionRiftDelete delete;

	@Permission
	public PermissionRiftCreate create;

	@Permission
	public PermissionRiftReboot reload;

	@Override
	protected String getNode()
	{
		return "rift";
	}

	@Override
	public String getDescription()
	{
		return "The root permission node for rifts";
	}

	@Override
	public boolean isDefault()
	{
		return false;
	}
}

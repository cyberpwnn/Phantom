package com.volmit.phantom.main;

import java.lang.reflect.Field;

import com.volmit.phantom.api.job.J;
import com.volmit.phantom.api.lang.D;
import com.volmit.phantom.api.module.Command;
import com.volmit.phantom.api.module.Instance;
import com.volmit.phantom.api.module.Module;
import com.volmit.phantom.api.module.ModuleDescription;
import com.volmit.phantom.api.module.ModuleOperation;
import com.volmit.phantom.api.module.Permission;
import com.volmit.phantom.api.sheduler.A;
import com.volmit.phantom.api.sheduler.S;
import com.volmit.phantom.main.commands.CommandModules;
import com.volmit.phantom.main.commands.CommandServices;
import com.volmit.phantom.main.permissions.PermissionPhantom;
import com.volmit.phantom.util.text.C;

public class PhantomModule extends Module
{
	@Command("Modules")
	public CommandModules modules;

	@Command("Services")
	public CommandServices services;

	@Permission
	public static PermissionPhantom perm;

	@Instance
	public static PhantomModule mod;

	@Override
	public boolean isNative()
	{
		return true;
	}

	public static PhantomModule create() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		PhantomModule mod = new PhantomModule();
		ModuleDescription desc = new ModuleDescription();
		desc.setName("Phantom");
		desc.setAuthors("cyberpwn");
		desc.setVersion("1.0");
		desc.setColor(C.LIGHT_PURPLE);
		Field fd = Module.class.getDeclaredField("description");
		fd.setAccessible(true);
		fd.set(mod, desc);
		mod.executeModuleOperation(ModuleOperation.REGISTER_INSTANCES);

		new S()
		{
			@Override
			public void run()
			{
				mod.executeModuleOperation(ModuleOperation.REGISTER_PERMISSIONS);

				new A()
				{
					@Override
					public void run()
					{
						mod.executeModuleOperation(ModuleOperation.REGISTER_COMMANDS);
						mod.executeModuleOperation(ModuleOperation.REGISTER_CONFIGS);
						J.s(() -> mod.executeModuleOperation(ModuleOperation.START));
						D.ll("Started Module: " + mod.getName());
					}
				};
			}
		};

		return mod;
	}
}

package com.volmit.phantom.api.module;

import java.io.File;

import com.volmit.phantom.api.lang.GList;
import com.volmit.phantom.api.lang.GSet;
import com.volmit.phantom.api.lang.Logged;
import com.volmit.phantom.api.service.IService;
import com.volmit.phantom.util.text.C;

public interface IModule extends Logged
{
	public GList<Class<? extends IService>> getRegisteredServices();

	public GSet<Class<?>> getModuleClasses();

	public GSet<Class<?>> getModuleClasses(Class<?> c);

	public Object executeModuleOperation(ModuleOperation op, Object... params);

	public File getDataFile(String... path);

	public File getDataFolder(String... folders);

	public void forceBindService(Class<? extends IService> svc);

	public File getModuleFile();

	public String getName();

	public String getVersion();

	public String getAuthors();

	public C getColor();

	public String getTag();

	public String getTag(String sub);

	public ModuleDescription getDescription();

	public boolean isNative();
}

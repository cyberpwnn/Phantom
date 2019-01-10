package com.volmit.phantom.api.plugin;

import java.io.File;

import com.volmit.phantom.imp.plugin.Scaffold.ModuleInfo;
import com.volmit.phantom.imp.plugin.StructuredModule;
import com.volmit.phantom.util.text.C;

public interface IModule
{
	public File getDataFile(String... path);

	public File getDataFolder(String... folders);

	public File getModuleFile();

	public StructuredModule getStructure();

	public ModuleInfo getModuleInfo();

	public String getName();

	public String getVersion();

	public String getAuthor();

	public C getColor();

	public String getTag();

	public String getTag(String sub);
}

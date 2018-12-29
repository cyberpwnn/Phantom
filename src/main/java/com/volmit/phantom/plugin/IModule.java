package com.volmit.phantom.plugin;

import java.io.File;

import com.volmit.phantom.plugin.Scaffold.ModuleInfo;
import com.volmit.phantom.text.C;

public interface IModule
{
	public File getDataFile(String... path);

	public File getDataFolder(String... folders);

	public StructuredModule getStructure();

	public ModuleInfo getModuleInfo();

	public String getName();

	public String getVersion();

	public String getAuthor();

	public C getColor();

	public String getTag();

	public String getTag(String sub);
}

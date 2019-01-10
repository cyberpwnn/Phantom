package com.volmit.phantom.api.module;

import java.io.File;

import com.volmit.phantom.util.text.C;

public interface IModule
{
	public File getDataFile(String... path);

	public File getDataFolder(String... folders);

	public File getModuleFile();

	public String getName();

	public String getVersion();

	public String getAuthor();

	public C getColor();

	public String getTag();

	public String getTag(String sub);

	public ModuleDescription getDescription();
}

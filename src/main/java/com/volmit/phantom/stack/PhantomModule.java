package com.volmit.phantom.stack;

import com.volmit.phantom.plugin.Module;
import com.volmit.phantom.plugin.SVC;
import com.volmit.phantom.plugin.Scaffold.Command;
import com.volmit.phantom.plugin.Scaffold.Instance;
import com.volmit.phantom.plugin.Scaffold.ModuleInfo;
import com.volmit.phantom.plugin.Scaffold.Start;
import com.volmit.phantom.services.DevelopmentSVC;
import com.volmit.phantom.text.C;

@ModuleInfo(name = "Phantom", version = "HEAD", author = "cyberpwn", color = C.LIGHT_PURPLE)
public class PhantomModule extends Module
{
	@Instance
	public static PhantomModule instance;

	@Command
	public CommandPhantom phantom;

	@Command("Modules")
	public CommandModules modules;

	@Command("Services")
	public CommandServices services;

	@Start
	public void start()
	{
		SVC.start(DevelopmentSVC.class);
	}
}

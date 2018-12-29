package com.volmit.phantom.apitests;

import com.volmit.phantom.plugin.Module;
import com.volmit.phantom.plugin.Scaffold.Instance;
import com.volmit.phantom.plugin.Scaffold.ModuleInfo;
import com.volmit.phantom.plugin.Scaffold.Start;
import com.volmit.phantom.plugin.Scaffold.Stop;
import com.volmit.phantom.plugin.Scaffold.Tick;
import com.volmit.phantom.text.C;

@ModuleInfo(name = "SkyPrime", version = "1.1", author = "cyberpwn", color = C.AQUA)
public class SkyPrime extends Module
{
	@Instance
	public static SkyPrime instance;

	@Start
	public void start()
	{

	}

	@Stop
	public void stop()
	{

	}

	@Tick(5)
	public void tick()
	{

	}
}

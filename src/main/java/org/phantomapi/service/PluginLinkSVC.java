package org.phantomapi.service;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.phantomapi.Phantom;

import phantom.dispatch.PD;
import phantom.lang.GMap;
import phantom.pawn.Name;
import phantom.pawn.Register;
import phantom.pawn.Singular;
import phantom.pawn.Start;
import phantom.pawn.Stop;
import phantom.pluginadapter.IPluginLink;
import phantom.service.IService;
import phantom.util.metrics.Documented;

/**
 * Service for linking plugins
 *
 * @author cyberpwn
 */
@Documented
@Register
@Name("SVC Plugin Link")
@Singular
public class PluginLinkSVC implements IService
{
	private GMap<Class<? extends IPluginLink>, IPluginLink> links;

	@Start
	public void start()
	{
		links = new GMap<Class<? extends IPluginLink>, IPluginLink>();
	}

	@Stop
	public void stop()
	{

	}

	@SuppressWarnings("unchecked")
	public <T extends IPluginLink> T getLink(Class<? extends T> t)
	{
		if(!links.containsKey(t))
		{
			try
			{
				IPluginLink l = t.getConstructor().newInstance();
				Phantom.activate(l);
				Phantom.claim(this, l);
				links.put(t, l);
				PD.v("Plugin Adapter: " + l.getClass().getSimpleName());
			}
			
			catch(InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e)
			{
				Phantom.kick(e);
			}
		}
		
		return (T) links.get(t);
	}

	public boolean isPluginInstalled(String p, String v)
	{
		return isPluginInstalled(p) && getPlugin(p).getDescription().getVersion().equals(v);
	}

	public boolean isPluginInstalled(String p)
	{
		return getPlugin(p) != null;
	}

	public Plugin getPlugin(String p)
	{
		return Bukkit.getPluginManager().getPlugin(p);
	}
}

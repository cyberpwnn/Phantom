package org.cyberpwn.phantom.construct;

import org.bukkit.event.Listener;
import org.cyberpwn.phantom.lang.GList;

public interface Controllable extends Listener
{
	public void start();
	public void stop();
	public void tick();
	public void onStart();
	public void onStop();
	public void onTick();
	public void register(Controller c);
	public ControllablePlugin getPlugin();
	public Controllable getParentController();
	public String getName();
	public GList<Controllable> getControllers();
}

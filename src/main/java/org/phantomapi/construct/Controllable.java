package org.phantomapi.construct;

import org.bukkit.event.Listener;
import org.phantomapi.lang.GList;

/**
 * Controllable object
 * 
 * @author cyberpwn
 */
public interface Controllable extends Listener, ControllerMessenger
{
	/**
	 * Start the controller
	 */
	public void start();
	
	/**
	 * Get MS time on cpu
	 * 
	 * @return time on cpu.
	 */
	public double getTime();
	
	/**
	 * Stop the controller
	 */
	public void stop();
	
	/**
	 * Reload the controller and all subcontrollers
	 */
	public void reload();
	
	/**
	 * Unregister the controller
	 * 
	 * @param c
	 *            the controller
	 */
	public void unregister(Controllable c);
	
	/**
	 * Called BEFORE the controller is reloaded
	 */
	public void onReload();
	
	/**
	 * Tick the controler
	 */
	public void tick();
	
	/**
	 * Override for start
	 */
	public void onStart();
	
	/**
	 * Override for stop
	 */
	public void onStop();
	
	/**
	 * Override for ticking
	 */
	public void onTick();
	
	/**
	 * Should this controller be ticking
	 * 
	 * @return true if its ticked
	 */
	public boolean isTicked();
	
	/**
	 * Called before onstart
	 */
	public void onPreStart();
	
	/**
	 * Called after onstart
	 */
	public void onPostStop();
	
	/**
	 * Called after all controllers in and under phantom have loaded. This is
	 * invoked by phantom
	 */
	public void onLoadComplete();
	
	/**
	 * Called after all plugins have started
	 */
	public void onPluginsComplete();
	
	/**
	 * Register a subcontroller of this controller By registering a
	 * subcontroller to this controller THIS controller will start/stop the
	 * registrant when THIS controller start/stops
	 * 
	 * @param c
	 *            the registrant
	 */
	public void register(Controller c);
	
	/**
	 * Get the root controller plugin
	 * 
	 * @return the plugin
	 */
	public ControllablePlugin getPlugin();
	
	/**
	 * Get the parent controller
	 * 
	 * @return the parent controller
	 */
	public Controllable getParentController();
	
	/**
	 * The name of this controller
	 * 
	 * @return the name
	 */
	public String getName();
	
	/**
	 * Get all subcontrollers of this controller
	 * 
	 * @return a list of controllers
	 */
	public GList<Controllable> getControllers();
	
	/**
	 * Get any controller on the server by name
	 * 
	 * @param name
	 *            the controller name
	 * @return the controller or null if not found
	 */
	public Controllable getController(String name);
}

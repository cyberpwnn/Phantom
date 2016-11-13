package org.phantomapi.construct;

import java.io.File;
import org.bukkit.event.Listener;
import org.phantomapi.clust.Configurable;
import org.phantomapi.lang.GList;
import org.phantomapi.util.D;

/**
 * Controllable object. A controllable object has several functions that make it
 * truly controllable.<br />
 * <ul>
 * <li>A Controllable object can be started, stopped, and ticked with
 * the @Ticked annotation</li>
 * <li>A Controllable object must have a parent controller unless it is a
 * controllable plugin</li>
 * <li>A Controllable object can contain sub controllers which must be started
 * before this controller starts
 * <p>
 * </li>
 * </ul>
 * 
 * @author cyberpwn
 */
public interface Controllable extends Listener, ControllerMessenger
{
	/**
	 * Start the controller. This starts all sub controllers first before
	 * starting
	 * itself. Do not override this unless you know what you are doing. Override
	 * the
	 * onStart()
	 */
	public void start();
	
	/**
	 * Get the dispatcher of this controller
	 * 
	 * @return the dispatcher
	 */
	public D getDispatcher();
	
	/**
	 * Get MS time on cpu. Returns the milliseconds used for ticking and
	 * starting
	 * this controller
	 * 
	 * @return time on cpu.
	 */
	public double getTime();
	
	/**
	 * Stop the controller. Stops the controller by disabling all sub
	 * controllers,
	 * then stopping itself after. Do not override this unless you know what you
	 * are
	 * doing. Override the onStop method
	 */
	public void stop();
	
	/**
	 * Load the sqlite data into the cluster
	 * 
	 * @param c
	 *            the configurable object
	 * @param file
	 *            the file
	 */
	public void loadSqLite(Configurable c, File file);
	
	/**
	 * Save cluster data into the sqlite db
	 * 
	 * @param c
	 *            the cluster
	 * @param file
	 *            the sqlite db
	 */
	public void saveSqLite(Configurable c, File file);
	
	/**
	 * Close and unlock the sqlite file
	 * 
	 * @param file
	 *            the file
	 */
	public void closeSqLite(File file);
	
	/**
	 * Load mysql data into the given cluster
	 * 
	 * @param c
	 *            the cluster
	 * @param finish
	 *            when its done
	 */
	public void loadMysql(Configurable c, Runnable finish);
	
	/**
	 * Load the mysql data into the given cluster
	 * 
	 * @param c
	 *            the cluster
	 */
	public void loadMysql(Configurable c);
	
	/**
	 * Save cluster data into the mysql db
	 * 
	 * @param c
	 *            the cluster
	 */
	public void saveMysql(Configurable c);
	
	/**
	 * Save cluster data into the mysql db
	 * 
	 * @param c
	 *            the cluster
	 * @param finish
	 *            when its done
	 */
	public void saveMysql(Configurable c, Runnable finish);
	
	/**
	 * Reload the controller and all subcontrollers. Simply calls stop(), then
	 * start() following the process of controller structure. Before this
	 * happens,
	 * onReload is called.
	 */
	public void reload();
	
	/**
	 * Unregister the controller. Unregisters the controller from this
	 * controller
	 * and phantom, along with any other post controller processing done on
	 * setup
	 * 
	 * @param c
	 *            the controller to be unregistered from this controller and
	 *            phantom
	 */
	public void unregister(Controllable c);
	
	/**
	 * Called when a controller is reloading (all sub controllers will reload)
	 */
	public void onReload();
	
	/**
	 * Tick the controler. This is called by a phantom provider in the plugin
	 * holding this controller. To get this to fire, either tick it, or
	 * use @Ticked(interval) on this class
	 */
	public void tick();
	
	/**
	 * Override for start. Called when this controller is starting up. This
	 * could be
	 * called async. If you need it sync, either use S.class, or
	 * annotate @SyncStart
	 * for this class
	 */
	public void onStart();
	
	/**
	 * Override for stop. Called when the controller is stopping (likley a
	 * plugin
	 * disable)
	 */
	public void onStop();
	
	/**
	 * Override for ticking. Called when the phantom provider has decided to
	 * tick
	 * this plugin or tick was called
	 */
	public void onTick();
	
	/**
	 * Should this controller be ticking. If this controller has @Ticked() on
	 * the
	 * type, it will return true
	 * 
	 * @return true if its ticked
	 */
	public boolean isTicked();
	
	/**
	 * Called before onstart. This is called before the controller has started
	 * but
	 * after it has been initialized
	 */
	public void onPreStart();
	
	/**
	 * Called after onstop. This is called for any last minute things that need
	 * to
	 * be done. You cannot schedule tasks here (they would die before they
	 * start)
	 */
	public void onPostStop();
	
	/**
	 * Called after all controllers in and under phantom have loaded. This is
	 * invoked by phantom.
	 */
	public void onLoadComplete();
	
	/**
	 * Called after all plugins have started and the server is active
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
	 * Get the root controller plugin (your plugin)
	 * 
	 * @return the plugin
	 */
	public ControllablePlugin getPlugin();
	
	/**
	 * Get the parent controller. This controller may be a sub controller of
	 * another
	 * controller or controllable plugin.
	 * 
	 * @return the parent controller or null if this is a controllable plugin
	 *         since
	 *         a plugin does not have a parent controller
	 */
	public Controllable getParentController();
	
	/**
	 * The name of this controller. The name is effectivley
	 * class.getSimpleName()
	 * 
	 * @return the name
	 */
	public String getName();
	
	/**
	 * Get all subcontrollers of this controller. Modifying this causes
	 * unexpected
	 * functionality
	 * 
	 * @return a list of controllers
	 */
	public GList<Controllable> getControllers();
	
	/**
	 * Get any controller on the server by name. This allows you to get
	 * controllers
	 * from other plugins on the same server even if it is not in your
	 * classpath.
	 * You can then send this controller a message in a datacluster format to
	 * transfer data back and fourth. It is much faster than reflection.
	 * 
	 * @param name
	 *            the controller name (class.getSimpleName)
	 * @return the controller or null if not found
	 */
	public Controllable getController(String name);
}

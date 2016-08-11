package org.cyberpwn.phantom.physics;

import org.bukkit.util.Vector;
import org.cyberpwn.phantom.Phantom;
import org.cyberpwn.phantom.lang.GList;
import org.cyberpwn.phantom.sync.ExecutiveRunnable;
import org.cyberpwn.phantom.sync.Task;

/**
 * Simulate gravity with multiple objects at a specific tickrate
 * 
 * @author cyberpwn
 *
 */
public class PhysicalSimulation
{
	private GList<Physical> objects;
	private Task task;
	private Boolean gravity;
	
	/**
	 * Create a new simulation. This initializes empty holders for new objects
	 * to be added
	 */
	public PhysicalSimulation()
	{
		this.objects = new GList<Physical>();
		this.task = null;
		this.gravity = false;
	}
	
	/**
	 * Attaches a physical object to this simulation. You are permitted to
	 * attach multiple object to one simulation, or attach an object to multiple
	 * simulations. Just be sure you know what you are doing.
	 * 
	 * @param p
	 *            the physical object
	 */
	public void attach(Physical p)
	{
		objects.add(p);
	}
	
	/**
	 * This function is called when the entire simulation is ticked. This by
	 * default handles gravity.
	 */
	public void tick()
	{
		Phantom.schedule("physics", objects.copy().iterator(new ExecutiveRunnable<Physical>()
		{
			public void run()
			{
				onPreProcess(next());
			}
		}));
		
		for(Physical i : objects)
		{
			GList<Physical> oobj = objects.copy().qdel(i);
			Phantom.schedule("physics", oobj.iterator(new ExecutiveRunnable<Physical>()
			{
				public void run()
				{
					onPrePhysics(next());
					onPostPhysics(i);
					i.influenceGravity(next());
					
					if(getGravity())
					{
						i.influenceForce(new Vector(0, -0.1, 0));
					}
					
					onPostPhysics(next());
					onPostPhysics(i);
				}
			}));
		}
		
		Phantom.schedule("physics", objects.copy().iterator(new ExecutiveRunnable<Physical>()
		{
			public void run()
			{
				onPostProcess(next());
			}
		}));
	}
	
	/**
	 * This is called before physics is applied to a specific object in the
	 * simulation. Please keep in mind that each object may be fired many times
	 * per tick, not once per tick. For once per tick, use onPreProcess and
	 * onPostProces instead
	 * 
	 * @param p
	 *            the physical object
	 */
	public void onPrePhysics(Physical p)
	{
		
	}
	
	/**
	 * This is called after physics is applied to a specific object in the
	 * simulation. Please keep in mind that each object may be fired many times
	 * per tick, not once per tick. For once per tick, use onPreProcess and
	 * onPostProces instead
	 * 
	 * @param p
	 *            the physical object
	 */
	public void onPostPhysics(Physical p)
	{
		
	}
	
	/**
	 * This is called as soon as the tick begins, ensuring that this is called
	 * before any physics are applied on this object. This is fired once per
	 * tick per object
	 * 
	 * @param p
	 *            the physical object
	 */
	public void onPreProcess(Physical p)
	{
		
	}
	
	/**
	 * This is called after the tick is near complete. Everything else has
	 * fired, and we are about to finish the tick. Here is a good place for
	 * correcting things, checking position and more. This is fired once per
	 * tick per object
	 * 
	 * @param p
	 *            the physical object
	 */
	public void onPostProcess(Physical p)
	{
		
	}
	
	/**
	 * Start the simulation at the desired tickrate
	 * 
	 * @param tickrate
	 *            the tickrate, where 0/1 == 20tps, and 20 == 1tps
	 */
	public void start(int tickrate)
	{
		task = new Task(tickrate)
		{
			public void run()
			{
				tick();
			}
		};
	}
	
	/**
	 * Stop any tasks running here if they are. This method is idiot safe.
	 * However please note that if the simulation was too demanding, the rest of
	 * the ticks will be completed, in which case no more will be requested to
	 * tick, however some may still be finishing up when this is called
	 */
	public void stop()
	{
		if(task != null && task.isRunning())
		{
			task.cancel();
		}
	}

	public GList<Physical> getObjects()
	{
		return objects;
	}

	public void setObjects(GList<Physical> objects)
	{
		this.objects = objects;
	}

	public Task getTask()
	{
		return task;
	}

	public void setTask(Task task)
	{
		this.task = task;
	}

	public Boolean getGravity()
	{
		return gravity;
	}

	public void setGravity(Boolean gravity)
	{
		this.gravity = gravity;
	}
}

package org.cyberpwn.phantom;

import org.bukkit.entity.Player;
import org.cyberpwn.phantom.construct.Controllable;
import org.cyberpwn.phantom.construct.Controller;
import org.cyberpwn.phantom.construct.Ticked;
import org.cyberpwn.phantom.gui.Notification;
import org.cyberpwn.phantom.lang.GList;
import org.cyberpwn.phantom.lang.GMap;
import org.cyberpwn.phantom.lang.Priority;
import org.cyberpwn.phantom.sync.ExecutiveRunnable;
import org.cyberpwn.phantom.sync.ExecutiveTask;

@Ticked(0)
public class NotificationController extends Controller
{
	private GMap<Player, GMap<Priority, GList<Notification>>> queue;
	private GMap<Player, Integer> holds;
	private ExecutiveTask<Player> task;
	
	public NotificationController(Controllable parentController)
	{
		super(parentController);
		
		this.task = null;
		this.holds = new GMap<Player, Integer>();
		this.queue = new GMap<Player, GMap<Priority, GList<Notification>>>();
	}
	
	public void onTick()
	{
		if(!queue.isEmpty())
		{
			if(task == null || !task.isRunning())
			{
				task = new ExecutiveTask<Player>(queue.k().iterator(new ExecutiveRunnable<Player>()
				{
					public void run()
					{
						Player p = next();
						
						if(holds.containsKey(p))
						{
							holds.put(p, holds.get(p) - 1);
							
							if(holds.get(p) <= 0)
							{
								holds.remove(p);
							}
							
							return;
						}
						
						for(Priority i : Priority.topDown())
						{
							if(queue.get(p).containsKey(i))
							{
								queue.get(p).get(i).get(0).play(p);
								
								if(!queue.get(p).get(i).get(0).getOngoing())
								{
									holds.put(p, queue.get(p).get(i).get(0).getTitle().totalTime());
								}
								
								queue.get(p).get(i).remove(0);
								
								if(queue.get(p).get(i).isEmpty())
								{
									queue.get(p).remove(i);
								}
							}
						}
						
						if(queue.get(p).isEmpty())
						{
							queue.remove(p);
						}
					}
				}), 0.1, 0, new Runnable()
				{
					@Override
					public void run()
					{
						
					}
				});
			}
		}
	}
	
	public void queue(Player p, Notification n)
	{
		if(!queue.containsKey(p))
		{
			queue.put(p, new GMap<Priority, GList<Notification>>());
		}
		
		if(!queue.get(p).containsKey(n.getPriority()))
		{
			queue.get(p).put(n.getPriority(), new GList<Notification>());
		}
		
		queue.get(p).get(n.getPriority()).add(n);
	}
	
	public void queue(Notification n)
	{
		new ExecutiveTask<Player>(Phantom.instance().onlinePlayers().iterator(new ExecutiveRunnable<Player>()
		{
			public void run()
			{
				queue(next(), n);
			}
		}), 0.1, 0, new Runnable()
		{
			@Override
			public void run()
			{
				
			}
		});
	}
}

package org.phantomapi.core;

import java.util.Date;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.phantomapi.clust.Comment;
import org.phantomapi.clust.ConfigurableController;
import org.phantomapi.clust.Keyed;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Ticked;
import org.phantomapi.nms.NMSX;
import org.phantomapi.sync.Task;
import org.phantomapi.sync.TaskLater;
import org.phantomapi.text.SYM;
import org.phantomapi.text.TXT;
import org.phantomapi.util.C;
import org.phantomapi.util.FinalInteger;
import org.phantomapi.util.P;

@Ticked(20)
public class RebootController extends ConfigurableController
{
	@Keyed("enabled")
	public boolean enabled = false;
	
	@Keyed("time.hours")
	@Comment("The hour to reboot on (1-24)")
	public int hours = 5;
	
	@Keyed("time.minutes")
	@Comment("The minute to reboot on (1-60)")
	public int minutes = 35;
	
	@Keyed("time.warning-time")
	@Comment("The time to give warning of the reboot in seconds")
	public int seconds = 60;
	
	private boolean rebooting;
	
	public RebootController(Controllable parentController)
	{
		super(parentController, "rebooter");
		
		rebooting = false;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onTick()
	{
		if(!enabled)
		{
			return;
		}
		
		Date d = new Date();
		int minute = d.getMinutes();
		int hour = d.getHours();
		
		if(minute == minutes && hour == hours && !rebooting)
		{
			reboot(seconds);
		}
	}
	
	public void reboot(int mt)
	{
		if(rebooting)
		{
			rebooting = false;
			
			new TaskLater(25)
			{
				@Override
				public void run()
				{
					for(Player i : onlinePlayers())
					{
						P.showProgress(i, C.LIGHT_PURPLE + ";Reboot Cancelled");
					}
					
					new TaskLater(100)
					{
						@Override
						public void run()
						{
							for(Player i : onlinePlayers())
							{
								P.clearProgress(i);
							}
						}
					};
				}
			};
			
			return;
		}
		
		rebooting = true;
		
		FinalInteger fi = new FinalInteger(mt);
		
		for(Player i : onlinePlayers())
		{
			P.showProgress(i, C.GOLD + "" + SYM.SYMBOL_WARNING + C.RED + " Reboot;" + C.DARK_GRAY + "Rebooting in " + C.RED + mt + C.DARK_GRAY + " seconds.");
		}
		
		new TaskLater(100)
		{
			@Override
			public void run()
			{
				for(Player i : onlinePlayers())
				{
					P.clearProgress(i);
				}
				
				new Task(20)
				{
					@Override
					public void run()
					{
						if(!rebooting)
						{
							cancel();
							
							for(Player i : onlinePlayers())
							{
								P.clearProgress(i);
							}
							
							return;
						}
						
						if(fi.get() < 0)
						{
							cancel();
							Bukkit.shutdown();
							return;
						}
						
						fi.sub(1);
						
						if(fi.get() < 10)
						{
							for(Player i : onlinePlayers())
							{
								P.showProgress(i, C.GOLD + "" + SYM.SYMBOL_WARNING + C.RED + " Rebooting in " + C.GOLD + (fi.get() + 1) + "s;You will be sent to the hub.");
							}
						}
						
						else
						{
							for(Player i : onlinePlayers())
							{
								String v = TXT.getLine(C.GOLD, 44, (double) ((double) fi.get() / (double) mt), (fi.get() % 2 == 0 ? SYM.SYMBOL_WARNING + "" : SYM.SYMBOL_WARNING + ""), fi.get() + "s", (fi.get() % 2 == 0 ? SYM.SYMBOL_WARNING + "" : SYM.SYMBOL_WARNING + ""));
								
								NMSX.sendActionBar(i, v);
							}
						}
					}
				};
			}
		};
	}
	
	@Override
	public void onStart()
	{
		loadCluster(this);
	}
	
	@Override
	public void onStop()
	{
		
	}
}

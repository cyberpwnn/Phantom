package org.phantomapi.core;

import java.io.File;
import java.io.IOException;
import org.phantomapi.clust.Configurable;
import org.phantomapi.clust.DataCluster;
import org.phantomapi.clust.YAMLDataOutput;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Controller;
import org.phantomapi.construct.Ticked;
import org.phantomapi.lang.GMap;
import org.phantomapi.sync.TaskLater;
import org.phantomapi.util.A;
import org.phantomapi.util.F;
import org.phantomapi.util.S;

@Ticked(20)
public class ConfigurationBackupController extends Controller
{
	private GMap<File, DataCluster> queue;
	private static boolean running;
	
	public ConfigurationBackupController(Controllable parentController)
	{
		super(parentController);
		
		this.queue = new GMap<File, DataCluster>();
		
		running = false;
	}
	
	@Override
	public void onStart()
	{
		
	}
	
	@Override
	public void onStop()
	{
		
	}
	
	public void onTick()
	{
		new TaskLater(2)
		{
			@Override
			public void run()
			{
				if(!running && !queue.isEmpty())
				{
					running = true;
					GMap<File, DataCluster> asyncQueue = queue.copy();
					queue.clear();
					
					new A()
					{
						@Override
						public void async()
						{
							int processed = 0;
							int failed = 0;
							
							for(File i : asyncQueue.k())
							{
								if(asyncQueue.get(i).size() == 0)
								{
									failed++;
									w("Not backing up " + i.getPath() + " cluster is empty.");
									continue;
								}
								
								try
								{
									new YAMLDataOutput().save(asyncQueue.get(i), i);
									processed++;
								}
								
								catch(IOException e)
								{
									failed++;
								}
							}
							
							int p = processed;
							int f = failed;
							
							v("Backed up " + F.f(p) + " file(s) with " + F.f(f) + " failures.");
							
							new S()
							{
								@Override
								public void sync()
								{
									running = false;
								}
							};
						}
					};
				}
			}
		};
	}
	
	public void handle(Controller c, Configurable cc, String cat)
	{
		DataCluster dc = cc.getConfiguration().copy();
		File f = new File(new File(getPlugin().getDataFolder(), "backup"), c.getPlugin().getDataFolder().getName());
		
		if(cat != null)
		{
			f = new File(f, cat);
		}
		
		f = new File(f, cc.getCodeName() + ".yml");
		queue.put(f, dc);
	}
}

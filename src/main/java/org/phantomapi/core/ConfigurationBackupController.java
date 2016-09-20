package org.phantomapi.core;

import java.io.File;
import java.io.IOException;
import org.phantomapi.async.A;
import org.phantomapi.clust.Comment;
import org.phantomapi.clust.Configurable;
import org.phantomapi.clust.ConfigurableController;
import org.phantomapi.clust.DataCluster;
import org.phantomapi.clust.Keyed;
import org.phantomapi.clust.YAMLDataOutput;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Controller;
import org.phantomapi.construct.Ticked;
import org.phantomapi.lang.GList;
import org.phantomapi.lang.GMap;
import org.phantomapi.lang.GTime;
import org.phantomapi.sync.S;
import org.phantomapi.sync.TaskLater;
import org.phantomapi.util.F;
import org.phantomapi.util.M;

@Ticked(20)
public class ConfigurationBackupController extends ConfigurableController
{
	@Comment("Hours to pass before cleaning configs")
	@Keyed("housekeeping.time-before-delete")
	public double ttd = 72.0;
	
	private GMap<File, DataCluster> queue;
	private GList<File> scannable;
	private static boolean running;
	private int k;
	
	public ConfigurationBackupController(Controllable parentController)
	{
		super(parentController, "backup-manager");
		
		this.queue = new GMap<File, DataCluster>();
		this.scannable = new GList<File>();
		this.k = 60;
		
		running = false;
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
	
	public void onTick()
	{
		k++;
		
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
									File f = new File(i, "" + F.stamp() + ".yml");
									scannable.add(i);
									new YAMLDataOutput().save(asyncQueue.get(i), f);
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
				
				if(!running && queue.isEmpty() && !scannable.isEmpty() && k > 60)
				{
					k = 0;
					running = true;
					
					new A()
					{
						@Override
						public void async()
						{
							int del = 0;
							
							for(File i : scannable)
							{
								if(i.exists() && i.isDirectory())
								{
									for(File j : i.listFiles())
									{
										if(new GTime(M.ms() - j.lastModified()).getHours() > ttd && i.listFiles().length > 1)
										{
											if(j.delete())
											{
												del++;
											}
										}
									}
								}
							}
							
							if(del > 0)
							{
								w("Cleaned " + del + " old backups.");
							}
							
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
		
		File ff = f;
		
		new TaskLater(45)
		{
			@Override
			public void run()
			{
				queue.put(ff, dc);
			}
		};
	}
}

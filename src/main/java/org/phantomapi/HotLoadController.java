package org.phantomapi;

import java.io.File;
import java.io.IOException;
import org.phantomapi.clust.ColdLoad;
import org.phantomapi.clust.Comment;
import org.phantomapi.clust.Configurable;
import org.phantomapi.clust.ConfigurationHandler;
import org.phantomapi.clust.DataCluster;
import org.phantomapi.clust.Keyed;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Controller;
import org.phantomapi.construct.Ticked;
import org.phantomapi.lang.GMap;
import org.phantomapi.sync.ExecutiveIterator;
import org.phantomapi.sync.Task;
import org.phantomapi.sync.TaskLater;

@Ticked(20)
public class HotLoadController extends Controller implements Configurable
{
	private DataCluster cc;
	
	@Comment("Enable Hotloading")
	@Keyed("hotload.enable")
	public Boolean enable = true;
	
	private Task t;
	private GMap<File, Configurable> fileRegistry;
	private GMap<File, Long> mods;
	private GMap<File, Long> sizes;
	
	public HotLoadController(Controllable parentController)
	{
		super(parentController);
		
		this.cc = new DataCluster();
		this.fileRegistry = new GMap<File, Configurable>();
		this.mods = new GMap<File, Long>();
		this.sizes = new GMap<File, Long>();
		this.t = null;
	}
	
	public void onStart()
	{
		new TaskLater(4)
		{
			@Override
			public void run()
			{
				s("Loaded " + fileRegistry.size() + " HotLoadable Files");
			}
		};
	}
	
	public void onTick()
	{
		if(t == null || !t.isRunning())
		{
			ExecutiveIterator<File> it = new ExecutiveIterator<File>(fileRegistry.k())
			{
				@Override
				public void onIterate(File next)
				{
					handle(next);
				}
			};
			
			t = new Task(0)
			{
				@Override
				public void run()
				{
					if(it.hasNext())
					{
						it.next();
					}
					
					else
					{
						cancel();
					}
				}
			};
		}
	}
	
	@Override
	public void onNewConfig()
	{
		// Dynamic
	}
	
	@Override
	public void onReadConfig()
	{
		// Dynamic
	}
	
	@Override
	public DataCluster getConfiguration()
	{
		return cc;
	}
	
	@Override
	public String getCodeName()
	{
		return "hot-load";
	}
	
	public void overChanges(File f)
	{
		mods.put(f, f.lastModified());
		sizes.put(f, f.length());
	}
	
	public boolean modified(File f)
	{
		return mods.get(f) != f.lastModified() || sizes.get(f) != f.length();
	}
	
	public void handle(File f)
	{
		if(modified(f))
		{
			loadFile(f);
			overChanges(f);
		}
	}
	
	public void loadFile(File f)
	{
		Configurable c = fileRegistry.get(f);
		
		try
		{
			ConfigurationHandler.read(f.getParentFile(), c);
			s("HotLoaded Configuration: " + f.getPath() + " <> " + c.getCodeName());
		}
		
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void registerHotLoad(File file, Configurable c)
	{
		if(c.getClass().isAnnotationPresent(ColdLoad.class))
		{
			return;
		}
		
		fileRegistry.put(file, c);
		overChanges(file);
	}

	@Override
	public void onStop()
	{
		
	}
}

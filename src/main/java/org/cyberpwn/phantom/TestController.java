package org.cyberpwn.phantom;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.cyberpwn.phantom.clust.DataCluster;
import org.cyberpwn.phantom.clust.YAMLDataInput;
import org.cyberpwn.phantom.clust.YAMLDataOutput;
import org.cyberpwn.phantom.construct.Controllable;
import org.cyberpwn.phantom.construct.Controller;
import org.cyberpwn.phantom.lang.GList;
import org.cyberpwn.phantom.lang.GMap;
import org.cyberpwn.phantom.sync.ExecutiveIterator;
import org.cyberpwn.phantom.sync.ExecutiveRunnable;

/**
 * Runs tests on various functions of phantom
 * 
 * @author cyberpwn
 *
 */
public class TestController extends Controller
{
	private GMap<String, Runnable> tests;
	
	public TestController(Controllable parentController)
	{
		super(parentController);
		
		tests = new GMap<String, Runnable>();
		
		tests.put("channel-pool", new Runnable()
		{
			@Override
			public void run()
			{
				GList<String> k = new GList<String>();
				GList<String> v = new GList<String>();
				
				for(int i = 0; i < 10240008 * Math.random(); i++)
				{
					k.add(UUID.randomUUID().toString());
				}
				
				v.qadd("alpha").qadd("beta").qadd("charlie").qadd("delta");
				
				Phantom.schedule(new ExecutiveIterator<String>(k.copy(), new ExecutiveRunnable<String>()
				{
					public void run()
					{
						while(Math.random() < 0.6)
						{
							UUID.randomUUID();
							Math.sqrt(Math.sqrt(Math.random()));
						}
						
						if(Math.random() < 0.001)
						{
							Phantom.schedule(v.pickRandom(), new ExecutiveIterator<String>(k.copy(), new ExecutiveRunnable<String>()
							{
								public void run()
								{
									
								}
							}));
						}
					}
				}));
			}
		});
		
		tests.put("cluster-write", new Runnable()
		{
			@Override
			public void run()
			{
				DataCluster dc = new DataCluster();
				dc.set("test.string", "Stringd");
				dc.set("test.double", -0.65743345);
				dc.set("test.int", 234);
				dc.set("test.list", new GList<String>().qadd("test").qadd("list").qadd("test"));
				dc.set("test.boolean", true);
				
				try
				{
					new YAMLDataOutput().save(dc, new File(getPlugin().getDataFolder(), "cluster-test.yml"));
				}
				
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
		});
				
		tests.put("cluster-overwrite", new Runnable()
		{
			@Override
			public void run()
			{
				DataCluster dc = new DataCluster();
				dc.set("test.overwrite.data", "New Data");
				dc.set("test.string", "New String Text (Overwrite)");
				
				try
				{
					new YAMLDataOutput().save(dc, new File(getPlugin().getDataFolder(), "cluster-test.yml"));
				}
				
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
		});
				
		tests.put("cluster-read", new Runnable()
		{
			@Override
			public void run()
			{
				DataCluster dc = new DataCluster();
				
				try
				{
					new YAMLDataInput().load(dc, new File(getPlugin().getDataFolder(), "cluster-test.yml"));
					
					for(String i : dc.getData().keySet())
					{
						System.out.println(i + ": " + dc.getAbstract(i));
					}
				}
				
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
		});
	}
	
	public void execute(final CommandSender sender, String test)
	{
		for(String i : tests.k())
		{
			if(i.toLowerCase().contains(test.toLowerCase()))
			{
				sender.sendMessage(ChatColor.GREEN + "Running Test " + i);
				tests.get(i).run();
				break;
			}
		}
	}
	
	public GMap<String, Runnable> getTests()
	{
		return tests;
	}
}

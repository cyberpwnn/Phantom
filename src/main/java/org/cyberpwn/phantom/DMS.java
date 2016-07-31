package org.cyberpwn.phantom;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.cyberpwn.phantom.async.AsyncTask;
import org.cyberpwn.phantom.async.Callback;
import org.cyberpwn.phantom.construct.Controllable;
import org.cyberpwn.phantom.construct.Controller;
import org.cyberpwn.phantom.sync.TaskLater;
import org.cyberpwn.phantom.util.C;
import org.cyberpwn.phantom.util.F;
import org.cyberpwn.phantom.util.ServerState;

/**
 * Startup environment tests
 * @author cyberpwn
 *
 */
public class DMS extends Controller
{
	private String address;
	private Boolean hasInternet;
	private static ServerState state;
	
	public DMS(Controllable parentController)
	{
		super(parentController);
		
		address = null;
		hasInternet = null;
		
		if(Bukkit.getOnlinePlayers().isEmpty())
		{
			state = ServerState.START;
		}
		
		else
		{
			state = ServerState.ENABLE;
		}
	}
	
	@EventHandler
	public void on(PlayerCommandPreprocessEvent e)
	{
		if(e.getMessage().equalsIgnoreCase("/stop"))
		{
			if(e.getPlayer().hasPermission("bukkit.command.stop"))
			{
				state = ServerState.STOP;
			}
		}
	}
	
	@EventHandler
	public void on(ServerCommandEvent e)
	{
		if(e.getCommand().equalsIgnoreCase("stop"))
		{
			state = ServerState.STOP;
		}
	}
	
	public void onStop()
	{
		if(state.equals(ServerState.RUNNING))
		{
			state = ServerState.DISABLE;
		}
	}
	
	public void onStart()
	{
		new TaskLater(5)
		{
			public void run()
			{
				s("-------- Environment --------");
				testInternetConnection();
				//TODO ASYNC testMySqlConnection();
				showDiskSpace();
			}
		};
		
		new TaskLater(0)
		{
			public void run()
			{
				state = ServerState.RUNNING;
			}
		};
	}
	
	public void showDiskSpace()
	{
		s(C.YELLOW + "! " + C.AQUA + "Max  Memory: " + C.GREEN + F.memSize(Runtime.getRuntime().maxMemory()));
		s(C.YELLOW + "! " + C.AQUA + "Logic Cores: " + C.GREEN + Runtime.getRuntime().availableProcessors());
		s(C.YELLOW + "! " + C.AQUA + "At Location: " + C.GREEN + getPlugin().getDataFolder().getParentFile().getAbsolutePath());
		s(C.YELLOW + "! " + C.AQUA + "Free  Space: " + C.GREEN + F.fileSize(new File("/").getUsableSpace()));
		s(C.YELLOW + "! " + C.AQUA + "Used  Space: " + C.GREEN + F.fileSize(new File("/").getTotalSpace() - new File("/").getUsableSpace()));
		s(C.YELLOW + "! " + C.AQUA + "Total Space: " + C.GREEN + F.fileSize(new File("/").getTotalSpace()));
	}
	
	public void testMySqlConnection()
	{
		w("> " + C.AQUA + "Testing MySQL Connection...");
		
		if(((Phantom)getPlugin()).getMySQLConnectionController().testConnection())
		{
			s("> " + C.AQUA + "Connected: " + C.GREEN + "MySQL");
		}
		
		else
		{
			f("> " + C.YELLOW + "NETWORK FAILURE. NOT CONNECTED");
		}
	}
	
	public void testInternetConnection()
	{
		w("> " + C.AQUA + "Testing Internet Connection...");
		new AsyncTask<String>(new Callback<String>()
		{
			public void run()
			{
				DMS.this.address = get();
				
				if(get() == null)
				{
					f("> " + C.YELLOW + "NETWORK FAILURE. NOT CONNECTED");
					hasInternet = false;
				}
				
				else
				{
					s("> " + C.AQUA + "Connected <> Address: " + C.GREEN + get());
					hasInternet = true;
				}
				
				Phantom.instance().setEnvironmentData(getPlugin(), "status-network-failure", !hasInternet);
			}
		})
		{
			public String execute()
			{
				try
				{
					BufferedReader pr = new BufferedReader(new InputStreamReader(new URL("http://checkip.amazonaws.com/").openStream()));
					String address = pr.readLine();
					
					pr.close();
					
					return address;
				}
				
				catch(Exception e)
				{
					return null;
				}
			}
		}.start();
	}
	
	public String getAddress()
	{
		return address;
	}
	
	public void setAddress(String address)
	{
		this.address = address;
	}
	
	public Boolean getHasInternet()
	{
		return hasInternet;
	}
	
	public void setHasInternet(Boolean hasInternet)
	{
		this.hasInternet = hasInternet;
	}
	
	public static ServerState getState()
	{
		return state;
	}
}
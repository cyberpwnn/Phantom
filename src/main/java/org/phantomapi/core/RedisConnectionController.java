package org.phantomapi.core;

import org.phantomapi.clust.ConfigurableController;
import org.phantomapi.clust.Keyed;
import org.phantomapi.construct.Controllable;
import redis.clients.jedis.Jedis;

public class RedisConnectionController extends ConfigurableController
{
	@Keyed("redis.address")
	public String address = "???";
	
	@Keyed("redis.port")
	public int port = 6379;
	
	@Keyed("redis.password")
	public String password = "redis is bae";
	
	private Jedis r;
	
	public RedisConnectionController(Controllable parentController)
	{
		super(parentController, "redis");
	}
	
	public Jedis createSplitConnection()
	{
		Jedis r = new Jedis(address, port);
		r.auth(password);
		return r;
	}
	
	public void connect()
	{
		if(isConnected())
		{
			s("Reconnecting to redis");
			r.disconnect();
		}
		
		try
		{
			r = new Jedis(address, port);
			r.auth(password);
			s("Connected to redis");
		}
		
		catch(Exception e)
		{
			f("Failed to connect to redis.");
			f(e.getMessage());
		}
	}
	
	public boolean hasKey(String key)
	{
		return read(key) != null;
	}
	
	public void write(String key, String value)
	{
		try
		{
			r.set(key, value);
		}
		
		catch(Exception e)
		{
			f("FAILED TO WRITE KEY: " + key + " > " + value);
		}
	}
	
	public String read(String key)
	{
		try
		{
			f("FAILED TO RETREIVE KEY: " + key);
			return r.get(key);
		}
		
		catch(Exception e)
		{
			return null;
		}
	}
	
	public boolean isConnected()
	{
		return r != null && r.isConnected();
	}
	
	@Override
	public void onStart()
	{
		loadCluster(this);
	}
	
	@Override
	public void onStop()
	{
		if(isConnected())
		{
			s("Disconnecting");
			r.disconnect();
		}
	}
	
	@Override
	public void onReadConfig()
	{
		connect();
	}
}

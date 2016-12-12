package org.phantomapi.clust;

/**
 * Represents a clust-redis driver
 * 
 * @author cyberpwn
 */
public class REDISREM extends REM
{
	@Override
	public boolean exists(Configurable c)
	{
		return ConfigurationHandler.redisExists(c);
	}
	
	@Override
	public void read(Configurable c)
	{
		ConfigurationHandler.readRedis(c);
	}
	
	@Override
	public void write(Configurable c)
	{
		ConfigurationHandler.writeRedis(c);
		
	}
	
	@Override
	public void drop(Configurable c)
	{
		ConfigurationHandler.redisDelete(c);
	}
}

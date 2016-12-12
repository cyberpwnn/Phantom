package org.phantomapi.clust;

import java.sql.SQLException;

/**
 * Represents a clust-sql driver
 * 
 * @author cyberpwn
 */
public class SQLREM extends REM
{
	@Override
	public boolean exists(Configurable c) throws ClassNotFoundException, SQLException
	{
		return ConfigurationHandler.rowExists(c, sql);
	}
	
	@Override
	public void read(Configurable c) throws ClassNotFoundException, SQLException
	{
		ConfigurationHandler.fromMysql(c, sql);
	}
	
	@Override
	public void write(Configurable c) throws ClassNotFoundException, SQLException
	{
		ConfigurationHandler.toMysql(c, sql);
		
	}
	
	@Override
	public void drop(Configurable c) throws ClassNotFoundException, SQLException
	{
		ConfigurationHandler.dropRow(c, sql);
	}
}

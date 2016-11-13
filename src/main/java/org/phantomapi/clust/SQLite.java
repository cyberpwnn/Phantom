package org.phantomapi.clust;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Connects to and uses a SQLite database
 *
 * @author tips48
 * @author cyberpwn
 */
public class SQLite extends Database
{
	private final File dbLocation;
	
	/**
	 * Creates a new SQLite instance
	 *
	 * @param dbLocation
	 *            Location of the Database (Must end in .db)
	 */
	public SQLite(File dbLocation)
	{
		this.dbLocation = dbLocation;
	}
	
	@Override
	public Connection openConnection() throws SQLException, ClassNotFoundException
	{
		if(checkConnection())
		{
			return connection;
		}
		
		if(!dbLocation.exists())
		{
			try
			{
				dbLocation.getParentFile().mkdirs();
				dbLocation.createNewFile();
			}
			
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
		
		Class.forName("org.sqlite.JDBC");
		connection = DriverManager.getConnection("jdbc:sqlite:" + dbLocation + "/" + dbLocation);
		
		return connection;
	}
}
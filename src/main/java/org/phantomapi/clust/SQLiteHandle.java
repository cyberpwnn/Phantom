package org.phantomapi.clust;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

public class SQLiteHandle
{
	private SQLite db;
	private File file;
	private Connection connection;
	
	public SQLiteHandle(File file)
	{
		this.file = file;
		db = new SQLite(file);
		connection = null;
	}
	
	public Connection getConnection() throws ClassNotFoundException, SQLException
	{
		if(connection == null || connection.isClosed())
		{
			connection = db.openConnection();
		}
		
		return connection;
	}
	
	public void close() throws SQLException
	{
		if(connection != null && !connection.isClosed())
		{
			connection.close();
		}
	}
	
	public SQLite getDb()
	{
		return db;
	}
	
	public File getFile()
	{
		return file;
	}
	
	public boolean isClosed()
	{
		if(connection != null)
		{
			try
			{
				return connection.isClosed();
			}
			
			catch(SQLException e)
			{
				
			}
		}
		
		return true;
	}
}

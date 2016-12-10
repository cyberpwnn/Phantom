package org.phantomapi.clust;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.phantomapi.Phantom;

/**
 * Connects to and uses a MySQL database
 * 
 * @author -_Husky_-
 * @author tips48
 * @author cyberpwn
 */
public class MySQL extends Database
{
	private final String user;
	private final String database;
	private final String password;
	private final String port;
	private final String hostname;
	
	/**
	 * Creates a new MySQL instance
	 *
	 * @param hostname
	 *            Name of the host
	 * @param port
	 *            Port number
	 * @param username
	 *            Username
	 * @param password
	 *            Password
	 */
	public MySQL(String hostname, String port, String username, String password)
	{
		this(hostname, port, null, username, password);
	}
	
	/**
	 * Creates a new MySQL instance for a specific database
	 *
	 * @param hostname
	 *            Name of the host
	 * @param port
	 *            Port number
	 * @param database
	 *            Database name
	 * @param username
	 *            Username
	 * @param password
	 *            Password
	 */
	public MySQL(String hostname, String port, String database, String username, String password)
	{
		this.hostname = hostname;
		this.port = port;
		this.database = database;
		user = username;
		this.password = password;
	}
	
	@Override
	public Connection openConnection() throws SQLException, ClassNotFoundException
	{
		if(checkConnection())
		{
			return connection;
		}
		
		String connectionURL = "jdbc:mysql://" + hostname + ":" + port;
		if(database != null)
		{
			connectionURL = connectionURL + "/" + database;
		}
		
		Class.forName("com.mysql.jdbc.Driver");
		connection = DriverManager.getConnection(connectionURL, user, password);
		return connection;
	}
	
	public static MySQL get()
	{
		return Phantom.instance().getMySQLConnectionController().getSql();
	}
}
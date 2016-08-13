package org.phantomapi.clust;

/**
 * A simple databse connection pojo holder
 * 
 * @author cyberpwn
 *
 */
public class DatabaseConnection
{
	private String address;
	private int port;
	private String database;
	private String username;
	private String password;
	
	/**
	 * Reference a connection
	 * 
	 * @param address
	 *            the address
	 * @param username
	 *            the user
	 * @param password
	 *            the pass
	 * @param database
	 *            the database
	 */
	public DatabaseConnection(String address, String database, String username, String password)
	{
		this(address, database, 3306, username, password);
	}
	
	/**
	 * Reference a connection
	 * 
	 * @param port
	 *            the port
	 * @param username
	 *            the user
	 * @param password
	 *            the pass
	 * @param database
	 *            the database
	 */
	public DatabaseConnection(String database, int port, String username, String password)
	{
		this("localhost", database, port, username, password);
	}
	
	/**
	 * Reference a connection
	 * 
	 * @param username
	 *            the user
	 * @param password
	 *            the pass
	 * @param database
	 *            the database
	 */
	public DatabaseConnection(String database, String username, String password)
	{
		this("localhost", database, username, password);
	}
	
	/**
	 * Reference a connection
	 * 
	 * @param address
	 *            the address
	 * @param port
	 *            the port
	 * @param username
	 *            the username
	 * @param password
	 *            the pass
	 * @param database
	 *            the database
	 */
	public DatabaseConnection(String address, String database, int port, String username, String password)
	{
		this.address = address;
		this.port = port;
		this.username = username;
		this.password = password;
		this.database = database;
	}
	
	public String getAddress()
	{
		return address;
	}
	
	public void setAddress(String address)
	{
		this.address = address;
	}
	
	public int getPort()
	{
		return port;
	}
	
	public void setPort(int port)
	{
		this.port = port;
	}
	
	public String getUsername()
	{
		return username;
	}
	
	public void setUsername(String username)
	{
		this.username = username;
	}
	
	public String getPassword()
	{
		return password;
	}
	
	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getDatabase()
	{
		return database;
	}

	public void setDatabase(String database)
	{
		this.database = database;
	}
}

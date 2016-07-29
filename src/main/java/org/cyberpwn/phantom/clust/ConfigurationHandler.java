package org.cyberpwn.phantom.clust;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.cyberpwn.phantom.lang.GList;
import org.cyberpwn.phantom.util.D;

/**
 * The heart of dataclusters. Most should not have to use this. It is
 * essentially a utility class for controllers and any other components of
 * Phantom to use automatically when you need it called. For example,
 * loadCluster would call parts of this when needed.
 * 
 * @author cyberpwn
 *
 */
public class ConfigurationHandler
{
	/**
	 * Read data from config files and dataclusters and put them into the keyed
	 * config fields in the configurable class supplied
	 * 
	 * @param c
	 *            the configurable object
	 */
	public static void toFields(Configurable c)
	{
		for(Field i : c.getClass().getDeclaredFields())
		{
			if(i.isAnnotationPresent(Keyed.class))
			{
				if(isValidType(i.getType()))
				{
					if(Modifier.isPublic(i.getModifiers()) && !Modifier.isStatic(i.getModifiers()))
					{
						try
						{
							String key = i.getDeclaredAnnotation(Keyed.class).value();
							Object value = c.getConfiguration().getAbstract(key);
							
							if(value instanceof List)
							{
								List<?> l = (List<?>) value;
								GList<String> k = new GList<String>();
								
								for(Object j : l)
								{
									k.add(j.toString());
								}
								
								i.set(c, k);
							}
							
							else
							{
								i.set(c, value);
							}
						}
						
						catch(IllegalArgumentException e)
						{
							e.printStackTrace();
						}
						
						catch(IllegalAccessException e)
						{
							e.printStackTrace();
						}
					}
					
					else
					{
						new D(c.getCodeName() + "/" + i.getType().getSimpleName() + " " + i.getName()).w("INVALID MODIFIERS. MUST BE PUBLIC NON STATIC");
					}
				}
				
				else
				{
					new D(c.getCodeName() + "/" + i.getType().getSimpleName() + " " + i.getName()).w("INVALID TYPE. NOT SUPPORTED FOR KEYED CONFIGS");
					;
				}
			}
		}
	}
	
	/**
	 * Write data to the cluster from the keyed fields from the configurable
	 * object
	 * 
	 * @param c
	 *            the configurable object
	 */
	public static void fromFields(Configurable c)
	{
		for(Field i : c.getClass().getDeclaredFields())
		{
			if(i.isAnnotationPresent(Keyed.class))
			{
				if(isValidType(i.getType()))
				{
					if(Modifier.isPublic(i.getModifiers()) && !Modifier.isStatic(i.getModifiers()))
					{
						try
						{
							String key = i.getDeclaredAnnotation(Keyed.class).value();
							Object value = i.get(c);
							c.getConfiguration().trySet(key, value);
							
							if(i.isAnnotationPresent(Comment.class))
							{
								c.getConfiguration().comment(key, i.getDeclaredAnnotation(Comment.class).value());
							}
						}
						
						catch(IllegalArgumentException e)
						{
							e.printStackTrace();
						}
						
						catch(IllegalAccessException e)
						{
							e.printStackTrace();
						}
					}
					
					else
					{
						new D(c.getCodeName() + "/" + i.getType().getSimpleName() + " " + i.getName()).w("INVALID MODIFIERS. MUST BE PUBLIC NON STATIC");
					}
				}
				
				else
				{
					new D(c.getCodeName() + "/" + i.getType().getSimpleName() + " " + i.getName()).w("INVALID TYPE. NOT SUPPORTED FOR KEYED CONFIGS");
					;
				}
			}
		}
	}
	
	/**
	 * But is this type valid?
	 * 
	 * @param type
	 *            the class type
	 * @return true if can be saved
	 */
	public static boolean isValidType(Class<?> type)
	{
		if(type.equals(String.class))
		{
			return true;
		}
		
		else if(type.equals(Integer.class))
		{
			return true;
		}
		
		else if(type.equals(int.class))
		{
			return true;
		}
		
		else if(type.equals(Long.class))
		{
			return true;
		}
		
		else if(type.equals(long.class))
		{
			return true;
		}
		
		else if(type.equals(Double.class))
		{
			return true;
		}
		
		else if(type.equals(double.class))
		{
			return true;
		}
		
		else if(type.equals(GList.class))
		{
			return true;
		}
		
		else if(type.equals(Boolean.class))
		{
			return true;
		}
		
		else if(type.equals(boolean.class))
		{
			return true;
		}
		
		else
		{
			return false;
		}
	}
	
	/**
	 * Handle reading in configs. Also adds new paths that do not exist in the
	 * file from the onNewConfig(), and adds default values
	 * 
	 * @param base
	 *            the base directory
	 * @param c
	 *            the configurable object
	 * @throws IOException
	 *             1337
	 */
	public static void read(File base, Configurable c) throws IOException
	{
		File config = new File(base, c.getCodeName() + ".yml");
		
		if(!config.getParentFile().exists())
		{
			config.getParentFile().mkdirs();
		}
		
		if(!config.exists())
		{
			config.createNewFile();
		}
		
		if(config.isDirectory())
		{
			throw new IOException("Cannot read config (it's a folder)");
		}
		
		fromFields(c);
		c.onNewConfig();
		new YAMLDataInput().load(c.getConfiguration(), config);
		new YAMLDataOutput().save(c.getConfiguration(), config);
		toFields(c);
		c.onReadConfig();
	}
	
	/**
	 * Handle saving configs
	 * 
	 * @param base
	 *            the base directory
	 * @param c
	 *            the configurable object
	 * @throws IOException
	 *             1337
	 */
	public static void save(File base, Configurable c) throws IOException
	{
		File config = new File(base, c.getCodeName() + ".yml");
		
		if(!config.getParentFile().exists())
		{
			config.getParentFile().mkdirs();
		}
		
		if(!config.exists())
		{
			config.createNewFile();
		}
		
		if(config.isDirectory())
		{
			throw new IOException("Cannot save config (it's a folder)");
		}
		
		fromFields(c);
		new YAMLDataOutput().save(c.getConfiguration(), config);
	}
	
	/**
	 * Handle reading in configs. Also adds new paths that do not exist in the
	 * file from the onNewConfig(), and adds default values
	 * 
	 * @param c
	 *            the configurable object
	 * @throws IOException
	 *             1337
	 * @throws SQLException
	 *             shit happens
	 * @throws ClassNotFoundException
	 *             really bad shit happens
	 */
	public static void readMySQL(Configurable c, DatabaseConnection connection) throws IOException, ClassNotFoundException, SQLException
	{
		fromFields(c);
		c.onNewConfig();
		fromMysql(c, connection);
		toMysql(c, connection);
		toFields(c);
		c.onReadConfig();
	}
	
	/**
	 * Handle saving configs
	 * 
	 * @param c
	 *            the configurable object
	 * @throws IOException
	 *             1337
	 * @throws SQLException
	 *             shit happens
	 * @throws ClassNotFoundException
	 *             really bad shit happens
	 */
	public static void saveMySQL(Configurable c, DatabaseConnection connection) throws IOException, ClassNotFoundException, SQLException
	{
		fromFields(c);
		toMysql(c, connection);
	}
	
	/**
	 * Checks if the configurable object has a table annotation for SQL purposes
	 * 
	 * @param c
	 *            the configurable object
	 * @return true if a table is defined
	 */
	public static boolean hasTable(Configurable c)
	{
		return c.getClass().isAnnotationPresent(Tabled.class);
	}
	
	/**
	 * Get the table defined in this tabled configurable object
	 * 
	 * @param c
	 *            the tabled configurable object
	 * @return the table
	 */
	public static String getTable(Configurable c)
	{
		return c.getClass().getAnnotation(Tabled.class).value();
	}
	
	public static String grave(String s)
	{
		return "`" + s + "`";
	}
	
	/**
	 * Pull data from mysql into the configurable object
	 * 
	 * @param c
	 *            the configurable object
	 * @param connection
	 *            the connection data
	 * @throws SQLException
	 *             shit happens
	 * @throws ClassNotFoundException
	 *             really bad shit happens
	 */
	public static void fromMysql(Configurable c, DatabaseConnection connection) throws SQLException, ClassNotFoundException
	{
		MySQL db = new MySQL(connection.getAddress(), String.valueOf(connection.getPort()), connection.getDatabase(), connection.getUsername(), connection.getPassword());
		Connection conn = db.openConnection();
		PreparedStatement s = conn.prepareStatement("CREATE TABLE IF NOT EXISTS " + getTable(c) + " (`k` TEXT, `d` TEXT);");
		s.execute();
		s.close();
		
		PreparedStatement st = conn.prepareStatement("SELECT * FROM " + getTable(c) + " WHERE k=?;");
		st.setString(1, c.getCodeName());
		ResultSet res = st.executeQuery();
		
		if(res.next())
		{
			PreparedStatement stx = conn.prepareStatement("SELECT `d` FROM " + getTable(c) + " WHERE k=?;");
			stx.setString(1, c.getCodeName());
			ResultSet resx = stx.executeQuery();
			resx.next();
			JSONObject jso = new JSONObject(resx.getString("d"));
			c.getConfiguration().addJson(jso);
			resx.close();
			stx.close();
		}
		
		res.close();
		st.close();
		db.closeConnection();
	}
	
	/**
	 * Put data from configurable objects into mysql
	 * 
	 * @param c
	 *            the configurable object
	 * @param connection
	 *            the connection data
	 * @throws SQLException
	 *             shit happens
	 * @throws ClassNotFoundException
	 *             really bad shit happens
	 */
	public static void toMysql(Configurable c, DatabaseConnection connection) throws SQLException, ClassNotFoundException
	{
		MySQL db = new MySQL(connection.getAddress(), String.valueOf(connection.getPort()), connection.getDatabase(), connection.getUsername(), connection.getPassword());
		Connection conn = db.openConnection();
		PreparedStatement s = conn.prepareStatement("CREATE TABLE IF NOT EXISTS " + getTable(c) + " (`k` TEXT, `d` TEXT);");
		s.execute();
		s.close();
		
		PreparedStatement st = conn.prepareStatement("SELECT * FROM " + getTable(c) + " WHERE k=?;");
		st.setString(1, c.getCodeName());
		ResultSet res = st.executeQuery();
		
		if(res.next())
		{
			PreparedStatement stx = conn.prepareStatement("UPDATE " + getTable(c) + " SET d=? WHERE k=?;");
			stx.setString(1, c.getConfiguration().toJSON().toString());
			stx.setString(2, c.getCodeName());
			stx.executeUpdate();
			stx.close();
		}
		
		else
		{
			PreparedStatement stx = conn.prepareStatement("INSERT INTO " + getTable(c) + " values(?,?);");
			stx.setString(1, c.getCodeName());
			stx.setString(2, c.getConfiguration().toJSON().toString());
			stx.executeUpdate();
			stx.close();
		}
		
		res.close();
		st.close();
		db.closeConnection();
	}
}
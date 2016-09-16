package org.phantomapi.core;

import java.sql.SQLException;
import org.phantomapi.Phantom;
import org.phantomapi.clust.Comment;
import org.phantomapi.clust.Configurable;
import org.phantomapi.clust.ConfigurationHandler;
import org.phantomapi.clust.DataCluster;
import org.phantomapi.clust.Keyed;
import org.phantomapi.clust.MySQL;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Controller;
import org.phantomapi.construct.Ticked;
import org.phantomapi.lang.GList;
import org.phantomapi.lang.GTriset;
import org.phantomapi.statistics.Monitorable;
import org.phantomapi.util.C;
import org.phantomapi.util.F;
import org.phantomapi.util.SQLOperation;

@Ticked(5)
public class MySQLConnectionController extends Controller implements Configurable, Monitorable
{
	private DataCluster cc;
	private GList<GTriset<SQLOperation, Configurable, Runnable>> queue;
	private MySQL sql;
	private int qq;
	
	@Comment("Database Address to the database server. Localhost is the local machine (this one)\nThis will be used for all phantom plugins that use databases")
	@Keyed("database.address")
	public String address = "localhost";
	
	@Comment("Database Name. This is the specific database to cram tables into.\nThis will be used for all phantom plugins that use databases")
	@Keyed("database.database")
	public String database = "database";
	
	@Comment("The username for the database\nThis will be used for all phantom plugins that use databases")
	@Keyed("database.username")
	public String username = "username";
	
	@Comment("The password for the database\nThis will be used for all phantom plugins that use databases")
	@Keyed("database.password")
	public String password = "password";
	
	@Comment("The port used for this database server. Typically 3306\nThis will be used for all phantom plugins that use databases")
	@Keyed("database.port")
	public int port = 3306;
	
	public MySQLConnectionController(Controllable parentController)
	{
		super(parentController);
		
		this.cc = new DataCluster();
		this.queue = new GList<GTriset<SQLOperation, Configurable, Runnable>>();
		this.sql = null;
		this.qq = 0;
	}
	
	public boolean testConnection()
	{
		try
		{
			sql.openConnection();
		}
		
		catch(ClassNotFoundException | SQLException e)
		{
			Phantom.instance().setEnvironmentData(getPlugin(), "status-database-failure", true);
			
			try
			{
				sql.closeConnection();
			}
			
			catch(SQLException e1)
			{
				
			}
			
			return false;
		}
		
		Phantom.instance().setEnvironmentData(getPlugin(), "status-database-failure", false);
		
		try
		{
			sql.closeConnection();
		}
		
		catch(SQLException e)
		{
			
		}
		
		return true;
	}
	
	public void onTick()
	{
		qq = 0;
	}
	
	public void onStop()
	{
		try
		{
			flush();
			sql.closeConnection();
		}
		
		catch(ClassNotFoundException | SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public void queue(SQLOperation o, Configurable c, Runnable finish)
	{
		queue.add(new GTriset<SQLOperation, Configurable, Runnable>(o, c, finish));
		qq++;
		
		try
		{
			flush();
		}
		
		catch(ClassNotFoundException | SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public void flush() throws ClassNotFoundException, SQLException
	{
		if(queue.isEmpty())
		{
			return;
		}
				
		for(GTriset<SQLOperation, Configurable, Runnable> i : queue)
		{
			execute(i.getA(), i.getB(), i.getC());
		}
				
		queue.clear();
	}
	
	public void execute(SQLOperation o, Configurable c, Runnable r) throws ClassNotFoundException, SQLException
	{
		if(!sql.checkConnection())
		{
			sql.openConnection();
		}
		
		if(o.equals(SQLOperation.LOAD))
		{
			ConfigurationHandler.fromMysql(c, sql);
		}
		
		else if(o.equals(SQLOperation.SAVE))
		{
			ConfigurationHandler.toMysql(c, sql);
		}
		
		r.run();
	}
	
	public void onStart()
	{
		loadCluster(this);
		sql = new MySQL(address, String.valueOf(port), database, username, password);
	}
	
	@Override
	public void onNewConfig()
	{
		
	}
	
	@Override
	public void onReadConfig()
	{
		
	}
	
	@Override
	public DataCluster getConfiguration()
	{
		return cc;
	}
	
	@Override
	public String getCodeName()
	{
		return "mysql";
	}

	@Override
	public String getMonitorableData()
	{
		return "Queued: " + C.LIGHT_PURPLE + F.f(qq);
	}
}

package org.cyberpwn.phantom;

import java.sql.SQLException;

import org.cyberpwn.phantom.clust.Comment;
import org.cyberpwn.phantom.clust.Configurable;
import org.cyberpwn.phantom.clust.ConfigurationHandler;
import org.cyberpwn.phantom.clust.DataCluster;
import org.cyberpwn.phantom.clust.Keyed;
import org.cyberpwn.phantom.clust.MySQL;
import org.cyberpwn.phantom.construct.Controllable;
import org.cyberpwn.phantom.construct.Controller;
import org.cyberpwn.phantom.construct.Ticked;
import org.cyberpwn.phantom.lang.GList;
import org.cyberpwn.phantom.lang.GTriset;
import org.cyberpwn.phantom.sync.ExecutiveRunnable;
import org.cyberpwn.phantom.util.C;
import org.cyberpwn.phantom.util.SQLOperation;

@Ticked(0)
public class MySQLConnectionController extends Controller implements Configurable
{
	private DataCluster cc;
	private GList<GTriset<SQLOperation, Configurable, Runnable>> queue;
	private MySQL sql;
	
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
		try
		{
			flush();
		}
		
		catch(ClassNotFoundException | SQLException e)
		{
			e.printStackTrace();
		}
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
	}
	
	public void flush() throws ClassNotFoundException, SQLException
	{
		if(queue.isEmpty())
		{
			return;
		}
		
		int s = queue.size();
		
		try
		{
			Phantom.schedule("mysql", queue.copy().iterator(new ExecutiveRunnable<GTriset<SQLOperation, Configurable, Runnable>>()
			{
				public void run()
				{
					try
					{
						execute(next().getA(), next().getB(), next().getC());
					}
					
					catch(ClassNotFoundException | SQLException e)
					{
						e.printStackTrace();
					}
				}
			}));
		}
		
		catch(Exception e)
		{
			for(GTriset<SQLOperation, Configurable, Runnable> i : queue)
			{
				execute(i.getA(), i.getB(), i.getC());
			}
			
			f("Using Shutdown flush method.");
		}
			
		w("Batched " + C.GREEN + s + C.YELLOW + " SQL Operations");
		
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
	
}

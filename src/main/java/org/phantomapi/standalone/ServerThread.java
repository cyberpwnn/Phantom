package org.phantomapi.standalone;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import org.phantomapi.clust.Configurable;
import org.phantomapi.clust.ConfigurationHandler;
import org.phantomapi.clust.DataCluster;
import org.phantomapi.clust.JSONObject;
import org.phantomapi.clust.Keyed;
import org.phantomapi.lang.GMap;
import jline.console.ConsoleReader;

public class ServerThread extends Thread implements Configurable
{
	@Keyed("bind")
	public int port = 34435;
	
	private ServerSocket s;
	private DataCluster cc;
	private ConsoleReader c;
	private GMap<String, ServerInstanceThread> connections;
	
	public ServerThread(ConsoleReader r) throws IOException
	{
		cc = new DataCluster();
		c = r;
		s = null;
		connections = new GMap<String, ServerInstanceThread>();
	}
	
	@Override
	public void run()
	{
		try
		{
			c.println("[server]: Reading Configuration...");
			File f = new File("server.yml");
			ConfigurationHandler.read(f, this);
			c.println("[server]: Binding to *:" + port);
			s = new ServerSocket(port);
			s.setSoTimeout(50);
			
			while(!s.isClosed() && !Thread.interrupted())
			{
				Socket server = s.accept();
				DataInputStream i = new DataInputStream(server.getInputStream());
				String data = i.readUTF();
				DataCluster auth = new DataCluster(new JSONObject(data));
				String name = auth.getString("n");
				ServerInstanceThread sit = new ServerInstanceThread(name, c, server);
				sit.start();
				connections.put(name, sit);
				c.println("[server]: Accepted downstream connection from " + name + " @ " + server.getInetAddress().getHostAddress() + ":" + server.getPort());
			}
		}
		
		catch(IOException e)
		{
			e.printStackTrace();
		}
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
		return "server";
	}
}

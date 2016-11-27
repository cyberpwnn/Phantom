package org.phantomapi.standalone;

import java.net.Socket;
import jline.console.ConsoleReader;

public class ServerInstanceThread extends Thread
{
	private ConsoleReader r;
	private Socket s;
	private String n;
	
	public ServerInstanceThread(String n, ConsoleReader r, Socket s)
	{
		this.n = n;
		this.r = r;
		this.s = s;
	}
	
	@Override
	public void run()
	{
		
	}
	
	public ConsoleReader getR()
	{
		return r;
	}
	
	public Socket getS()
	{
		return s;
	}
	
	public String getServerName()
	{
		return n;
	}
}

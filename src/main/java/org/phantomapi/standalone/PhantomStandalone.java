package org.phantomapi.standalone;

import java.io.IOException;
import org.phantomapi.lang.GList;
import jline.TerminalFactory;
import jline.console.ConsoleReader;

public class PhantomStandalone
{
	public static void main(String[] args) throws IOException
	{
		try
		{
			ConsoleReader console = new ConsoleReader();
			console.setPrompt("> ");
			console.setHistoryEnabled(true);
			
			String line = null;
			
			startup(console);
			
			while((line = console.readLine()) != null)
			{
				GList<String> arg = new GList<String>();
				String v = line.trim();
				
				for(String i : v.split(" "))
				{
					if(!i.isEmpty())
					{
						arg.add(i.trim());
					}
				}
				
				if(arg.isEmpty())
				{
					continue;
				}
				
				try
				{
					if(!handleCommand(arg.toArray(new String[arg.size()]), console))
					{
						console.println("! Unknown Command. Use help or ? for help.");
					}
				}
				
				catch(Exception e)
				{
					System.out.println("! Exception parsing command.");
					e.printStackTrace();
				}
			}
		}
		
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		finally
		{
			try
			{
				TerminalFactory.get().restore();
			}
			
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public static void startup(ConsoleReader c)
	{
		try
		{
			c.println("Starting Up");
			
		}
		
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static boolean handleCommand(String[] args, ConsoleReader console) throws IOException
	{
		String line = args[0];
		
		if(line.equalsIgnoreCase("exit") || line.equals("stop"))
		{
			console.println("! Shutting Down...");
			shutdown();
		}
		
		else if(line.equalsIgnoreCase("help") || line.equals("?"))
		{
			console.println("! Help command listing");
			console.println("- exit,stop - Shut down the server");
		}
		
		else
		{
			return false;
		}
		
		return true;
	}
	
	public static void shutdown()
	{
		System.exit(0);
	}
}

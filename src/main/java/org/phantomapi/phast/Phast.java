package org.phantomapi.phast;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import javax.script.ScriptException;
import org.bukkit.Bukkit;
import org.phantomapi.Phantom;
import org.phantomapi.command.PhantomCommandSender;
import org.phantomapi.lang.GList;
import org.phantomapi.sync.TaskLater;
import org.phantomapi.util.U;

/**
 * Phast evaluator
 * 
 * @author cyberpwn
 */
public class Phast
{
	/**
	 * Evaluate the phast script
	 * 
	 * @param expression
	 *            the expression
	 * @throws ScriptException
	 *             shit happens
	 */
	public static void evaluate(String expression, PhantomCommandSender sender) throws ScriptException
	{
		GList<String> inv = new GList<String>();
		
		if(!expression.endsWith(";"))
		{
			throw new ScriptException("Must end with a semicolon");
		}
		
		expression = expression.substring(0, expression.length() - 1);
		
		for(String i : expression.split(";"))
		{
			if(i.startsWith("#"))
			{
				continue;
			}
			
			String in = i;
			
			while(in.endsWith(" "))
			{
				in = in.substring(0, in.length() - 1);
			}
			
			while(in.startsWith(" "))
			{
				in = in.substring(1);
			}
			
			inv.add(in);
		}
		
		GList<String> km = inv.copy();
		
		execute(km, new int[] {0});
	}
	
	private static void execute(GList<String> im, int[] d)
	{
		if(im.isEmpty())
		{
			return;
		}
		
		new TaskLater(d[0])
		{
			@Override
			public void run()
			{
				d[0] = 0;
				String i = im.pop();
				String name = i;
				GList<String> args = new GList<String>();
				
				if(i.contains(" "))
				{
					GList<String> seg = new GList<String>(i.split(" "));
					name = seg.pop();
					args = seg.copy();
				}
				
				try
				{
					try
					{
						if(name.equalsIgnoreCase("wait") && args.size() == 1)
						{
							Integer wa = Integer.valueOf(args.get(0));
							d[0] = wa;
						}
						
						if(name.equalsIgnoreCase("reload") || name.equalsIgnoreCase("thrash"))
						{
							File f = new File(new File(Phantom.instance().getDataFolder(), "scripts"), "resume.txt");
							
							if(f.exists())
							{
								f.delete();
							}
							
							f.createNewFile();
							PrintWriter bw = new PrintWriter(new FileWriter(f, false));
							
							for(String j : im)
							{
								bw.println(j + ";");
							}
							
							bw.close();
							
							File s = new File(Phantom.instance().getDataFolder(), "initial.txt");
							PrintWriter bwf = new PrintWriter(new FileWriter(s, true));
							bwf.println("resume.txt");
							bwf.close();
						}
						
						if(name.equalsIgnoreCase("reset"))
						{
							Bukkit.reload();
							return;
						}
						
						if(name.equalsIgnoreCase("thrash"))
						{
							U.thrash();
							return;
						}
					}
					
					catch(Exception ex)
					{
						ex.printStackTrace();
					}
					
					Phantom.instance().getPhastController().handle(name, args.toArray(new String[args.size()]));
				}
				
				catch(Exception e)
				{
					e.printStackTrace();
				}
				
				execute(im, d);
			}
		};
	}
}

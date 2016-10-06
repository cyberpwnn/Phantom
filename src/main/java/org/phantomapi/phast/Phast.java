package org.phantomapi.phast;

import javax.script.ScriptException;
import org.phantomapi.Phantom;
import org.phantomapi.command.PhantomCommandSender;
import org.phantomapi.lang.GList;
import org.phantomapi.nms.NMSX;
import org.phantomapi.sync.TaskLater;
import org.phantomapi.util.C;
import org.phantomapi.util.F;
import org.phantomapi.util.P;

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
		status(sender, "Reading Script");
		GList<String> inv = new GList<String>();
		
		if(!expression.endsWith(";"))
		{
			throw new ScriptException("Must end with a semicolon");
		}
		
		expression = expression.substring(0, expression.length() - 1);
		status(sender, "Processing");
		
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
		
		status(sender, "Executing");
		
		int[] k = new int[] {0};
		int v = 0;
		
		new TaskLater((inv.size() * 5) + 20)
		{
			@Override
			public void run()
			{
				status(sender, null);
			}
		};
		
		for(String i : inv)
		{
			new TaskLater(v * 5)
			{
				@Override
				public void run()
				{
					String name = i;
					GList<String> args = new GList<String>();
					
					if(i.contains(" "))
					{
						GList<String> seg = new GList<String>(i.split(" "));
						name = seg.pop();
						args = seg.copy();
					}
					
					status(sender, "Executing " + F.pc((double) k[0] / (double) inv.size()) + ";Invoking " + i);
					
					try
					{
						Phantom.instance().getPhastController().handle(name, args.toArray(new String[args.size()]));
					}
					
					catch(Exception e)
					{
						status(sender, "Executing " + F.pc((double) k[0] / (double) inv.size()) + ";" + C.RED + "\\FAILED");
					}
					
					k[0]++;
				}
			};
			
			v++;
		}
	}
	
	private static void status(PhantomCommandSender sender, String message)
	{
		if(message == null)
		{
			if(sender.isPlayer())
			{
				P.clearProgress(sender.getPlayer());
			}
			
			return;
		}
		
		if(sender.isPlayer())
		{
			NMSX.sendActionBar(sender.getPlayer(), message);
		}
		
		else
		{
			sender.sendMessage(message);
		}
	}
}

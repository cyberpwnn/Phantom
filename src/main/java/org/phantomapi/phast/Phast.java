package org.phantomapi.phast;

import javax.script.ScriptException;
import org.phantomapi.Phantom;
import org.phantomapi.command.PhantomCommandSender;
import org.phantomapi.lang.GList;
import org.phantomapi.sync.TaskLater;

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
							d[0] += wa;
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

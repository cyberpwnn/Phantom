package org.phantomapi.util;

import org.phantomapi.lang.GList;

/**
 * Exception utils
 * 
 * @author cyberpwn
 */
public class ExceptionUtil
{
	/**
	 * Print the exception
	 * 
	 * @param e
	 *            the exception
	 */
	public static void print(Throwable e)
	{
		D d = new D("EX");
		d.f(e.getClass().getSimpleName() + " thrown at " + C.YELLOW + e.getStackTrace()[0].getClassName() + C.LIGHT_PURPLE + "." + e.getStackTrace()[0].getMethodName() + "(" + e.getStackTrace()[0].getLineNumber() + ")");
		d.w(e.getMessage());
		int k = 0;
		
		for(StackTraceElement i : new GList<StackTraceElement>(e.getStackTrace()))
		{
			String s = F.repeat(" ", k) + "  \\" + i.getClassName() + "." + C.YELLOW + i.getMethodName() + C.LIGHT_PURPLE + "(" + i.getLineNumber() + ")";
			
			if(i.getClassName().equals(e.getStackTrace()[0].getClassName()))
			{
				s = C.RED + s;
			}
			
			else if(i.getClassName().startsWith("net.minecraft.server."))
			{
				s = C.DARK_BLUE + s;
			}
			
			else if(i.getClassName().startsWith("org.bukkit.craftbukkit"))
			{
				s = C.DARK_GREEN + s;
			}
			
			else if(i.getClassName().startsWith("org.bukkit."))
			{
				s = C.DARK_AQUA + s;
			}
			
			else if(i.getClassName().startsWith("org.phantomapi."))
			{
				s = C.DARK_PURPLE + s;
			}
			
			else
			{
				s = C.DARK_GRAY + s;
			}
			
			d.f(s);
			k++;
		}
	}
}

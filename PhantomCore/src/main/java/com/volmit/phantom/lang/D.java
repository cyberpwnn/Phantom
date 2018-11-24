package com.volmit.phantom.lang;

import com.volmit.phantom.plugin.Phantom;
import com.volmit.phantom.text.C;

public class D
{
	private String tag;
	
	public D(String t)
	{
		tag = t;
	}
	
	private void log(String f, Object... s)
	{
		GList<Object> m = new GList<Object>(s);
		String msg = m.toString(", ");
		String c = C.getLastColors(msg);
		
		if(f.equals("INFO"))
		{
			c = C.WHITE.toString();
		}
		
		if(f.equals("WARN"))
		{
			c = C.YELLOW.toString();
		}
		
		if(f.equals("FATAL"))
		{
			c = C.RED.toString();
		}
		
		if(f.equals("VERBOSE"))
		{
			c = C.AQUA.toString();
		}
		
		Phantom.LOG_BUFFER.add(c + "|" + f.toUpperCase() + "| " + C.LIGHT_PURPLE + tag + C.WHITE + " " + c + msg + c);
	}
	
	public void l(Object... s)
	{
		log("INFO", s);
	}
	
	public void v(Object... s)
	{
		log("VERBOSE", s);
	}
	
	public void w(Object... s)
	{
		log("WARN", s);
	}
	
	public void f(Object... s)
	{
		log("FATAL", s);
	}
}

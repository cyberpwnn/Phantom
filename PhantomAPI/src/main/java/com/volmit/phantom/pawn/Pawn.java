package com.volmit.phantom.pawn;

import java.lang.reflect.Field;
import com.volmit.phantom.lang.GList;

public class Pawn
{
	public GList<Field> getAllFields()
	{
		Class<? extends Pawn> p = getClass();
		GList<Class<?>> classes = new GList<Class<?>>().qadd(p);
		GList<Field> fields = new GList<Field>();
		Class<?> c = null;
		
		while(!(c = p.getSuperclass()).equals(Object.class))
		{
			classes.add(c);
		}
		
		for(Class<?> i : classes)
		{
			if(i.isAnnotationPresent(Invisible.class))
			{
				continue;
			}
			
			for(Field j : i.getDeclaredFields())
			{
				if(j.isAnnotationPresent(Invisible.class))
				{
					continue;
				}
				
				j.setAccessible(true);
				fields.add(j);
			}
		}
		
		return fields;
	}
}

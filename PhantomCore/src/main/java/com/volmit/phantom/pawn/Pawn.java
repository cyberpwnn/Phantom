package com.volmit.phantom.pawn;

import java.lang.reflect.Field;
import java.util.UUID;
import com.volmit.phantom.lang.GList;

public class Pawn
{
	private final UUID id = UUID.randomUUID();
	
	public UUID getPawnId()
	{
		return id;
	}
	
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

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		Pawn other = (Pawn) obj;
		if(id == null)
		{
			if(other.id != null)
				return false;
		}
		else
			if(!id.equals(other.id))
				return false;
		return true;
	}
}

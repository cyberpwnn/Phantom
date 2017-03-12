package org.phantomapi.text;

import java.lang.reflect.Field;
import org.phantomapi.lang.GMap;

public class LanguagUtil
{
	public static GMap<Field, Lang> getFields(Class<?> c)
	{
		GMap<Field, Lang> fields = new GMap<Field, Lang>();
		
		for(Field i : c.getDeclaredFields())
		{
			if(i.isAnnotationPresent(Lang.class))
			{
				fields.put(i, i.getAnnotationsByType(Lang.class)[0]);
			}
		}
		
		return fields;
	}
}

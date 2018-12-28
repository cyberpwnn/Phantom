package com.volmit.phantom.pawn;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import com.volmit.phantom.json.JSONArray;
import com.volmit.phantom.json.JSONObject;
import com.volmit.phantom.lang.GList;
import com.volmit.phantom.lang.GMap;
import com.volmit.phantom.lang.PackageScanner;

public class UniversalParser
{
	private static final List<Class<?>> PRIMATIVES;
	private static final Map<Class<?>, ObjectHandler<?>> HANDLERS;

	public static void addHandlers(String mPackage, Class<?> jar) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException
	{
		PackageScanner s = new PackageScanner(new File(jar.getProtectionDomain().getCodeSource().getLocation().getFile()), mPackage);
		s.scan();
		
		for(Class<?> i : s.getClasses())
		{
			if(i.isAssignableFrom(ObjectHandler.class))
			{
				addHandler((ObjectHandler<?>) i.getConstructor().newInstance());
			}
		}
	}
	
	public static void addHandler(ObjectHandler<?> h)
	{
		HANDLERS.put(h.getHandledClass(), h);
	}
	
	public static String nept(String d)
	{
		return d == null ? "" : d;
	}

	@SuppressWarnings("unchecked")
	public static <T> T fromJSON(JSONObject o, Class<T> type) throws IllegalArgumentException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException, SecurityException
	{
		Set<Field> fields = new HashSet<Field>();

		for(Field i : type.getSuperclass().getDeclaredFields())
		{
			fields.add(i);
		}

		for(Field i : type.getFields())
		{
			fields.add(i);
		}

		for(Field i : type.getDeclaredFields())
		{
			fields.add(i);
		}

		Object m = type.getConstructor().newInstance();

		for(String i : o.keySet())
		{
			Object v = o.get(i);

			for(Field j : fields)
			{
				j.setAccessible(true);

				if(i.equals(j.getName()))
				{
					System.out.println(j.getName() + " > " + j.getType());
					if(v.getClass().equals(String.class))
					{
						j.set(m, stringToValue((String) v, j.getType()));
					}

					if(v.getClass().equals(JSONArray.class))
					{
						ListType dtype = j.getAnnotation(ListType.class);

						if(dtype != null)
						{
							JSONArray arr = (JSONArray) v;

							for(int k = 0; k < arr.length(); k++)
							{
								Object d = arr.get(k);
								Object list = j.get(m);

								if(d.getClass().equals(String.class))
								{
									list.getClass().getMethod("add", Object.class).invoke(list, dtype.value().cast(stringToValue((String) d, dtype.value())));
								}

								if(d.getClass().equals(JSONObject.class))
								{
									list.getClass().getMethod("add", Object.class).invoke(list, dtype.value().cast(fromJSON((JSONObject) d, dtype.value())));
								}
							}
						}
					}

					if(v.getClass().equals(JSONObject.class))
					{
						j.set(m, j.getType().cast(fromJSON((JSONObject) v, j.getType())));
					}
				}
			}
		}

		return (T) m;
	}

	public static JSONObject toJSON(Object o) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, SecurityException
	{
		JSONObject j = new JSONObject();
		Set<Field> fields = new HashSet<Field>();

		for(Field i : o.getClass().getSuperclass().getDeclaredFields())
		{
			fields.add(i);
		}

		for(Field i : o.getClass().getFields())
		{
			fields.add(i);
		}

		for(Field i : o.getClass().getDeclaredFields())
		{
			fields.add(i);
		}

		for(Field i : fields)
		{
			if(Modifier.isStatic(i.getModifiers()))
			{
				continue;
			}

			if(Modifier.isFinal(i.getModifiers()))
			{
				continue;
			}

			i.setAccessible(true);
			Object value = i.get(o);

			if(value == null)
			{
				continue;
			}

			if(i.getType().equals(GList.class))
			{
				ListType type = i.getAnnotation(ListType.class);

				if(type != null)
				{
					JSONArray a = new JSONArray();

					for(Object k : (GList<?>) value)
					{
						a.put(valueToString(k));
					}

					j.put(i.getName(), a);
				}
			}

			else
			{
				j.put(i.getName(), valueToString(value));
			}
		}

		return j;
	}

	private static Object stringToValue(String value, Class<?> type) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
	{
		if(type.equals(int.class))
		{
			return Integer.valueOf(value).intValue();
		}

		if(type.equals(long.class))
		{
			return Long.valueOf(value).longValue();
		}
		
		if(type.equals(byte.class))
		{
			return Byte.valueOf(value).byteValue();
		}
		
		if(type.equals(short.class))
		{
			return Short.valueOf(value).shortValue();
		}
		
		if(type.equals(float.class))
		{
			return Float.valueOf(value).floatValue();
		}

		if(type.equals(double.class))
		{
			return Double.valueOf(value).doubleValue();
		}

		if(type.equals(boolean.class))
		{
			return Boolean.valueOf(value).booleanValue();
		}

		if(type.equals(Integer.class))
		{
			return Integer.valueOf(value);
		}

		if(type.equals(Long.class))
		{
			return Long.valueOf(value);
		}

		if(type.equals(Double.class))
		{
			return Double.valueOf(value);
		}

		if(type.equals(Boolean.class))
		{
			return Boolean.valueOf(value);
		}
		
		if(type.equals(Short.class))
		{
			return Short.valueOf(value);
		}
		
		if(type.equals(Byte.class))
		{
			return Byte.valueOf(value);
		}
		
		if(type.equals(Float.class))
		{
			return Float.valueOf(value);
		}
		
		if(type.equals(UUID.class))
		{
			return UUID.fromString(value);
		}

		if(type.equals(String.class))
		{
			return value;
		}

		if(type.isEnum())
		{
			return type.getMethod("valueOf", String.class).invoke(null, value);
		}
		
		if(HANDLERS.containsKey(type))
		{
			return HANDLERS.get(type).fromString(value);
		}

		System.out.println("Warning: Unknown type " + type.getName());

		return null;
	}

	private static Object valueToString(Object value) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, SecurityException
	{
		if(value == null)
		{
			return "";
		}

		else if(PRIMATIVES.contains(value.getClass()))
		{
			return value.toString();
		}

		else if(value.getClass().equals(UUID.class))
		{
			return value.toString();
		}
		
		else if(HANDLERS.containsKey(value.getClass()))
		{
			return HANDLERS.get(value.getClass()).getClass().getMethod("toString", value.getClass()).invoke(HANDLERS.get(value.getClass()), value);
		}
		
		else if(value.getClass().isEnum())
		{
			return value.getClass().getMethod("name").invoke(value);
		}

		else
		{
			return toJSON(value);
		}
	}

	static
	{
		HANDLERS = new GMap<Class<?>, ObjectHandler<?>>();
		PRIMATIVES = new ArrayList<Class<?>>();
		PRIMATIVES.add(int.class);
		PRIMATIVES.add(long.class);
		PRIMATIVES.add(double.class);
		PRIMATIVES.add(boolean.class);
		PRIMATIVES.add(short.class);
		PRIMATIVES.add(byte.class);
		PRIMATIVES.add(float.class);
		PRIMATIVES.add(String.class);
		PRIMATIVES.add(Integer.class);
		PRIMATIVES.add(Long.class);
		PRIMATIVES.add(Short.class);
		PRIMATIVES.add(Byte.class);
		PRIMATIVES.add(Float.class);
		PRIMATIVES.add(Double.class);
		PRIMATIVES.add(Boolean.class);
	}
}

package org.phantomapi.nbt;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Map.Entry;
import org.phantomapi.lang.GMap;

public final class NBTTagCompound extends NBTBase
{
	
	private static Method _getByte;
	private static Method _getShort;
	private static Method _getInt;
	private static Method _getLong;
	private static Method _getFloat;
	private static Method _getDouble;
	private static Method _getString;
	private static Field _mapField;
	
	private static Method _tagSerializeStream;
	private static Method _tagUnserializeStream;
	
	static void prepareReflectionz() throws SecurityException, NoSuchMethodException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException
	{
		_getByte = _nbtTagCompoundClass.getMethod("getByte", String.class);
		_getShort = _nbtTagCompoundClass.getMethod("getShort", String.class);
		_getInt = _nbtTagCompoundClass.getMethod("getInt", String.class);
		_getLong = _nbtTagCompoundClass.getMethod("getLong", String.class);
		_getFloat = _nbtTagCompoundClass.getMethod("getFloat", String.class);
		_getDouble = _nbtTagCompoundClass.getMethod("getDouble", String.class);
		_getString = _nbtTagCompoundClass.getMethod("getString", String.class);
		_mapField = _nbtTagCompoundClass.getDeclaredField("map");
		_mapField.setAccessible(true);
		
		Class<?> nbtCompressedStreamToolsClass = BukkitReflect.getMinecraftClass("NBTCompressedStreamTools");
		_tagSerializeStream = nbtCompressedStreamToolsClass.getMethod("a", _nbtTagCompoundClass, OutputStream.class);
		_tagUnserializeStream = nbtCompressedStreamToolsClass.getMethod("a", InputStream.class);
	}
	
	Map<String, Object> _map;
	
	public NBTTagCompound()
	{
		this(BukkitReflect.newInstance(_nbtTagCompoundClass));
	}
	
	@SuppressWarnings("unchecked")
	NBTTagCompound(Object handle)
	{
		super(handle);
		_map = (Map<String, Object>) BukkitReflect.getFieldValue(handle, _mapField);
	}
	
	public byte getByte(String key)
	{
		return (Byte) invokeMethod(_getByte, key);
	}
	
	public short getShort(String key)
	{
		return (Short) invokeMethod(_getShort, key);
	}
	
	public int getInt(String key)
	{
		return (Integer) invokeMethod(_getInt, key);
	}
	
	public long getLong(String key)
	{
		return (Long) invokeMethod(_getLong, key);
	}
	
	public float getFloat(String key)
	{
		return (Float) invokeMethod(_getFloat, key);
	}
	
	public Double getDouble(String key)
	{
		return (Double) invokeMethod(_getDouble, key);
	}
	
	public String getString(String key)
	{
		return (String) invokeMethod(_getString, key);
	}
	
	public NBTTagCompound getCompound(String key)
	{
		Object obj = _map.get(key);
		if(obj != null && _nbtTagCompoundClass.isInstance(obj))
		{
			return new NBTTagCompound(obj);
		}
		return null;
	}
	
	public NBTTagList getList(String key)
	{
		Object obj = _map.get(key);
		if(obj != null && _nbtTagListClass.isInstance(obj))
		{
			return new NBTTagList(obj);
		}
		return null;
	}
	
	public Object[] getListAsArray(String key)
	{
		NBTTagList list = getList(key);
		return (list == null ? null : list.getAsArray());
	}
	
	public void setByte(String key, byte value)
	{
		set(key, value);
	}
	
	public void setShort(String key, short value)
	{
		set(key, value);
	}
	
	public void setInt(String key, int value)
	{
		set(key, value);
	}
	
	public void setLong(String key, long value)
	{
		set(key, value);
	}
	
	public void setFloat(String key, float value)
	{
		set(key, value);
	}
	
	public void setDouble(String key, double value)
	{
		set(key, value);
	}
	
	public void setString(String key, String value)
	{
		set(key, value);
	}
	
	public void setCompound(String key, NBTTagCompound value)
	{
		set(key, value);
	}
	
	public void setList(String key, NBTTagList value)
	{
		set(key, value);
	}
	
	public void setList(String key, Object... objects)
	{
		set(key, new NBTTagList(objects));
	}
	
	public GMap<String, Object> getMap()
	{
		return new GMap<String, Object>(_map);
	}
	
	private void set(String key, Object value)
	{
		_map.put(key, NBTTypes.toInternal(value));
	}
	
	public boolean hasKey(String key)
	{
		return _map.containsKey(key);
	}
	
	public void remove(String key)
	{
		_map.remove(key);
	}
	
	public int size()
	{
		return _map.size();
	}
	
	public boolean isEmpty()
	{
		return _map.isEmpty();
	}
	
	public void clear()
	{
		_map.clear();
	}
	
	public byte[] serialize()
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		serialize(out);
		return out.toByteArray();
	}
	
	public void serialize(OutputStream outputStream)
	{
		BukkitReflect.invokeMethod(null, _tagSerializeStream, _handle, outputStream);
	}
	
	public static NBTTagCompound unserialize(byte[] data)
	{
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		NBTTagCompound tag = unserialize(in);
		try
		{
			in.close();
		}
		catch(IOException e)
		{
		}
		return tag;
	}
	
	public static NBTTagCompound unserialize(InputStream inputStream)
	{
		return new NBTTagCompound(BukkitReflect.invokeMethod(null, _tagUnserializeStream, inputStream));
	}
	
	public void merge(NBTTagCompound other)
	{
		for(Entry<String, Object> tag : other._map.entrySet())
		{
			_map.put(tag.getKey(), NBTBase.clone(tag.getValue()));
		}
	}
	
	@Override
	public NBTTagCompound clone()
	{
		return (NBTTagCompound) super.clone();
	}
	
}

package org.phantomapi.nbt;

import java.lang.reflect.Field;
import java.util.List;

public final class NBTTagList extends NBTBase
{
	
	private static Field _typeField;
	private static Field _listField;
	List<Object> _list;
	
	static void prepareReflectionz() throws SecurityException, NoSuchMethodException, NoSuchFieldException
	{
		_typeField = _nbtTagListClass.getDeclaredField("type");
		_typeField.setAccessible(true);
		_listField = _nbtTagListClass.getDeclaredField("list");
		_listField.setAccessible(true);
	}
	
	public NBTTagList()
	{
		this(BukkitReflect.newInstance(_nbtTagListClass));
	}
	
	@SuppressWarnings("unchecked")
	NBTTagList(Object handle)
	{
		super(handle);
		_list = (List<Object>) BukkitReflect.getFieldValue(handle, _listField);
	}
	
	public NBTTagList(Object... values)
	{
		this(BukkitReflect.newInstance(_nbtTagListClass));
		for(Object value : values)
		{
			add(value);
		}
	}
	
	public Object get(int index)
	{
		return NBTTypes.fromInternal(_list.get(index));
	}
	
	public void add(Object value)
	{
		Object handle = NBTTypes.toInternal(value);
		BukkitReflect.setFieldValue(_handle, _typeField, NBTBase.getTypeId(handle));
		_list.add(handle);
	}
	
	public int size()
	{
		return _list.size();
	}
	
	public Object[] getAsArray()
	{
		int length = size();
		Object[] result = new Object[length];
		for(int i = 0; i < length; ++i)
		{
			result[i] = get(i);
		}
		return result;
	}
	
	@Override
	public NBTTagList clone()
	{
		return (NBTTagList) super.clone();
	}
	
}

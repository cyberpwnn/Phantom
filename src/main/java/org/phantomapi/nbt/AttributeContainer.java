package org.phantomapi.nbt;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;

public final class AttributeContainer
{
	
	private LinkedHashMap<AttributeType, Attribute> _attributes = new LinkedHashMap<AttributeType, Attribute>();
	
	public static AttributeContainer fromNBT(NBTTagList data)
	{
		AttributeContainer container = new AttributeContainer();
		for(Object attr : data.getAsArray())
		{
			container.setAttribute(Attribute.fromNBT((NBTTagCompound) attr));
		}
		return container;
	}
	
	public Attribute getAttribute(AttributeType type)
	{
		return _attributes.get(type);
	}
	
	public void setAttribute(Attribute attribute)
	{
		_attributes.put(attribute.getType(), attribute);
	}
	
	public Attribute removeAttribute(AttributeType type)
	{
		return _attributes.remove(type);
	}
	
	public int size()
	{
		return _attributes.size();
	}
	
	public Collection<Attribute> values()
	{
		return Collections.unmodifiableCollection(_attributes.values());
	}
	
	public Collection<AttributeType> types()
	{
		return Collections.unmodifiableCollection(_attributes.keySet());
	}
	
	public NBTTagList toNBT()
	{
		NBTTagList data = new NBTTagList();
		for(Attribute attribute : _attributes.values())
		{
			data.add(attribute.toNBT());
		}
		return data;
	}
	
}

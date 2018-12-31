package com.volmit.phantom.plugin;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import com.volmit.phantom.lang.D;
import com.volmit.phantom.lang.GList;
import com.volmit.phantom.plugin.Scaffold.Permission;

public abstract class PhantomPermission
{
	private PhantomPermission parent;

	public PhantomPermission()
	{
		for(Field i : getClass().getDeclaredFields())
		{
			if(i.isAnnotationPresent(Permission.class))
			{
				if(!Modifier.isStatic(i.getModifiers()))
				{
					D.as("Permission").w("Cannot set a non static permission refrence: " + i.getName());
					continue;
				}

				try
				{
					PhantomPermission px = (PhantomPermission) i.getType().getConstructor().newInstance();
					px.setParent(this);
					i.set(null, px);
				}

				catch(IllegalArgumentException | IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException | SecurityException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	public GList<PhantomPermission> getChildren()
	{
		GList<PhantomPermission> p = new GList<>();

		for(Field i : getClass().getDeclaredFields())
		{
			if(i.isAnnotationPresent(Permission.class))
			{
				if(!Modifier.isStatic(i.getModifiers()))
				{
					continue;
				}

				try
				{
					p.add((PhantomPermission) i.get(null));
				}

				catch(IllegalArgumentException | IllegalAccessException | SecurityException e)
				{
					e.printStackTrace();
				}
			}
		}

		return p;
	}

	public String getFullNode()
	{
		if(hasParent())
		{
			return getParent().getFullNode() + "." + getNode();
		}

		return getNode();
	}

	protected abstract String getNode();

	public abstract String getDescription();

	public abstract boolean isDefault();

	@Override
	public String toString()
	{
		return getFullNode();
	}

	public boolean hasParent()
	{
		return getParent() != null;
	}

	public PhantomPermission getParent()
	{
		return parent;
	}

	public void setParent(PhantomPermission parent)
	{
		this.parent = parent;
	}
}

package com.volmit.phantom.imp.plugin;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import com.volmit.phantom.api.lang.GList;
import com.volmit.phantom.imp.plugin.Scaffold.Permission;

public abstract class PhantomPermission
{
	private PhantomPermission parent;

	public PhantomPermission()
	{
		for(Field i : getClass().getDeclaredFields())
		{
			if(i.isAnnotationPresent(Permission.class))
			{
				try
				{
					PhantomPermission px = (PhantomPermission) i.getType().getConstructor().newInstance();
					px.setParent(this);
					i.set(Modifier.isStatic(i.getModifiers()) ? null : this, px);
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
				try
				{
					p.add((PhantomPermission) i.get(Modifier.isStatic(i.getModifiers()) ? null : this));
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

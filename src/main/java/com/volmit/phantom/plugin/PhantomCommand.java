package com.volmit.phantom.plugin;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import com.volmit.phantom.lang.GList;
import com.volmit.phantom.plugin.Scaffold.Command;

/**
 * Represents a pawn command
 *
 * @author cyberpwn
 *
 */
public abstract class PhantomCommand implements ICommand
{
	private GList<String> nodes;
	private String node;
	private Module owner;

	/**
	 * Override this with a super constructor as most commands shouldnt change these
	 * parameters
	 *
	 * @param node
	 *            the node (primary node) i.e. volume
	 * @param nodes
	 *            the aliases. i.e. v, vol, bile
	 */
	public PhantomCommand(String node, String... nodes)
	{
		this.node = node;
		this.nodes = new GList<String>(nodes);
	}

	public Module getOwner()
	{
		return owner;
	}

	public void setOwner(Module owner)
	{
		this.owner = owner;
	}

	@Override
	public String getNode()
	{
		return node;
	}

	@Override
	public GList<String> getNodes()
	{
		return nodes;
	}

	@Override
	public GList<String> getAllNodes()
	{
		return getNodes().copy().qadd(getNode());
	}

	@Override
	public void addNode(String node)
	{
		getNodes().add(node);
	}

	public GList<PhantomCommand> getChildren()
	{
		GList<PhantomCommand> p = new GList<>();

		for(Field i : getClass().getDeclaredFields())
		{
			if(i.isAnnotationPresent(Command.class))
			{
				try
				{
					i.setAccessible(true);
					p.add((PhantomCommand) i.getType().getConstructor().newInstance());
				}

				catch(IllegalArgumentException | IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException | SecurityException e)
				{
					e.printStackTrace();
				}
			}
		}

		return p;
	}
}

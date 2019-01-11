package com.volmit.phantom.imp.command;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import com.volmit.phantom.api.command.ICommand;
import com.volmit.phantom.api.command.PhantomPermission;
import com.volmit.phantom.api.lang.GList;
import com.volmit.phantom.api.module.Command;
import com.volmit.phantom.api.module.Module;

/**
 * Represents a pawn command
 *
 * @author cyberpwn
 *
 */
public abstract class PhantomCommand implements ICommand
{
	private GList<PhantomCommand> children;
	private GList<String> nodes;
	private GList<String> requiredPermissions;
	private String node;
	private Module owner;
	private String category;

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
		category = "";
		this.node = node;
		this.nodes = new GList<String>(nodes);
		requiredPermissions = new GList<>();
		children = buildChildren();
	}

	protected void requiresPermission(PhantomPermission node)
	{
		requiresPermission(node.toString());
	}

	protected void requiresPermission(String node)
	{
		requiredPermissions.add(node);
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
		return children;
	}

	private GList<PhantomCommand> buildChildren()
	{
		GList<PhantomCommand> p = new GList<>();

		for(Field i : getClass().getDeclaredFields())
		{
			if(i.isAnnotationPresent(Command.class))
			{
				try
				{
					i.setAccessible(true);
					PhantomCommand pc = (PhantomCommand) i.getType().getConstructor().newInstance();
					Command c = i.getAnnotation(Command.class);

					if(!c.value().trim().isEmpty())
					{
						pc.setCategory(c.value().trim());
					}

					else
					{
						pc.setCategory(getCategory());
					}

					p.add(pc);
				}

				catch(IllegalArgumentException | IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException | SecurityException e)
				{
					e.printStackTrace();
				}
			}
		}

		return p;
	}

	@Override
	public GList<String> getRequiredPermissions()
	{
		return requiredPermissions;
	}

	public String getCategory()
	{
		return category;
	}

	public void setCategory(String category)
	{
		this.category = category;
	}
}

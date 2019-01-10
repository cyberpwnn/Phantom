package com.volmit.phantom.util.world;

import com.volmit.phantom.util.plugin.Documented;

/**
 * Represents a cuboid exception
 *
 * @author cyberpwn
 */
@Documented
public class CuboidException extends Exception
{
	public CuboidException(String string)
	{
		super(string);
	}

	private static final long serialVersionUID = 1L;
}
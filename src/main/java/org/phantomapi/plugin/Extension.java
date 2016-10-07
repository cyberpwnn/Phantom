package org.phantomapi.plugin;

import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Controller;

public abstract class Extension extends Controller
{
	protected Extension(Controllable parentController)
	{
		super(parentController);
	}
}

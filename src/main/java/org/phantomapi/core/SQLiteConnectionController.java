package org.phantomapi.core;

import java.io.File;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Controller;
import org.phantomapi.lang.GMap;

public class SQLiteConnectionController extends Controller
{
	private GMap<File, SQLiteHandle> dbs;
	
	public SQLiteConnectionController(Controllable parentController)
	{
		super(parentController);
		
		dbs = new GMap<File, SQLiteHandle>();
	}
	
	public SQLiteHandle getHandle(File file)
	{
		dbs.put(file, new SQLiteHandle(file));
		
		return dbs.get(file);
	}
	
	public void purgeHandles()
	{
		for(File i : dbs.k())
		{
			if(dbs.get(i).isClosed())
			{
				dbs.remove(i);
			}
		}
	}
	
	@Override
	public void onStart()
	{
		
	}
	
	@Override
	public void onStop()
	{
		
	}
}

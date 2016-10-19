package org.phantomapi.hud;

import org.phantomapi.command.PhantomSender;
import org.phantomapi.sync.Task;

public abstract class Hud
{
	protected PhantomSender sender;
	private boolean running;
	
	public Hud(PhantomSender sender)
	{
		this.sender = sender;
		running = true;
		
		new Task(0)
		{
			@Override
			public void run()
			{
				if(!running)
				{
					cancel();
					return;
				}
				
				onTick();
			}
		};
	}
	
	public void stop()
	{
		running = false;
	}
	
	public abstract void onTick();
}

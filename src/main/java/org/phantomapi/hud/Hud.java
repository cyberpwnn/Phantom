package org.phantomapi.hud;

import javax.swing.JFrame;
import org.phantomapi.command.PhantomSender;
import org.phantomapi.sync.Task;

public abstract class Hud
{
	protected PhantomSender sender;
	private boolean running;
	protected JFrame root;
	
	public Hud(PhantomSender sender, JFrame root)
	{
		this.sender = sender;
		running = true;
		
		new Task(0)
		{
			@Override
			public void run()
			{
				if(!root.isVisible())
				{
					running = false;
				}
				
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
		root.setVisible(false);
		root.dispose();
	}
	
	public abstract void onTick();
}

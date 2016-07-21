package org.cyberpwn.phantom.gui;

import org.bukkit.entity.Player;
import org.cyberpwn.phantom.lang.GList;

public class PhantomDialog extends PhantomWindow implements Dialog
{
	private boolean cancellable;
	
	public PhantomDialog(String title, GList<Element> elements, Player viewer, boolean cancellable)
	{
		super(title, elements, viewer);
		
		setCancellable(cancellable);
	}
	
	public Window close()
	{
		super.close();
		
		if(!isCancellable())
		{
			open();
		}
		
		else
		{
			onCancelled(viewer, this, this);
		}
		
		return this;
	}

	public boolean isCancellable()
	{
		return cancellable;
	}

	public Dialog setCancellable(boolean cancellable)
	{
		this.cancellable = cancellable;
		
		return this;
	}

	@Override
	public void onCancelled(Player p, Window w, Dialog d)
	{
		
	}
}

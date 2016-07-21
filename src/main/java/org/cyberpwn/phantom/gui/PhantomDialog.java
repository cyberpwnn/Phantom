package org.cyberpwn.phantom.gui;

import org.bukkit.entity.Player;

/**
 * Implementation of the dialog
 * 
 * @author cyberpwn
 *
 */
public class PhantomDialog extends PhantomWindow implements Dialog
{
	protected boolean cancellable;
	
	/**
	 * Create a phantomDialog
	 * 
	 * @param title
	 *            the title
	 * @param viewer
	 *            the player
	 * @param cancellable
	 *            should this dialog be cancellable?
	 */
	public PhantomDialog(String title, Player viewer, boolean cancellable)
	{
		super(title, viewer);
		
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
	
	/**
	 * Is this dialog cancellable?
	 * 
	 * @return true if it is
	 */
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

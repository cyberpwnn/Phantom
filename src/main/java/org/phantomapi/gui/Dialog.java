package org.phantomapi.gui;

import org.bukkit.entity.Player;

/**
 * Dialogs
 * 
 * @author cyberpwn
 *
 */
public interface Dialog
{
	/**
	 * Set the dialog as cancellable
	 * 
	 * @param cancellable
	 *            if true, the dialog will be closed, and onCancelled will be
	 *            called. However if it is false, the player cannot exit the
	 *            inventory unless called.
	 * @return the dialog modified
	 */
	public Dialog setCancellable(boolean cancellable);
	
	/**
	 * Called when the player cancelled out of the dialog
	 * 
	 * @param p
	 *            the player
	 * @param w
	 *            the window instance of this dialog
	 * @param d
	 *            the dialog
	 */
	public void onCancelled(Player p, Window w, Dialog d);
}

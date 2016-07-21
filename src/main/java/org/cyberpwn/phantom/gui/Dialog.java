package org.cyberpwn.phantom.gui;

import org.bukkit.entity.Player;

public interface Dialog
{
	public Dialog setCancellable(boolean cancellable);
	public void onCancelled(Player p, Window w, Dialog d);
}

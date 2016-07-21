package org.cyberpwn.phantom.construct.test;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.cyberpwn.phantom.gui.Click;
import org.cyberpwn.phantom.gui.Dialog;
import org.cyberpwn.phantom.gui.Element;
import org.cyberpwn.phantom.gui.PhantomDialog;
import org.cyberpwn.phantom.gui.PhantomElement;
import org.cyberpwn.phantom.gui.PhantomWindow;
import org.cyberpwn.phantom.gui.Slot;
import org.cyberpwn.phantom.gui.Window;
import org.cyberpwn.phantom.util.C;

public class ExampleGUI
{
	public ExampleGUI(Player player)
	{
		//Create a "window" instance
		Window main = new PhantomWindow(C.RED + "Main Window", player);
		
		//Lets make a dialog window also
		Dialog dialog = new PhantomDialog("Are you sure?", player, true);
		
		//Create an element SLIME BALL, in a slot, named Button
		Element b = new PhantomElement(Material.SLIME_BALL, new Slot(0, 1), C.BLUE + "Button")
		{
			@Override
			public void onClick(Player p, Click c, Window w)
			{
				//Here, we override what happens when a player clicks the button
				
				//Lets open the dialog
				((Window)dialog).open();
			}
		};
		
		//Add the button to the MAIN window
		main.addElement(b);
	}
}

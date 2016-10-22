package org.phantomapi.hud;

import javax.swing.UIManager;

public class HudUtil
{
	public static void setSystemUi()
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}

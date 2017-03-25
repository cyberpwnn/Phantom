package org.phantomapi.frame;

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

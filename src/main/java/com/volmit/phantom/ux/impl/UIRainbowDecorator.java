package com.volmit.phantom.ux.impl;

import org.bukkit.Material;

import com.volmit.phantom.util.MaterialBlock;
import com.volmit.phantom.ux.Element;
import com.volmit.phantom.ux.Window;
import com.volmit.phantom.ux.WindowDecorator;

public class UIRainbowDecorator implements WindowDecorator
{
	@Override
	public Element onDecorateBackground(Window window, int position, int row)
	{
		int apos = window.getRealPosition(position, row);

		return new UIElement("bh")
				.setBackground(true)
				.setName(" ")
				.setMaterial(
						new MaterialBlock(
								Material.STAINED_GLASS_PANE,
								(byte) (apos % 15)));
	}
}

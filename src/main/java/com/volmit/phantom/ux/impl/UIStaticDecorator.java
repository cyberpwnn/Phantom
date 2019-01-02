package com.volmit.phantom.ux.impl;

import org.bukkit.Material;

import com.volmit.phantom.util.MaterialBlock;
import com.volmit.phantom.ux.Element;
import com.volmit.phantom.ux.Window;
import com.volmit.phantom.ux.WindowDecorator;

public class UIStaticDecorator implements WindowDecorator
{
	private Element element;

	public UIStaticDecorator(Element element)
	{
		this.element = element == null ? new UIElement("bg").setMaterial(new MaterialBlock(Material.AIR)) : element;
	}

	@Override
	public Element onDecorateBackground(Window window, int position, int row)
	{
		return element;
	}
}

package com.volmit.phantom.ux.impl;

import org.bukkit.Material;

import com.volmit.phantom.text.C;
import com.volmit.phantom.util.MaterialBlock;

public class UIPaneDecorator extends UIStaticDecorator
{
	public UIPaneDecorator(C color)
	{
		super(new UIElement("c").setName(" ").setMaterial(new MaterialBlock(Material.STAINED_GLASS_PANE, color.getItemMeta())));
	}
}

package com.volmit.phantom.api.inventory;

import org.bukkit.Material;

import com.volmit.phantom.util.text.C;
import com.volmit.phantom.util.world.MaterialBlock;

public class UIPaneDecorator extends UIStaticDecorator
{
	public UIPaneDecorator(C color)
	{
		super(new UIElement("c").setName(" ").setMaterial(new MaterialBlock(Material.STAINED_GLASS_PANE, color.getItemMeta())));
	}
}

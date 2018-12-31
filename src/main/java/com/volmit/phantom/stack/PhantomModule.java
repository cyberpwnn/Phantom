package com.volmit.phantom.stack;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.volmit.phantom.math.M;
import com.volmit.phantom.plugin.Module;
import com.volmit.phantom.plugin.SVC;
import com.volmit.phantom.plugin.Scaffold.Command;
import com.volmit.phantom.plugin.Scaffold.Instance;
import com.volmit.phantom.plugin.Scaffold.ModuleInfo;
import com.volmit.phantom.plugin.Scaffold.PlayerTest;
import com.volmit.phantom.plugin.Scaffold.Start;
import com.volmit.phantom.services.DevelopmentSVC;
import com.volmit.phantom.text.C;
import com.volmit.phantom.util.MaterialBlock;
import com.volmit.phantom.ux.Window;
import com.volmit.phantom.ux.WindowResolution;
import com.volmit.phantom.ux.impl.UIElement;
import com.volmit.phantom.ux.impl.UIPaneDecorator;
import com.volmit.phantom.ux.impl.UIWindow;

@ModuleInfo(name = "Phantom", version = "HEAD", author = "cyberpwn", color = C.LIGHT_PURPLE)
public class PhantomModule extends Module
{
	@Instance
	public static PhantomModule instance;

	@Command
	public CommandPhantom phantom;

	@Command("Modules")
	public CommandModules modules;

	@Command("Services")
	public CommandServices services;

	@Start
	public void start()
	{
		SVC.start(DevelopmentSVC.class);
	}

	@PlayerTest
	public void inventory(Player p)
	{
		Window w = new UIWindow(p)
				.setResolution(WindowResolution.W9_H6)
				.setDecorator(new UIPaneDecorator(C.BLACK))
				.setTitle("Testing")
				.setViewportHeight(6);

		for(int i = 0; i < 128; i++)
		{
			w.setElement(M.rand(-1, 1), i, new UIElement("tow")
					.setEnchanted(true)
					.setMaterial(new MaterialBlock(Material.DIRT, (byte) 1))
					.setName("Row " + i)
					.addLore("Lore 2 1")
					.addLore("Lore 2 2")
					.setCount(3));
		}

		w.open();
	}
}

package org.phantomapi.service;

import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.PrepareItemCraftEvent;

import phantom.lang.GList;
import phantom.pawn.Name;
import phantom.pawn.Register;
import phantom.pawn.Singular;
import phantom.pawn.Start;
import phantom.pawn.Stop;
import phantom.recipe.IRecipe;
import phantom.service.IService;
import phantom.util.metrics.Documented;

/**
 * Recipe service for creating, overriding, and deleting recipes
 *
 * @author cyberpwn
 */
@Documented
@Register
@Name("SVC Recipe")
@Singular
public class RecipeSVC implements IService
{
	private GList<IRecipe> recipes;

	@Start
	public void start()
	{
		recipes = new GList<IRecipe>();
	}

	@Stop
	public void stop()
	{

	}

	@EventHandler
	public void on(PrepareItemCraftEvent e)
	{
		for(IRecipe i : recipes)
		{
			if(i.matches(e.getInventory()))
			{
				e.getInventory().setResult(i.getResult());
			}
		}
	}
}

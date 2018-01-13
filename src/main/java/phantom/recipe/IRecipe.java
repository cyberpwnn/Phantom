package phantom.recipe;

import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

import phantom.lang.GList;

public interface IRecipe
{
	public ItemStack getResult();

	public GList<ItemStack> getAdditionalResults();

	public boolean matches(CraftingInventory inventory);
}

package phantom.recipe;

import org.bukkit.Material;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

import phantom.lang.GList;
import phantom.world.MaterialBlock;

public class PhantomShapelessRecipe implements IRecipe
{
	private ItemStack result;
	private GList<ItemStack> additionalResults;
	private GList<ItemStack> ingredients;
	private GList<Material> materials;
	private GList<MaterialBlock> materialBlocks;

	public PhantomShapelessRecipe(ItemStack result)
	{
		this.result = result;
		additionalResults = new GList<ItemStack>();
		ingredients = new GList<ItemStack>();
		materials = new GList<Material>();
		materialBlocks = new GList<MaterialBlock>();
	}

	public void addAdditionalResult(ItemStack item)
	{
		additionalResults.add(item);
	}

	public void addIngredient(ItemStack item)
	{
		ingredients.add(item);
	}

	public void addIngredient(Material material)
	{
		materials.add(material);
	}

	public void addIngredient(MaterialBlock mb)
	{
		materialBlocks.add(mb);
	}

	@Override
	public ItemStack getResult()
	{
		return result;
	}

	@Override
	public GList<ItemStack> getAdditionalResults()
	{
		return additionalResults;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean matches(CraftingInventory inventory)
	{
		GList<ItemStack> match = new GList<ItemStack>();
		GList<ItemStack> forItems = ingredients.copy();
		GList<Material> forMaterials = materials.copy();
		GList<MaterialBlock> forMaterialBlocks = materialBlocks.copy();

		for(int i = 1; i < inventory.getContents().length; i++)
		{
			if(inventory.getContents()[i] != null && !inventory.getContents()[i].getType().equals(Material.AIR))
			{
				match.add(inventory.getContents()[i].clone());
			}
		}

		if(!match.isEmpty() && !forItems.isEmpty())
		{
			for(ItemStack i : match.copy())
			{
				for(ItemStack j : forItems.copy())
				{
					if(i.equals(j))
					{
						match.remove(i);
						forItems.remove(j);
					}
				}
			}
		}

		if(!match.isEmpty() && !forMaterialBlocks.isEmpty())
		{
			for(ItemStack i : match.copy())
			{
				for(MaterialBlock j : forMaterialBlocks.copy())
				{
					if(new MaterialBlock(i.getType(), i.getData().getData()).equals(j))
					{
						match.remove(i);
						forMaterialBlocks.remove(j);
					}
				}
			}
		}

		if(!match.isEmpty() && !forMaterials.isEmpty())
		{
			for(ItemStack i : match.copy())
			{
				for(Material j : forMaterials.copy())
				{
					if(i.getType().equals(j))
					{
						match.remove(i);
						forMaterials.remove(j);
					}
				}
			}
		}

		return match.isEmpty() && forItems.isEmpty() && forMaterialBlocks.isEmpty() && forMaterials.isEmpty();
	}
}

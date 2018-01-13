package phantom.recipe;

import org.bukkit.Material;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

import phantom.lang.GList;
import phantom.lang.GMap;
import phantom.world.MaterialBlock;

public class PhantomShapedRecipe implements IRecipe
{
	private GMap<Character, ItemStack> patternItems;
	private GMap<Character, MaterialBlock> patternMaterialBlocks;
	private GMap<Character, Material> patternMaterials;
	private GList<String> pattern;
	private ItemStack result;
	private GList<ItemStack> additionalResults;

	public PhantomShapedRecipe(ItemStack result, String... pattern)
	{
		this.result = result;
		this.pattern = new GList<String>(pattern);
		this.additionalResults = new GList<ItemStack>();
		this.patternItems = new GMap<Character, ItemStack>();
		this.patternMaterials = new GMap<Character, Material>();
		this.patternMaterialBlocks = new GMap<Character, MaterialBlock>();
	}

	public void addAdditionalResult(ItemStack is)
	{
		additionalResults.add(is);
	}

	public void addIngredient(String key, Material value)
	{
		char c = key.charAt(0);
		patternMaterials.put(c, value);
		patternItems.remove(c);
		patternMaterialBlocks.remove(c);
	}

	public void addIngredient(String key, ItemStack value)
	{
		char c = key.charAt(0);
		patternItems.put(c, value);
		patternMaterials.remove(c);
		patternMaterialBlocks.remove(c);
	}

	public void addIngredient(String key, MaterialBlock value)
	{
		char c = key.charAt(0);
		patternMaterials.remove(c);
		patternItems.remove(c);
		patternMaterialBlocks.put(c, value);
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

	public GMap<Character, ItemStack> getPatternItems()
	{
		return patternItems;
	}

	public GMap<Character, MaterialBlock> getPatternMaterialBlocks()
	{
		return patternMaterialBlocks;
	}

	public GMap<Character, Material> getPatternMaterials()
	{
		return patternMaterials;
	}

	public GList<String> getPattern()
	{
		return pattern;
	}

	@Override
	public boolean matches(CraftingInventory inventory)
	{
		return false;
	}
}

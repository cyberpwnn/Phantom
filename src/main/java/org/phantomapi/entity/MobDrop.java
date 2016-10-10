package org.phantomapi.entity;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.phantomapi.lang.GList;

/**
 * Mob drops
 *
 * @author cyberpwn
 */
public enum MobDrop
{
	CHICKEN(Material.FEATHER, Material.RAW_CHICKEN),
	COW(Material.LEATHER, Material.RAW_BEEF),
	HORSE(Material.LEATHER),
	MUSHROOM_COW(Material.LEATHER, Material.RAW_BEEF),
	PIG(Material.PORK),
	RABBIT(Material.RABBIT, Material.RABBIT_HIDE),
	SHEEP(Material.WOOL, Material.MUTTON),
	SQUID(Material.INK_SACK),
	IRON_GOLEM(Material.IRON_INGOT),
	SNOWMAN(Material.SNOW_BALL, Material.PUMPKIN),
	BLAZE(Material.BLAZE_ROD),
	CAVE_SPIDER(Material.STRING, Material.SPIDER_EYE),
	CREEPER(Material.SULPHUR),
	GUARDIAN(Material.PRISMARINE_CRYSTALS, Material.PRISMARINE_CRYSTALS, Material.RAW_FISH),
	ENDERMAN(Material.ENDER_PEARL),
	GHAST(Material.GHAST_TEAR, Material.SULPHUR),
	MAGMA_CUBE(Material.MAGMA_CREAM),
	SKELETON(Material.BONE),
	SLIME(Material.SLIME_BALL),
	SPIDER(Material.STRING, Material.SPIDER_EYE),
	WITCH(Material.GLASS_BOTTLE, Material.GLOWSTONE_DUST, Material.REDSTONE, Material.SULPHUR, Material.SPIDER_EYE, Material.STICK, Material.SUGAR),
	WITHER(Material.FEATHER, Material.RAW_CHICKEN),
	ZOMBIE(Material.ROTTEN_FLESH),
	PIG_ZOMBIE(Material.FEATHER, Material.RAW_CHICKEN);

	private Material[] materials;

	private MobDrop(Material... materials)
	{
		this.materials = materials;
	}

	/**
	 * Get mob drops
	 *
	 * @return the item stacks
	 */
	public GList<ItemStack> getDrops()
	{
		GList<ItemStack> is = new GList<ItemStack>();

		for(Material i : materials)
		{
			is.add(new ItemStack(i));
		}

		return is;
	}

	/**
	 * Get materials
	 *
	 * @return the materials
	 */
	public GList<Material> getMaterials()
	{
		return new GList<Material>(materials);
	}
}

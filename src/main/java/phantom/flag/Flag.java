package phantom.flag;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import phantom.lang.GList;
import phantom.util.metrics.Documented;

/**
 * Flag (banner stuff) useful for copy a banner from a block into an item stack
 * or whatnot. Holds blueprint information for banners and can be loaded or
 * applied to items or blocks
 * 
 * @author cyberpwn
 */
@Documented
public class Flag
{
	private GList<FlagLayer> layers;
	private DyeColor baseColor;

	/**
	 * Create an empty banner with a base color
	 * 
	 * @param baseColor
	 *            the base color
	 */
	public Flag(DyeColor baseColor)
	{
		this.layers = new GList<FlagLayer>();
		this.baseColor = baseColor;
	}

	/**
	 * Create a flag representing the given banner block
	 * 
	 * @param block
	 *            the block. Does nothing if it is not a banner
	 */
	public Flag(Block block)
	{
		this(DyeColor.WHITE);

		if(block.getState() instanceof Banner)
		{
			Banner bm = ((Banner) block.getState());
			baseColor = bm.getBaseColor();

			for(int i = 0; i < bm.numberOfPatterns(); i++)
			{
				if(bm.getPattern(i).getPattern().equals(PatternType.BASE))
				{
					continue;
				}

				addLayer(new FlagLayer(bm.getPattern(i)));
			}
		}
	}

	/**
	 * Create a flag representing a banner item's layers. Does nothing if it is not
	 * a banner
	 * 
	 * @param item
	 *            the item. Does nothing if it isnt a banner
	 */
	@SuppressWarnings("deprecation")
	public Flag(ItemStack item)
	{
		this(DyeColor.WHITE);

		if(item.getType().equals(Material.BANNER))
		{
			BannerMeta bm = ((BannerMeta) item.getItemMeta());
			baseColor = bm.getBaseColor();

			for(int i = 0; i < bm.numberOfPatterns(); i++)
			{
				if(bm.getPattern(i).getPattern().equals(PatternType.BASE))
				{
					continue;
				}

				addLayer(new FlagLayer(bm.getPattern(i)));
			}
		}
	}

	/**
	 * Convert the supplied banner item's layers into this flags layers
	 * 
	 * @param is
	 *            the itemstack (banner) does nothing if the item is not a banner
	 * @return the same itemstack with modified bannermeta (or the same item if it
	 *         isnt a banner)
	 */
	@SuppressWarnings("deprecation")
	public ItemStack toItem(ItemStack is)
	{
		if(!is.getType().equals(Material.BANNER))
		{
			return is;
		}

		BannerMeta bm = ((BannerMeta) is.getItemMeta());
		bm.setBaseColor(baseColor);

		for(int i = 0; i < bm.numberOfPatterns(); i++)
		{
			bm.removePattern(i);
		}

		for(FlagLayer i : layers)
		{
			bm.addPattern(i.toPattern());
		}

		is.setItemMeta(bm);

		return is;
	}

	/**
	 * Create an banner itemstack with this flags contents
	 * 
	 * @return the item
	 */
	public ItemStack toItem()
	{
		return toItem(new ItemStack(Material.BANNER));
	}

	/**
	 * Apply this flags contents to a block. Does nothing unless this block is a
	 * banner type
	 * 
	 * @param b
	 *            the block
	 */
	public void toBlock(Block b)
	{
		if(b.getState() instanceof org.bukkit.block.Banner)
		{
			org.bukkit.block.Banner bm = ((org.bukkit.block.Banner) b.getState());
			bm.setBaseColor(baseColor);

			for(int i = 0; i < bm.numberOfPatterns(); i++)
			{
				bm.removePattern(i);
			}

			for(FlagLayer i : layers)
			{
				bm.addPattern(i.toPattern());
			}

			bm.update();
		}
	}

	/**
	 * Convert this flag to json/nbt data (used in commands)
	 */
	@SuppressWarnings("deprecation")
	public String toString()
	{
		String ta = "{BlockEntityTag:{Base:" + String.valueOf((int) baseColor.getDyeData()) + ",Patterns:[";

		for(FlagLayer i : layers)
		{
			ta += i.toString() + ",";
		}

		return ta.substring(0, ta.length() - 1) + "]}}";
	}

	/**
	 * Add a layer to this flag
	 * 
	 * @param shape
	 *            the shape
	 * @param color
	 *            the color
	 */
	public void addLayer(FlagPattern shape, DyeColor color)
	{
		addLayer(new FlagLayer(shape, color));
	}

	/**
	 * Add a layer to this flag
	 * 
	 * @param layer
	 *            the flag layer
	 */
	public void addLayer(FlagLayer layer)
	{
		layers.add(layer);
	}

	/**
	 * Get the base color of this flag
	 * 
	 * @return the base color
	 */
	public DyeColor getBaseColor()
	{
		return baseColor;
	}

	/**
	 * Set the base color of this flag
	 * 
	 * @param baseColor
	 *            the base color
	 */
	public void setBaseColor(DyeColor baseColor)
	{
		this.baseColor = baseColor;
	}

	/**
	 * Get a direct reference to all of the flag layers
	 * 
	 * @return the layers
	 */
	public GList<FlagLayer> getLayers()
	{
		return layers;
	}
}

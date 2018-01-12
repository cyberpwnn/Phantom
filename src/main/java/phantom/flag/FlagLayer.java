package phantom.flag;

import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;

import phantom.util.metrics.Documented;

/**
 * Represents a layer of a banner
 * 
 * @author cyberpwn
 */
@Documented
public class FlagLayer
{
	private FlagPattern shape;
	private DyeColor color;

	/**
	 * Create a flag layer
	 * 
	 * @param shape
	 *            the flag pattern
	 * @param color
	 *            the flag color
	 */
	public FlagLayer(FlagPattern shape, DyeColor color)
	{
		this.color = color;
		this.shape = shape;
	}

	/**
	 * Create a flag layer
	 * 
	 * @param pattern
	 *            the bukkit banner pattern
	 */
	public FlagLayer(Pattern pattern)
	{
		this.color = pattern.getColor();
		this.shape = FlagPattern.fromPattern(pattern.getPattern());
	}

	/**
	 * Convert this flag layer to a bukkit pattern
	 * 
	 * @return the pattern
	 */
	public Pattern toPattern()
	{
		return new Pattern(getColor(), getShape().toPatternType());
	}

	/**
	 * Get the flag pattern
	 * 
	 * @return the flag pattern
	 */
	public FlagPattern getShape()
	{
		return shape;
	}

	/**
	 * Set the falg pattern
	 * 
	 * @param shape
	 *            the flag pattern
	 */
	public void setShape(FlagPattern shape)
	{
		this.shape = shape;
	}

	/**
	 * Get the color
	 * 
	 * @return the color
	 */
	public DyeColor getColor()
	{
		return color;
	}

	/**
	 * Set the color
	 * 
	 * @param color
	 *            the color
	 */
	public void setColor(DyeColor color)
	{
		this.color = color;
	}

	@SuppressWarnings("deprecation")
	public String toString()
	{
		return "{Pattern:" + getShape().getCode() + ",Color:" + String.valueOf((int) color.getDyeData()) + "}";
	}
}

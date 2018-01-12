package phantom.flag;

import org.bukkit.block.banner.PatternType;

import phantom.util.metrics.Documented;

/**
 * Flag pattern types
 * 
 * @author cyberpwn
 */
@Documented
public enum FlagPattern
{
	GRADIENT_TOP("gra"),
	GRADIENT_BOTTOM("gru"),
	BRICKS("bri"),
	HALF_TOP("hh"),
	HALF_BOTTOM("hhb"),
	HALF_LEFT("vh"),
	HALF_RIGHT("vhr"),
	THIRD_TOP("ts"),
	THIRD_BOTTOM("bs"),
	THIRD_LEFT("ls"),
	THIRD_RIGHT("rs"),
	TRIANGLE_TOP_LEFT("ld"),
	TRIANGLE_TOP_RIGHT("rud"),
	TRIANGLE_BOTTOM_LEFT("lud"),
	TRIANGLE_BOTTOM_RIGHT("rd"),
	CROSS("cr"),
	FORWARD_SLASH("dls"),
	BACK_SLASH("drs"),
	PLUS("sc"),
	VERTICAL_LINE("cs"),
	HORIZONTAL_LINE("ms"),
	SQUARE_TOP_LEFT("tl"),
	SQUARE_TOP_RIGHT("tr"),
	SQUARE_BOTTOM_LEFT("bl"),
	SQUARE_BOTTOM_RIGHT("br"),
	TRIANGLE_TOP("tt"),
	TRIANGLE_BOTTOM("bt"),
	DIAMOND("mr"),
	CIRCLE("mc"),
	SHINGLE_TOP("tts"),
	SHINGLE_BOTTOM("bts"),
	VERTICAL_LINES("ss"),
	FLAT_BORDER("bo"),
	SHINGLE_BORDER("cbo"),
	SUN_FLOWER("flo"),
	CREEPER("cre"),
	SKULL("sku"),
	MOJANG("moj");

	private String code;
	private PatternType type;

	private FlagPattern(String code)
	{
		this.code = code;

		for(PatternType i : PatternType.values())
		{
			if(i.getIdentifier().equals(code))
			{
				type = i;
			}
		}
	}

	/**
	 * Get the flag code
	 * 
	 * @return the flag code
	 */
	public String getCode()
	{
		return code;
	}

	/**
	 * Get the pattern type
	 * 
	 * @return the pattern type
	 */
	public PatternType toPatternType()
	{
		return type;
	}

	/**
	 * Get the flag pattern from the bukkit pattern type
	 * 
	 * @param pattern
	 *            the pattern
	 * @return the flag pattern
	 */
	public static FlagPattern fromPattern(PatternType pattern)
	{
		for(FlagPattern i : FlagPattern.values())
		{
			if(i.getCode().equals(pattern.getIdentifier()))
			{
				return i;
			}
		}

		return null;
	}
}

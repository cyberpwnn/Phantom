package org.phantomapi.text;

import java.lang.reflect.Field;
import org.bukkit.entity.Player;
import org.phantomapi.util.C;

/**
 * Common utf8 symbols
 * 
 * @author cyberpwn
 */
public class SYM
{
	public static final char QUOTE_LEFT = '\u275D';
	public static final char QUOTE_RIGHT = '\u275E';
	public static final char QUOTE_SINGLE_LEFT = '\u275B';
	public static final char QUOTE_SINGLE_RIGHT = '\u275C';
	
	public static final char DEGREE_CELCIUS = '\u2103';
	public static final char DEGREE_FAHRENHEIT = '\u2109';
	
	public static final char BRACKET_LEFT = '\u2770';
	public static final char BRACKET_RIGHT = '\u2771';
	public static final char BRACKET_SHELL_LEFT = '\u2772';
	public static final char BRACKET_SHELL_RIGHT = '\u2773';
	
	public static final char ARROW_LEFT = '\u2190';
	public static final char ARROW_UP = '\u2191';
	public static final char ARROW_RIGHT = '\u2192';
	public static final char ARROW_DOWN = '\u2193';
	public static final char ARROW_DOUBLE_LEFT = '\u21C7';
	public static final char ARROW_DOUBLE_UP = '\u21C8';
	public static final char ARROW_DOUBLE_RIGHT = '\u21C9';
	public static final char ARROW_DOUBLE_DOWN = '\u21CA';
	public static final char ARROW_LEFT_RIGHT = '\u2194';
	public static final char ARROW_UP_DOWN = '\u2195';
	public static final char ARROW_UP_LEFT = '\u2196';
	public static final char ARROW_UP_RIGHT = '\u2197';
	public static final char ARROW_DOWN_RIGHT = '\u2198';
	public static final char ARROW_DOWN_LEFT = '\u2199';
	
	public static final char SYMBOL_SNOWFLAKE = '\u2744';
	public static final char SYMBOL_HEART = '\u2665';
	public static final char SYMBOL_COPYRIGHT = '\u00A9';
	public static final char SYMBOL_REGISTERED = '\u00AE';
	public static final char SYMBOL_WARNING = '\u26A0';
	public static final char SYMBOL_DIAMOND = '\u2756';
	public static final char SYMBOL_NIB = '\u2712';
	public static final char SYMBOL_CUT = '\u2700';
	public static final char SYMBOL_OHM = '\u2126';
	public static final char SYMBOL_GEAR = '\u2699';
	public static final char SYMBOL_PENCIL = '\u270E';
	public static final char SYMBOL_SMILE = '\u263B';
	public static final char SYMBOL_RADIOACTIVE = '\u2622';
	public static final char SYMBOL_PEACE = '\u262E';
	public static final char SYMBOL_FEMALE = '\u2640';
	public static final char SYMBOL_MALE = '\u2642';
	public static final char SYMBOL_KING = '\u265A';
	public static final char SYNBOL_QUEEN = '\u265B';
	public static final char SYMBOL_CASTLE = '\u265C';
	public static final char SYMBOL_HORSE = '\u265E';
	public static final char SYMBOL_CLOVER = '\u2663';
	public static final char SYMBOL_FLAG = '\u2691';
	public static final char SYMBOL_VOLTAGE = '\u26A1';
	public static final char SYMBOL_EGG = '\u2B2E';
	
	public static final char SHAPE_CIRCLE = '\u25CF';
	public static final char SHAPE_SQUARE = '\u25A0';
	public static final char SHAPE_TRIANGLE = '\u25B2';
	public static final char SHAPE_DIAMOND = '\u25C6';
	public static final char SHAPE_PENTAGON = '\u2B1F';
	public static final char SHAPE_HEXAGON = '\u2B22';
	public static final char SHAPE_HEXAGON_HORIZONTAL = '\u2B23';
	public static final char SHAPE_ELIPSE = '\u2B2E';
	
	/**
	 * Print out the symbols to a player
	 * 
	 * @param p
	 *            the player
	 */
	public static void printSymbols(Player p)
	{
		RTX rtx = new RTX();
		
		for(Field i : SYM.class.getDeclaredFields())
		{
			try
			{
				rtx.addTextHover("" + (char) i.get(null), new RTEX(new ColoredString(C.WHITE, i.getName() + "\n"), new ColoredString(C.RED, (char) i.get(null) + ""), new ColoredString(C.GREEN, (char) i.get(null) + ""), new ColoredString(C.BLUE, (char) i.get(null) + "\n"), new ColoredString(C.GOLD, (char) i.get(null) + ""), new ColoredString(C.YELLOW, (char) i.get(null) + ""), new ColoredString(C.AQUA, (char) i.get(null) + "\n")), C.LIGHT_PURPLE);
			}
			
			catch(Exception e)
			{
				
			}
		}
		
		rtx.tellRawTo(p);
	}
}

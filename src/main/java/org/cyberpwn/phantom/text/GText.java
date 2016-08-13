package org.cyberpwn.phantom.text;

import org.cyberpwn.phantom.util.C;
import org.cyberpwn.phantom.util.F;

/**
 * RawText Wrapper
 * 
 * @author cyberpwn
 */
public class GText
{
	private RawText t;
	
	/**
	 * Creates a text wrapper for raw text objects. Make your changes, then
	 * invoke pack() for finishing up
	 */
	public GText()
	{
		this.t = new RawText();
	}
	
	/**
	 * Adds an element to the json array dynamically adding multiple elements
	 * for multiple colors even with the same hover and command if supplied.
	 * Place chat colors in the string itself.
	 * 
	 * @param s
	 *            the text which may or may not have chat colors in it
	 */
	public void add(String s)
	{
		for(String i : F.colorSplit(s))
		{
			String colors = C.getLastColors(i);
			Boolean bold = i.contains('\u00A7' + C.BOLD.getChar() + "");
			Boolean underline = i.contains('\u00A7' + C.UNDERLINE.getChar() + "");
			Boolean strike = i.contains('\u00A7' + C.STRIKETHROUGH.getChar() + "");
			Boolean magic = i.contains('\u00A7' + C.MAGIC.getChar() + "");
			Boolean italics = i.contains('\u00A7' + C.ITALIC.getChar() + "");
			
			if(colors.length() > 1)
			{
				colors = colors.substring(1, 2);
			}
			
			t.addText(C.stripColor(i), C.getByChar(colors).name(), bold, italics, underline, strike, magic);
		}
	}
	
	/**
	 * Adds an element to the json array dynamically adding multiple elements
	 * for multiple colors even with the same hover and command if supplied.
	 * Place chat colors in the string itself.
	 * 
	 * @param s
	 *            the text which may or may not have chat colors in it
	 * @param hover
	 *            the hover text which may have up to one chat color in it
	 */
	public void addWithHover(String s, String hover)
	{
		String colorsh = C.getLastColors(hover);
		
		for(String i : F.colorSplit(s))
		{
			String colors = C.WHITE.toString();
			
			try
			{
				colors = C.getLastColors(i);
			}
			
			catch(Exception e)
			{
				
			}
			
			Boolean bold = i.contains('\u00A7' + C.BOLD.getChar() + "");
			Boolean underline = i.contains('\u00A7' + C.UNDERLINE.getChar() + "");
			Boolean strike = i.contains('\u00A7' + C.STRIKETHROUGH.getChar() + "");
			Boolean magic = i.contains('\u00A7' + C.MAGIC.getChar() + "");
			Boolean italics = i.contains('\u00A7' + C.ITALIC.getChar() + "");
			
			if(colors.length() > 1)
			{
				colors = colors.substring(1, 2);
			}
			
			if(colorsh.length() > 1)
			{
				colorsh = colorsh.substring(1, 2);
			}
			
			t.addTextWithHover(C.stripColor(i), C.getByChar(colors).name(), C.stripColor(hover), C.getByChar(colorsh).name(), bold, italics, underline, strike, magic);
		}
	}
	
	/**
	 * Adds an element to the json array dynamically adding multiple elements
	 * for multiple colors even with the same hover and command if supplied.
	 * Place chat colors in the string itself.
	 * 
	 * @param s
	 *            the text which may or may not have chat colors in it
	 * @param hover
	 *            the hover text which may have up to one chat color in it
	 * @param command
	 *            the command itself
	 */
	public void addWithHoverCommand(String s, String hover, String command)
	{
		String colorsh = C.getLastColors(hover);
		
		for(String i : F.colorSplit(s))
		{
			String colors = C.getLastColors(i);
			Boolean bold = i.contains('\u00A7' + C.BOLD.getChar() + "");
			Boolean underline = i.contains('\u00A7' + C.UNDERLINE.getChar() + "");
			Boolean strike = i.contains('\u00A7' + C.STRIKETHROUGH.getChar() + "");
			Boolean magic = i.contains('\u00A7' + C.MAGIC.getChar() + "");
			Boolean italics = i.contains('\u00A7' + C.ITALIC.getChar() + "");
			
			if(colors.length() > 1)
			{
				colors = colors.substring(1, 2);
			}
			
			if(colorsh.length() > 1)
			{
				colorsh = colorsh.substring(1, 2);
			}
			
			t.addTextWithHoverCommand(C.stripColor(i), C.getByChar(colors).name(), command, C.stripColor(hover), C.getByChar(colorsh).name(), bold, italics, underline, strike, magic);
		}
	}
	
	/**
	 * Pack the text into a rawtext object
	 * 
	 * @return the rawtext object
	 */
	public RawText pack()
	{
		return t;
	}
}

package org.cyberpwn.phantom.text;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.cyberpwn.phantom.clust.JSONException;
import org.cyberpwn.phantom.clust.JSONObject;
import org.cyberpwn.phantom.util.C;

/**
 * Raw Text json wrapper
 * 
 * @author cyberpwn
 */
public class RawText
{
	private static final String HEAD_TEXT = "text";
	private static final String HEAD_COLOR = "color";
	private static final String HEAD_BOLD = "bold";
	private static final String HEAD_ITALIC = "italic";
	private static final String HEAD_UNDERLINED = "underlined";
	private static final String HEAD_STRIKETHROUGH = "strikethrough";
	private static final String HEAD_OBFUSCATED = "obfuscated";
	private static final String HEAD_CLICK_EVENT = "clickEvent";
	private static final String HEAD_HOVER_EVENT = "hoverEvent";
	private static final String HEAD_ACTION = "action";
	private static final String HEAD_VALUE = "value";
	private static final String HEAD_EXTRA = "extra";
	private static final String HEAD_ACTION_SHOW_TEXT = "show_text";
	private static final String HEAD_ACTION_COMMAND = "run_command";
	
	private ArrayList<JSONObject> components;
	
	/**
	 * Create a new raw text holder
	 */
	public RawText()
	{
		this.components = new ArrayList<JSONObject>();
	}
	
	/**
	 * Add Text with no color to the object
	 * 
	 * @param text
	 *            the text
	 * @return this
	 */
	public RawText addText(String text)
	{
		return addText(text, C.WHITE.name());
	}
	
	/**
	 * Add Raw text with the specified color
	 * 
	 * @param text
	 *            the text
	 * @param color
	 *            the color
	 * @return this
	 */
	public RawText addText(String text, String color)
	{
		return addText(text, color, false, false, false, false, false);
	}
	
	/**
	 * Add raw text to the object with data
	 * 
	 * @param text
	 *            the text
	 * @param color
	 *            the color
	 * @param bold
	 *            if bold
	 * @param italic
	 *            if italic
	 * @param underlined
	 *            if underlined
	 * @param strikethrough
	 *            if striked
	 * @param obfuscated
	 *            if magic
	 * @return this
	 */
	public RawText addText(String text, String color, boolean bold, boolean italic, boolean underlined, boolean strikethrough, boolean obfuscated)
	{
		try
		{
			JSONObject object = new JSONObject();
			
			object.put(HEAD_TEXT, text);
			
			object.put(HEAD_COLOR, color.toLowerCase());
			
			if(bold)
			{
				object.put(HEAD_BOLD, true);
			}
			
			if(italic)
			{
				object.put(HEAD_ITALIC, true);
			}
			
			if(underlined)
			{
				object.put(HEAD_UNDERLINED, true);
			}
			
			if(strikethrough)
			{
				object.put(HEAD_STRIKETHROUGH, true);
			}
			
			if(obfuscated)
			{
				object.put(HEAD_OBFUSCATED, true);
			}
			
			components.add(object);
		}
		
		catch(JSONException e)
		{
			e.printStackTrace();
		}
		
		return this;
	}
	
	/**
	 * Add text with a command
	 * 
	 * @param text
	 *            the text
	 * @param color
	 *            the color
	 * @param command
	 *            the command
	 * @return this
	 */
	public RawText addTextWithCommand(String text, String color, String command)
	{
		return addTextWithCommand(text, color, command, false, false, false, false, false);
	}
	
	/**
	 * Add text with data and a command
	 * 
	 * @param text
	 *            the text
	 * @param color
	 *            the color
	 * @param command
	 *            the command
	 * @param bold
	 *            bolded
	 * @param italic
	 *            italics
	 * @param underlined
	 *            underline it
	 * @param strikethrough
	 *            strike it
	 * @param obfuscated
	 *            witchcraft
	 * @return this
	 */
	public RawText addTextWithCommand(String text, String color, String command, boolean bold, boolean italic, boolean underlined, boolean strikethrough, boolean obfuscated)
	{
		try
		{
			JSONObject object = new JSONObject();
			
			object.put(HEAD_TEXT, text);
			
			object.put(HEAD_COLOR, color.toLowerCase());
			
			if(bold)
			{
				object.put(HEAD_BOLD, true);
			}
			
			if(italic)
			{
				object.put(HEAD_ITALIC, true);
			}
			
			if(underlined)
			{
				object.put(HEAD_UNDERLINED, true);
			}
			
			if(strikethrough)
			{
				object.put(HEAD_STRIKETHROUGH, true);
			}
			
			if(obfuscated)
			{
				object.put(HEAD_OBFUSCATED, true);
			}
			
			object.put(HEAD_CLICK_EVENT, new JSONObject().put(HEAD_ACTION, HEAD_ACTION_COMMAND).put(HEAD_VALUE, command));
			
			components.add(object);
		}
		
		catch(JSONException e)
		{
			e.printStackTrace();
		}
		
		return this;
	}
	
	/**
	 * Add text with a hover
	 * 
	 * @param text
	 *            the text
	 * @param color
	 *            the color
	 * @param hoverText
	 *            the hovering text
	 * @param hoverColor
	 *            the hovering color
	 * @return this
	 */
	public RawText addTextWithHover(String text, String color, String hoverText, String hoverColor)
	{
		return addTextWithHover(text, color, hoverText, hoverColor, false, false, false, false, false);
	}
	
	/**
	 * Add text with hover and data
	 * 
	 * @param text
	 *            the text
	 * @param color
	 *            the color
	 * @param hoverText
	 *            the hover text
	 * @param hoverColor
	 *            the hover color
	 * @param bold
	 *            bolded
	 * @param italic
	 *            italics
	 * @param underlined
	 *            underlines
	 * @param strikethrough
	 *            strikes
	 * @param obfuscated
	 *            witchcraft
	 * @return this
	 */
	public RawText addTextWithHover(String text, String color, String hoverText, String hoverColor, boolean bold, boolean italic, boolean underlined, boolean strikethrough, boolean obfuscated)
	{
		try
		{
			JSONObject object = new JSONObject();
			
			object.put(HEAD_TEXT, text);
			
			object.put(HEAD_COLOR, color.toLowerCase());
			
			if(bold)
			{
				object.put(HEAD_BOLD, true);
			}
			
			if(italic)
			{
				object.put(HEAD_ITALIC, true);
			}
			
			if(underlined)
			{
				object.put(HEAD_UNDERLINED, true);
			}
			
			if(strikethrough)
			{
				object.put(HEAD_STRIKETHROUGH, true);
			}
			
			if(obfuscated)
			{
				object.put(HEAD_OBFUSCATED, true);
			}
			
			JSONObject[] dummy = new JSONObject[1];
			dummy[0] = new JSONObject().put(HEAD_TEXT, hoverText).put(HEAD_COLOR, hoverColor.toLowerCase());
			object.put(HEAD_HOVER_EVENT, new JSONObject().put(HEAD_ACTION, HEAD_ACTION_SHOW_TEXT).put(HEAD_VALUE, new JSONObject().put(HEAD_TEXT, "").put(HEAD_EXTRA, dummy)));
			
			components.add(object);
		}
		
		catch(JSONException e)
		{
			e.printStackTrace();
		}
		
		return this;
	}
	
	/**
	 * Add text with a hover and command
	 * 
	 * @param text
	 *            text
	 * @param color
	 *            the color
	 * @param command
	 *            the command
	 * @param hoverText
	 *            the hover text
	 * @param hoverColor
	 *            the hover text color
	 * @return this
	 */
	public RawText addTextWithHoverCommand(String text, String color, String command, String hoverText, String hoverColor)
	{
		return addTextWithHoverCommand(text, color, command, hoverText, hoverColor, false, false, false, false, false);
	}
	
	/**
	 * Add raw text with hovers commands and data
	 * 
	 * @param text
	 *            the text
	 * @param color
	 *            the color
	 * @param command
	 *            the command
	 * @param hoverText
	 *            the hover text
	 * @param hoverColor
	 *            the hover text color
	 * @param bold
	 *            bolded
	 * @param italic
	 *            italics
	 * @param underlined
	 *            underline it
	 * @param strikethrough
	 *            strike it
	 * @param obfuscated
	 *            dark witchcraft
	 * @return this
	 */
	public RawText addTextWithHoverCommand(String text, String color, String command, String hoverText, String hoverColor, boolean bold, boolean italic, boolean underlined, boolean strikethrough, boolean obfuscated)
	{
		try
		{
			JSONObject object = new JSONObject();
			
			object.put(HEAD_TEXT, text);
			
			object.put(HEAD_COLOR, color.toLowerCase());
			
			if(bold)
			{
				object.put(HEAD_BOLD, true);
			}
			
			if(italic)
			{
				object.put(HEAD_ITALIC, true);
			}
			
			if(underlined)
			{
				object.put(HEAD_UNDERLINED, true);
			}
			
			if(strikethrough)
			{
				object.put(HEAD_STRIKETHROUGH, true);
			}
			
			if(obfuscated)
			{
				object.put(HEAD_OBFUSCATED, true);
			}
			
			object.put(HEAD_CLICK_EVENT, new JSONObject().put(HEAD_ACTION, HEAD_ACTION_COMMAND).put(HEAD_VALUE, command));
			
			JSONObject[] dummy = new JSONObject[1];
			
			dummy[0] = new JSONObject().put(HEAD_TEXT, hoverText).put(HEAD_COLOR, hoverColor.toLowerCase());
			object.put(HEAD_HOVER_EVENT, new JSONObject().put(HEAD_ACTION, HEAD_ACTION_SHOW_TEXT).put(HEAD_VALUE, new JSONObject().put(HEAD_TEXT, "").put(HEAD_EXTRA, dummy)));
			
			components.add(object);
		}
		
		catch(JSONException e)
		{
			e.printStackTrace();
		}
		
		return this;
	}
	
	/**
	 * Cram it into a json string
	 * 
	 * @return the json string
	 */
	public String compile()
	{
		String base = "[\"\"";
		for(JSONObject i : components)
		{
			base = base + "," + i.toString();
		}
		
		return base + "]";
	}
	
	/**
	 * Tell raw to a player
	 * 
	 * @param p
	 *            the player
	 */
	public void tellRawTo(Player p)
	{
		Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "tellraw " + p.getName() + " " + compile());
	}
}

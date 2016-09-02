package org.phantomapi.text;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.phantomapi.clust.JSONArray;
import org.phantomapi.clust.JSONObject;
import org.phantomapi.util.C;
import org.phantomapi.util.S;

/**
 * Raw Text holder
 * 
 * @author cyberpwn
 */
public class RTX
{
	private JSONArray base;
	
	/**
	 * Create a base raw text holder
	 */
	public RTX()
	{
		this.base = new JSONArray();
		base.put("");
	}
	
	/**
	 * Add manual json (helper)
	 * 
	 * @param object
	 *            the json object
	 */
	public void add(JSONObject object)
	{
		base.put(object);
	}
	
	/**
	 * Add basic text
	 * 
	 * @param text
	 *            the text
	 */
	public void addText(String text)
	{
		JSONObject js = new JSONObject();
		js.put("text", text);
		js.put("color", "none");
		add(js);
	}
	
	/**
	 * Add Text with color
	 * 
	 * @param text
	 *            the text
	 * @param color
	 *            the color
	 */
	public void addText(String text, C color)
	{
		JSONObject js = new JSONObject();
		js.put("text", text);
		js.put("color", color.name().toLowerCase());
		add(js);
	}
	
	/**
	 * Add Text with color and formatting
	 * 
	 * @param text
	 *            the text
	 * @param color
	 *            the color
	 * @param formatting
	 *            the formatting
	 */
	public void addText(String text, C color, C... formatting)
	{
		JSONObject js = new JSONObject();
		js.put("text", text);
		js.put("color", color.name().toLowerCase());
		
		for(C i : formatting)
		{
			if(i.isFormat())
			{
				if(i.equals(C.BOLD))
				{
					js.put("bold", true);
				}
				
				if(i.equals(C.ITALIC))
				{
					js.put("italic", true);
				}
				
				if(i.equals(C.UNDERLINE))
				{
					js.put("underlined", true);
				}
				
				if(i.equals(C.STRIKETHROUGH))
				{
					js.put("strikethrough", true);
				}
				
				if(i.equals(C.MAGIC))
				{
					js.put("obfuscated", true);
				}
			}
		}
		
		add(js);
	}
	
	/**
	 * Add a text with RTEX hovers
	 * 
	 * @param text
	 *            the text
	 * @param hover
	 *            the hover RTEX
	 * @param color
	 *            the color
	 * @param formatting
	 *            the formatting
	 */
	public void addTextHover(String text, RTEX hover, C color, C... formatting)
	{
		JSONObject js = new JSONObject();
		js.put("text", text);
		js.put("color", color.name().toLowerCase());
		
		for(C i : formatting)
		{
			if(i.isFormat())
			{
				if(i.equals(C.BOLD))
				{
					js.put("bold", true);
				}
				
				if(i.equals(C.ITALIC))
				{
					js.put("italic", true);
				}
				
				if(i.equals(C.UNDERLINE))
				{
					js.put("underlined", true);
				}
				
				if(i.equals(C.STRIKETHROUGH))
				{
					js.put("strikethrough", true);
				}
				
				if(i.equals(C.MAGIC))
				{
					js.put("obfuscated", true);
				}
			}
		}
		
		JSONObject hoverEvent = new JSONObject();
		hoverEvent.put("action", "show_text");
		hoverEvent.put("value", hover.toJSON());
		js.put("hoverEvent", hoverEvent);
		add(js);
	}
	
	/**
	 * Add a command Suggestion
	 * 
	 * @param text
	 *            the text
	 * @param cmd
	 *            the suggestion command
	 * @param color
	 *            the color
	 * @param formatting
	 *            the formatting
	 */
	public void addTextSuggestedCommand(String text, String cmd, C color, C... formatting)
	{
		JSONObject js = new JSONObject();
		js.put("text", text);
		js.put("color", color.name().toLowerCase());
		
		JSONObject clickEvent = new JSONObject();
		clickEvent.put("action", "suggest_command");
		clickEvent.put("value", cmd);
		js.put("clickEvent", clickEvent);
		
		for(C i : formatting)
		{
			if(i.isFormat())
			{
				if(i.equals(C.BOLD))
				{
					js.put("bold", true);
				}
				
				if(i.equals(C.ITALIC))
				{
					js.put("italic", true);
				}
				
				if(i.equals(C.UNDERLINE))
				{
					js.put("underlined", true);
				}
				
				if(i.equals(C.STRIKETHROUGH))
				{
					js.put("strikethrough", true);
				}
				
				if(i.equals(C.MAGIC))
				{
					js.put("obfuscated", true);
				}
			}
		}
		
		add(js);
	}
	
	/**
	 * Add an open url clickable text
	 * 
	 * @param text
	 *            the text
	 * @param url
	 *            the url
	 * @param color
	 *            the color
	 * @param formatting
	 *            the formatting
	 */
	public void addTextOpenURL(String text, String url, C color, C... formatting)
	{
		JSONObject js = new JSONObject();
		js.put("text", text);
		js.put("color", color.name().toLowerCase());
		
		JSONObject clickEvent = new JSONObject();
		clickEvent.put("action", "open_url");
		clickEvent.put("value", url);
		js.put("clickEvent", clickEvent);
		
		for(C i : formatting)
		{
			if(i.isFormat())
			{
				if(i.equals(C.BOLD))
				{
					js.put("bold", true);
				}
				
				if(i.equals(C.ITALIC))
				{
					js.put("italic", true);
				}
				
				if(i.equals(C.UNDERLINE))
				{
					js.put("underlined", true);
				}
				
				if(i.equals(C.STRIKETHROUGH))
				{
					js.put("strikethrough", true);
				}
				
				if(i.equals(C.MAGIC))
				{
					js.put("obfuscated", true);
				}
			}
		}
		
		add(js);
	}
	
	/**
	 * Add a run command text
	 * 
	 * @param text
	 *            the text
	 * @param cmd
	 *            the command
	 * @param color
	 *            the color
	 * @param formatting
	 *            the formatting
	 */
	public void addTextFireCommand(String text, String cmd, C color, C... formatting)
	{
		JSONObject js = new JSONObject();
		js.put("text", text);
		js.put("color", color.name().toLowerCase());
		
		JSONObject clickEvent = new JSONObject();
		clickEvent.put("action", "run_command");
		clickEvent.put("value", cmd);
		js.put("clickEvent", clickEvent);
		
		for(C i : formatting)
		{
			if(i.isFormat())
			{
				if(i.equals(C.BOLD))
				{
					js.put("bold", true);
				}
				
				if(i.equals(C.ITALIC))
				{
					js.put("italic", true);
				}
				
				if(i.equals(C.UNDERLINE))
				{
					js.put("underlined", true);
				}
				
				if(i.equals(C.STRIKETHROUGH))
				{
					js.put("strikethrough", true);
				}
				
				if(i.equals(C.MAGIC))
				{
					js.put("obfuscated", true);
				}
			}
		}
		
		add(js);
	}
	
	/**
	 * Add a text suggestion clickable piece of text with hover text
	 * 
	 * @param text
	 *            the text
	 * @param hover
	 *            the hover
	 * @param cmd
	 *            the command
	 * @param color
	 *            the color
	 * @param formatting
	 *            the formatting
	 */
	public void addTextSuggestedHoverCommand(String text, RTEX hover, String cmd, C color, C... formatting)
	{
		JSONObject js = new JSONObject();
		js.put("text", text);
		js.put("color", color.name().toLowerCase());
		
		JSONObject clickEvent = new JSONObject();
		clickEvent.put("action", "suggest_command");
		clickEvent.put("value", cmd);
		js.put("clickEvent", clickEvent);
		
		for(C i : formatting)
		{
			if(i.isFormat())
			{
				if(i.equals(C.BOLD))
				{
					js.put("bold", true);
				}
				
				if(i.equals(C.ITALIC))
				{
					js.put("italic", true);
				}
				
				if(i.equals(C.UNDERLINE))
				{
					js.put("underlined", true);
				}
				
				if(i.equals(C.STRIKETHROUGH))
				{
					js.put("strikethrough", true);
				}
				
				if(i.equals(C.MAGIC))
				{
					js.put("obfuscated", true);
				}
			}
		}
		
		JSONObject hoverEvent = new JSONObject();
		hoverEvent.put("action", "show_text");
		hoverEvent.put("value", hover.toJSON());
		js.put("hoverEvent", hoverEvent);
		add(js);
	}
	
	/**
	 * Add a url text with a hover event
	 * 
	 * @param text
	 *            the text
	 * @param hover
	 *            the hover
	 * @param url
	 *            the url
	 * @param color
	 *            the color
	 * @param formatting
	 *            the color formatting
	 */
	public void addTextOpenHoverURL(String text, RTEX hover, String url, C color, C... formatting)
	{
		JSONObject js = new JSONObject();
		js.put("text", text);
		js.put("color", color.name().toLowerCase());
		
		JSONObject clickEvent = new JSONObject();
		clickEvent.put("action", "open_url");
		clickEvent.put("value", url);
		js.put("clickEvent", clickEvent);
		
		for(C i : formatting)
		{
			if(i.isFormat())
			{
				if(i.equals(C.BOLD))
				{
					js.put("bold", true);
				}
				
				if(i.equals(C.ITALIC))
				{
					js.put("italic", true);
				}
				
				if(i.equals(C.UNDERLINE))
				{
					js.put("underlined", true);
				}
				
				if(i.equals(C.STRIKETHROUGH))
				{
					js.put("strikethrough", true);
				}
				
				if(i.equals(C.MAGIC))
				{
					js.put("obfuscated", true);
				}
			}
		}
		
		JSONObject hoverEvent = new JSONObject();
		hoverEvent.put("action", "show_text");
		hoverEvent.put("value", hover.toJSON());
		js.put("hoverEvent", hoverEvent);
		add(js);
	}
	
	/**
	 * Add a text fire event with hover
	 * 
	 * @param text
	 *            the text
	 * @param hover
	 *            the hover rtex
	 * @param cmd
	 *            the command
	 * @param color
	 *            the color
	 * @param formatting
	 *            the formatting
	 */
	public void addTextFireHoverCommand(String text, RTEX hover, String cmd, C color, C... formatting)
	{
		JSONObject js = new JSONObject();
		js.put("text", text);
		js.put("color", color.name().toLowerCase());
		
		JSONObject clickEvent = new JSONObject();
		clickEvent.put("action", "run_command");
		clickEvent.put("value", cmd);
		js.put("clickEvent", clickEvent);
		
		for(C i : formatting)
		{
			if(i.isFormat())
			{
				if(i.equals(C.BOLD))
				{
					js.put("bold", true);
				}
				
				if(i.equals(C.ITALIC))
				{
					js.put("italic", true);
				}
				
				if(i.equals(C.UNDERLINE))
				{
					js.put("underlined", true);
				}
				
				if(i.equals(C.STRIKETHROUGH))
				{
					js.put("strikethrough", true);
				}
				
				if(i.equals(C.MAGIC))
				{
					js.put("obfuscated", true);
				}
			}
		}
		
		JSONObject hoverEvent = new JSONObject();
		hoverEvent.put("action", "show_text");
		hoverEvent.put("value", hover.toJSON());
		js.put("hoverEvent", hoverEvent);
		add(js);
	}
	
	/**
	 * Cram it into json
	 * 
	 * @return the json
	 */
	public JSONArray toJSON()
	{
		return base;
	}
	
	/**
	 * Tell raw to a player (ASYNC SAFE)
	 * 
	 * @param p
	 *            the player
	 */
	public void tellRawTo(Player p)
	{
		new S()
		{
			@Override
			public void sync()
			{
				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "tellraw " + p.getName() + " " + toJSON().toString());
			}
		};
	}
}

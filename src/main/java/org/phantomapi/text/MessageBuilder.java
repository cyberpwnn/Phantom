package org.phantomapi.text;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.phantomapi.util.C;

/**
 * Message Builder with formatting tags
 * 
 * @author cyberpwn
 */
public class MessageBuilder
{
	private String tag;
	private String hoverText;
	
	/**
	 * Create an empty message builder.
	 */
	public MessageBuilder()
	{
		this.tag = "";
		this.hoverText = "";
	}
	
	/**
	 * Create a message builder from a tag provider
	 * 
	 * @param tagProvider
	 *            the tag provider
	 */
	public MessageBuilder(TagProvider tagProvider)
	{
		this.tag = tagProvider.getChatTag();
		this.hoverText = tagProvider.getChatTagHover();
	}
	
	/**
	 * Send the message to a sender with text. Tags and hovers are added.
	 * 
	 * @param sender
	 *            the sender
	 * @param text
	 *            the text
	 */
	public void message(CommandSender sender, String text)
	{
		if(sender instanceof Player)
		{
			GText t = new GText();
			
			if(hoverText != null && hoverText.length() > 0)
			{
				t.addWithHover(tag, hoverText);
			}
			
			else
			{
				t.add(tag);
			}
			
			try
			{
				t.add(C.getLastColors(tag) + text);
			}
			
			catch(Exception e)
			{
				t.add(C.WHITE + text);
			}
			
			t.pack().tellRawTo((Player) sender);
		}
		
		else
		{
			sender.sendMessage(tag + text);
		}
	}
	
	/**
	 * Set the tag
	 * 
	 * @param tag
	 *            the tag
	 * @return the builder (this)
	 */
	public MessageBuilder setTag(String tag)
	{
		this.tag = tag;
		return this;
	}
	
	/**
	 * Set the tag and hover
	 * 
	 * @param tag
	 *            the tag
	 * @param hover
	 *            the hover (for the tag)
	 * @return this
	 */
	public MessageBuilder setTag(String tag, String hover)
	{
		setTag(tag);
		hoverText = hover;
		
		return this;
	}

	public String getHoverText()
	{
		return hoverText;
	}

	public void setHoverText(String hoverText)
	{
		this.hoverText = hoverText;
	}

	public String getTag()
	{
		return tag;
	}
}

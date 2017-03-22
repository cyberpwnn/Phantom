package org.phantomapi.tag;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.phantomapi.lang.GList;
import org.phantomapi.nms.NMSX;

public class TaggedPlayer
{
	private final Player player;
	private final TagBuilder tagBuilder;
	private boolean tagged;
	private String name;
	private String contextual;
	private final GList<String> content;
	
	public TaggedPlayer(Player player)
	{
		this.player = player;
		tagged = false;
		name = player.getName();
		content = new GList<String>();
		contextual = null;
		tagBuilder = new TagBuilder(player);
	}
	
	public void update()
	{
		if(tagged)
		{
			tagBuilder.getContext().clear();
			tagBuilder.getContext().add(name);
			tagBuilder.getContext().add(content);
			
			if(contextual != null)
			{
				tagBuilder.getContext().add(contextual);
			}
			
			tagBuilder.update();
		}
	}
	
	public void showContext(Player p)
	{
		NMSX.showEntity(p, getEntity(contextual));
	}
	
	public void hideContext(Player p)
	{
		NMSX.hideEntity(p, getEntity(contextual));
	}
	
	public void showContent(Player p)
	{
		for(String i : content)
		{
			NMSX.showEntity(p, getEntity(i));
		}
	}
	
	public void hideContent(Player p)
	{
		for(String i : content)
		{
			NMSX.hideEntity(p, getEntity(i));
		}
	}
	
	public Entity getEntity(String content)
	{
		return tagBuilder.getLocks().get(content);
	}
	
	public boolean isTagged()
	{
		return tagged;
	}
	
	public void setTagged(boolean tagged)
	{
		this.tagged = tagged;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getContextual()
	{
		return contextual;
	}
	
	public void setContextual(String contextual)
	{
		this.contextual = contextual;
	}
	
	public GList<String> getContent()
	{
		return content;
	}
	
	public Player getPlayer()
	{
		return player;
	}
	
	public TagBuilder getTagBuilder()
	{
		return tagBuilder;
	}
}

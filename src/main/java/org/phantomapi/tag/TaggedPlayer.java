package org.phantomapi.tag;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.phantomapi.entity.PEntity;
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
	private final GList<String> staticContent;
	private Location request;
	
	public TaggedPlayer(Player player)
	{
		this.player = player;
		request = null;
		tagged = false;
		name = player.getName();
		content = new GList<String>();
		staticContent = new GList<String>();
		contextual = null;
		tagBuilder = new TagBuilder(player, this);
	}
	
	public void update()
	{
		while(getContent().size() > 6)
		{
			getContent().remove(0);
		}
		
		while(getStaticContent().size() > 6)
		{
			getStaticContent().remove(0);
		}
		
		if(request != null)
		{
			tagBuilder.destroyContext();
			new PEntity(player).clearPassengers();
			player.setPassenger(null);
			player.teleport(request);
			request = null;
		}
		
		if(tagged)
		{
			tagBuilder.getContext().clear();
			tagBuilder.getContext().add(name);
			tagBuilder.getContext().add(staticContent);
			tagBuilder.getContext().add(content);
			
			if(contextual != null)
			{
				tagBuilder.getContext().add(contextual);
			}
			
			tagBuilder.update();
		}
		
		else
		{
			tagBuilder.destroyContext();
		}
	}
	
	public void showContext(Player p)
	{
		if(!tagged)
		{
			return;
		}
		
		NMSX.showEntity(p, getEntity(contextual));
	}
	
	public void hideContext(Player p)
	{
		if(!tagged)
		{
			return;
		}
		
		NMSX.hideEntity(p, getEntity(contextual));
	}
	
	public void showContent(Player p)
	{
		if(!tagged)
		{
			return;
		}
		
		for(String i : content)
		{
			NMSX.showEntity(p, getEntity(i));
		}
	}
	
	public void hideContent(Player p)
	{
		if(!tagged)
		{
			return;
		}
		
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
	
	public void requestTeleport(Location l)
	{
		request = l;
	}
	
	public TagBuilder getTagBuilder()
	{
		return tagBuilder;
	}
	
	public GList<String> getStaticContent()
	{
		return staticContent;
	}
	
	public boolean hasContent()
	{
		return isTagged() && !getContent().isEmpty();
	}
	
	public boolean hasContext()
	{
		return isTagged() && getContextual() != null;
	}
	
	public boolean hasStaticContent()
	{
		return isTagged() && !getStaticContent().isEmpty();
	}
}

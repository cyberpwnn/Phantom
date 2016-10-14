package org.phantomapi.wraith;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.phantomapi.Phantom;
import org.phantomapi.lang.GList;
import org.phantomapi.text.MessageBuilder;
import org.phantomapi.text.TagProvider;
import org.phantomapi.util.C;
import org.phantomapi.world.Area;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.Equipment;
import net.citizensnpcs.api.trait.trait.Equipment.EquipmentSlot;

/**
 * Phantom NPC Wrapper implementation
 * 
 * @author cyberpwn
 */
public class PhantomNPCWrapper implements NPCWrapper, TagProvider
{
	private NPC npc;
	private String chatName;
	private String chatHover;
	private MessageBuilder mb;
	private WraithTarget target;
	private WraithTarget focus;
	private Boolean aggressive;
	
	public PhantomNPCWrapper(String name)
	{
		this.npc = null;
		this.npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, name + C.BLACK + "^");
		this.chatName = npc.getName() + ": ";
		this.chatHover = "Hi, I'm " + npc.getName();
		this.mb = new MessageBuilder(this);
		this.target = null;
		this.focus = null;
		this.aggressive = false;
	}
	
	@Override
	public void spawn(Location location)
	{
		npc.spawn(location);
	}
	
	@Override
	public void despawn()
	{
		npc.despawn();
	}
	
	@Override
	public boolean isSpawned()
	{
		return npc.isSpawned();
	}
	
	@Override
	public Location getLocation()
	{
		return npc.getStoredLocation();
	}
	
	@Override
	public void teleport(Location location)
	{
		if(isSpawned())
		{
			npc.teleport(location, TeleportCause.PLUGIN);
		}
	}
	
	@Override
	public Entity getEntity()
	{
		if(isSpawned())
		{
			return npc.getEntity();
		}
		
		return null;
	}
	
	@Override
	public int getEntityId()
	{
		if(isSpawned())
		{
			return getEntity().getEntityId();
		}
		
		return -1;
	}
	
	@Override
	public void setTarget(Location location)
	{
		setTarget(new WraithTarget(location));
	}
	
	@Override
	public void setTarget(Entity entity)
	{
		setTarget(new WraithTarget(entity));
	}
	
	@Override
	public void setFocus(WraithTarget target)
	{
		focus = target;
	}
	
	@Override
	public WraithTarget getFocus()
	{
		return focus;
	}
	
	@Override
	public void setEquipment(WraithEquipment slot, ItemStack item)
	{
		if(isSpawned())
		{
			EquipmentSlot s = EquipmentSlot.valueOf(slot.toString());
			npc.getTrait(Equipment.class).set(s, item);
		}
	}
	
	@Override
	public ItemStack getEquipment(WraithEquipment slot)
	{
		if(isSpawned())
		{
			EquipmentSlot s = EquipmentSlot.valueOf(slot.toString());
			return npc.getTrait(Equipment.class).get(s);
		}
		
		return null;
	}
	
	@Override
	public String getName()
	{
		return npc.getName();
	}
	
	@Override
	public void setName(String name)
	{
		name = name + C.BLACK + "^";
		npc.setName(name);
		chatName = npc.getName() + ": ";
		chatHover = "Hi, I'm " + npc.getName();
		mb = new MessageBuilder(this);
	}
	
	@Override
	public void setSneaking(boolean sneaking)
	{
		getPlayer().setSneaking(sneaking);
	}
	
	@Override
	public boolean isSneaking()
	{
		return getPlayer().isSneaking();
	}
	
	@Override
	public void setSprinting(boolean sprinting)
	{
		getPlayer().setSprinting(sprinting);
	}
	
	@Override
	public boolean isSprinting()
	{
		return getPlayer().isSprinting();
	}
	
	@Override
	public void allowFlight(boolean flightFinding)
	{
		npc.setFlyable(flightFinding);
	}
	
	@Override
	public Player getPlayer()
	{
		return (Player) getEntity();
	}
	
	@Override
	public boolean isAllowedFlight()
	{
		return npc.isFlyable();
	}
	
	@Override
	public void setProtected(boolean protect)
	{
		npc.setProtected(protect);
	}
	
	@Override
	public boolean isProtected()
	{
		return npc.isProtected();
	}
	
	@Override
	public boolean hasTarget()
	{
		return npc.getNavigator().isNavigating() || target != null;
	}
	
	@Override
	public void clearTarget()
	{
		npc.getNavigator().cancelNavigation();
		target = null;
	}
	
	@Override
	public void say(String message)
	{
		for(Player i : Phantom.instance().onlinePlayers())
		{
			say(message, i);
		}
	}
	
	@Override
	public void say(String message, double radius)
	{
		Area a = new Area(getLocation(), radius);
		
		for(Player i : a.getNearbyPlayers())
		{
			say(message, i);
		}
	}
	
	@Override
	public void say(String message, Player p)
	{
		if(isSpawned())
		{
			if(WraithUtil.isWraith(p))
			{
				return;
			}
			
			mb.message(p, C.RESET + message);
		}
	}
	
	@Override
	public void say(String message, Player... players)
	{
		for(Player i : players)
		{
			say(message, i);
		}
	}
	
	@Override
	public void say(String message, GList<Player> players)
	{
		say(message, players.toArray(new Player[players.size()]));
	}
	
	@Override
	public String getChatTag()
	{
		return chatName;
	}
	
	@Override
	public String getChatTagHover()
	{
		return chatHover;
	}
	
	@Override
	public String getChatName()
	{
		return getChatTag();
	}
	
	@Override
	public String getChatHover()
	{
		return getChatHover();
	}
	
	@Override
	public void setChatName(String name)
	{
		chatName = name;
	}
	
	@Override
	public void setChatHover(String hover)
	{
		chatHover = hover;
	}
	
	@Override
	public WraithTarget getTarget()
	{
		return target;
	}
	
	@Override
	public void tick()
	{
		if(isSpawned())
		{
			if(focus != null)
			{
				if(focus.getTarget() == null)
				{
					focus = null;
				}
			}
			
			if(target != null)
			{
				if(target.getTarget() == null)
				{
					target = null;
				}
			}
			
			onTick();
			updateTarget();
			updateFocus();
		}
	}
	
	@Override
	public void onTick()
	{
		
	}
	
	@Override
	public void updateTarget()
	{
		if(hasTarget())
		{
			npc.getNavigator().setTarget(getTarget().getTarget());
		}
	}
	
	@Override
	public void updateFocus()
	{
		if(hasFocus())
		{
			lookAt(getFocus().getTarget());
		}
	}
	
	@Override
	public void lookAt(Location location)
	{
		npc.faceLocation(location);
	}
	
	@Override
	public boolean hasFocus()
	{
		return focus != null;
	}
	
	@Override
	public void setTarget(WraithTarget target)
	{
		this.target = target;
	}
	
	@Override
	public void setFocus(Location location)
	{
		setFocus(new WraithTarget(location));
	}
	
	@Override
	public void setFocus(Entity entity)
	{
		setFocus(new WraithTarget(entity));
	}
	
	@Override
	public void clearEquipment()
	{
		for(WraithEquipment i : WraithEquipment.values())
		{
			setEquipment(i, null);
		}
	}
	
	@Override
	public void onInteract(Player p)
	{
		
	}
	
	@Override
	public void onCollide(Entity p)
	{
		
	}
	
	@Override
	public void onDamage(Entity damager, double damage)
	{
		
	}
	
	@Override
	public void setAggressive(boolean aggro)
	{
		aggressive = true;
	}
	
	@Override
	public boolean isAggressive()
	{
		return aggressive;
	}
	
	@Override
	public void destroy()
	{
		npc.destroy();
	}
}

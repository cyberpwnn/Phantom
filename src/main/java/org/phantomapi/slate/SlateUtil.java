package org.phantomapi.slate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.phantomapi.lang.GList;
import org.phantomapi.nms.NMSX;
import org.phantomapi.nms.Tab;
import org.phantomapi.nms.TabItem;

/**
 * Scoreboard & Tab utilities
 * 
 * @author cyberpwn
 */
public class SlateUtil
{
	private static final Map<UUID, Tab> slateMap = new ConcurrentHashMap<UUID, Tab>();
	
	/**
	 * Get the board manager
	 * 
	 * @return the manager
	 */
	public static ScoreboardManager getManager()
	{
		return Bukkit.getScoreboardManager();
	}
	
	/**
	 * Create a new board
	 * 
	 * @return the board
	 */
	public static Scoreboard newBoard()
	{
		return getManager().getNewScoreboard();
	}
	
	/**
	 * Create a new objective
	 * 
	 * @param board
	 *            the binding board
	 * @param name
	 *            the name of the objective
	 * @return the objective
	 */
	public static Objective newObjective(Scoreboard board, String name)
	{
		Objective o = board.registerNewObjective("slate", "dummy");
		
		if(name.length() > 32)
		{
			name = name.substring(0, 29) + "...";
		}
		
		o.setDisplayName(name);
		o.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		return o;
	}
	
	/**
	 * Create a new objective
	 * 
	 * @param board
	 *            the binding board
	 * @param name
	 *            the name of the objective
	 * @return the objective
	 */
	public static Objective newTabObjective(Scoreboard board, String name)
	{
		Objective o = board.registerNewObjective("slate", "dummy");
		
		if(name.length() > 32)
		{
			name = name.substring(0, 29) + "...";
		}
		
		o.setDisplayName(name);
		o.setDisplaySlot(DisplaySlot.PLAYER_LIST);
		
		return o;
	}
	
	/**
	 * Set the score with text
	 * 
	 * @param name
	 *            the name
	 * @param value
	 *            the value
	 * @param o
	 *            the objective
	 */
	public static void setScore(String name, int value, Objective o)
	{
		if(name.length() > 40)
		{
			name = name.substring(0, 37) + "...";
		}
		
		o.getScore(name).setScore(value);
	}
	
	/**
	 * Build a custom slate
	 * 
	 * @param name
	 *            the name of it
	 * @param data
	 *            the list of data
	 * @return the slate
	 */
	public static Scoreboard buildSlate(String name, GList<String> data)
	{
		Scoreboard slate = newBoard();
		Objective o = newObjective(slate, name);
		
		int ind = data.size();
		
		for(String i : data)
		{
			setScore(i, ind, o);
			
			ind--;
		}
		
		return slate;
	}
	
	/**
	 * Build a custom slate
	 * 
	 * @param name
	 *            the name of it
	 * @param data
	 *            the list of data
	 * @return the slate
	 */
	public static Scoreboard buildTabSlate(String name, GList<String> data)
	{
		Scoreboard slate = newBoard();
		Objective o = newTabObjective(slate, name);
		
		int ind = data.size();
		
		for(String i : data)
		{
			setScore(i, ind, o);
			
			ind--;
		}
		
		return slate;
	}
	
	/**
	 * Updates the Tablist header
	 *
	 * @param p
	 *            {@link org.bukkit.entity.Player}
	 * @param header
	 *            lines of the header
	 */
	public static void setHeader(Player p, String... header)
	{
		setHeaderFooter(p, header, getFooter(p));
	}
	
	/**
	 * @param p
	 *            {@link org.bukkit.entity.Player}
	 * @return The header, or null
	 */
	public static String[] getHeader(Player p)
	{
		Tab tab = slateMap.get(p.getUniqueId());
		
		return tab != null ? tab.header : null;
	}
	
	/**
	 * Updates the Tablist footer
	 *
	 * @param p
	 *            {@link org.bukkit.entity.Player}
	 * @param footer
	 *            lines of the footer
	 */
	public static void setFooter(Player p, String... footer)
	{
		setHeaderFooter(p, getHeader(p), footer);
	}
	
	/**
	 * @param p
	 *            {@link org.bukkit.entity.Player}
	 * @return The footer, or null
	 */
	public static String[] getFooter(Player p)
	{
		Tab tab = slateMap.get(p.getUniqueId());
		return tab != null ? tab.footer : null;
	}
	
	/**
	 * Updates the header and footer
	 *
	 * @param p
	 *            {@link org.bukkit.entity.Player}
	 * @param header
	 *            the header
	 * @param footer
	 *            the footer
	 */
	public static void setHeaderFooter(Player p, String header, String footer)
	{
		setHeaderFooter(p, header == null ? null : new String[] {header}, footer == null ? null : new String[] {footer});
	}
	
	/**
	 * Updates the header and footer
	 *
	 * @param p
	 *            {@link org.bukkit.entity.Player}
	 * @param header
	 *            lines of the header
	 * @param footer
	 *            lines of the footer
	 */
	public static void setHeaderFooter(Player p, String[] header, String[] footer)
	{
		if(!p.isOnline())
		{
			return;
		}
		
		if(!slateMap.containsKey(p.getUniqueId()))
		{
			slateMap.put(p.getUniqueId(), new Tab(p));
		}
		
		Tab tab = slateMap.get(p.getUniqueId());
		tab.header = convertJSON(header);
		tab.footer = convertJSON(footer);
		tab.updateHeaderFooter();
	}
	
	/**
	 * Adds a {@link org.phantomapi.nms.inventivetalent.tabapi.TabItem}
	 *
	 * @param p
	 *            {@link org.bukkit.entity.Player}
	 * @param items
	 *            item to add
	 */
	public static void addItem(Player p, TabItem... items)
	{
		if(!p.isOnline())
		{
			return;
		}
		
		if(!slateMap.containsKey(p.getUniqueId()))
		{
			slateMap.put(p.getUniqueId(), new Tab(p));
		}
		
		Tab tab = slateMap.get(p.getUniqueId());
		tab.items.addAll(Arrays.asList(items));
		tab.updateItems();
	}
	
	/**
	 * Sets the displayed
	 * {@link org.phantomapi.nms.inventivetalent.tabapi.TabItem}s
	 *
	 * @param p
	 *            {@link org.bukkit.entity.Player}
	 * @param items
	 *            {@link org.phantomapi.nms.inventivetalent.tabapi.TabItem}
	 *            collection
	 */
	public static void setItems(Player p, Collection<TabItem> items)
	{
		if(!p.isOnline())
		{
			return;
		}
		
		if(!slateMap.containsKey(p.getUniqueId()))
		{
			slateMap.put(p.getUniqueId(), new Tab(p));
		}
		
		Tab tab = slateMap.get(p.getUniqueId());
		tab.clearItems();
		tab.items.clear();
		tab.items.addAll(items);
		tab.updateItems();
	}
	
	/**
	 * @param p
	 *            {@link org.bukkit.entity.Player}
	 * @return {@link org.phantomapi.nms.inventivetalent.tabapi.TabItem}
	 *         collection
	 */
	public static Collection<TabItem> getItems(Player p)
	{
		Tab tab = slateMap.get(p.getUniqueId());
		
		return tab != null ? new ArrayList<TabItem>(tab.items) : new ArrayList<TabItem>();
	}
	
	/**
	 * Removes an item
	 *
	 * @param p
	 *            {@link org.bukkit.entity.Player}
	 * @param items
	 *            {@link org.phantomapi.nms.inventivetalent.tabapi.TabItem} to
	 *            remove
	 */
	public static void removeItem(Player p, TabItem... items)
	{
		Tab tab = slateMap.get(p.getUniqueId());
		
		if(tab != null)
		{
			tab.clearItems();
			
			tab.items.removeAll(Arrays.asList(items));
			tab.updateItems();
		}
	}
	
	/**
	 * Adds the default items to the list (online players)
	 *
	 * @param player
	 *            {@link org.bukkit.entity.Player}
	 */
	public static void fillDefaultItems(Player player)
	{
		List<TabItem> items = new ArrayList<TabItem>();
		
		for(Player p : Bukkit.getOnlinePlayers())
		{
			if(player.canSee(p))
			{
				items.add(new TabItem(p.getUniqueId(), p.getPlayerListName(), p.getName(), 0, p.getGameMode()));
			}
		}
		
		setItems(player, items);
	}
	
	/**
	 * Removes all displayed items from the list (including the player)
	 *
	 * @param player
	 *            {@link org.bukkit.entity.Player}
	 */
	public static void clearAllItems(Player player)
	{
		fillDefaultItems(player);
		Tab tab = slateMap.get(player.getUniqueId());
		
		if(tab != null)
		{
			tab.clearItems();
		}
	}
	
	/**
	 * Forces an update for the list
	 *
	 * @param player
	 *            {@link org.bukkit.entity.Player}
	 */
	public static void updateTab(Player player)
	{
		Tab tab = slateMap.get(player.getUniqueId());
		
		if(tab != null)
		{
			tab.updateItems();
			tab.updateHeaderFooter();
		}
	}
	
	/**
	 * Removes a player's tab list (the default one will be used)
	 *
	 * @param player
	 *            {@link org.bukkit.entity.Player}
	 */
	public static void removeTab(Player player)
	{
		slateMap.remove(player.getUniqueId());
	}
	
	public static String convertJSON(String noJson)
	{
		if(noJson == null)
		{
			return null;
		}
		
		if(noJson.startsWith("{") && noJson.endsWith("}"))
		{
			return noJson;
		}
		
		return String.format("{\"text\":\"%s\"}", noJson);
	}
	
	public static String[] convertJSON(String... noJson)
	{
		if(noJson == null)
		{
			return null;
		}
		
		String[] converted = new String[noJson.length];
		
		for(int i = 0; i < noJson.length; i++)
		{
			if(i != 0 && (!noJson[i].startsWith("{") || !noJson[i].endsWith("}")))
			{
				noJson[i] = "\\n" + noJson[i];
			}
			converted[i] = convertJSON(noJson[i]);
		}
		
		return converted;
	}

	public static void setTabTitle(Player p, String header, String footer)
	{
		NMSX.sendTabTitle(p, header, footer);
	}
}

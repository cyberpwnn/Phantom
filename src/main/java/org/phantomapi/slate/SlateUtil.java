package org.phantomapi.slate;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.phantomapi.lang.GList;

/**
 * Scoreboard utilities
 * 
 * @author cyberpwn
 */
public class SlateUtil
{
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
		o.setDisplayName(name);
		o.setDisplaySlot(DisplaySlot.SIDEBAR);
		
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
}

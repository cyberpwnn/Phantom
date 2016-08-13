package org.phantomapi.clust;

import java.io.File;
import java.io.IOException;

import org.bukkit.entity.Player;

/**
 * Load and save player objects in a data folder
 * 
 * @author cyberpwn
 *
 */
public class PlayerDataSet
{
	private File folder;
	
	/**
	 * The player data folder
	 * 
	 * @param folder
	 *            the folder
	 */
	public PlayerDataSet(File folder)
	{
		this.folder = folder;
	}
	
	/**
	 * Load the data
	 * 
	 * @param p
	 *            the player
	 * @param c
	 *            the data cluster
	 * @throws IOException
	 *             shit happens
	 */
	public void load(Player p, DataCluster c) throws IOException
	{
		File f = new File(folder, p.getUniqueId() + ".yml");
		new YAMLDataInput().load(c, f);
	}
	
	/**
	 * Save the data
	 * 
	 * @param p
	 *            the player
	 * @param c
	 *            the data cluster
	 * @throws IOException
	 *             disk sata cable ripped out
	 */
	public void save(Player p, DataCluster c) throws IOException
	{
		File f = new File(folder, p.getUniqueId() + ".yml");
		new YAMLDataOutput().save(c, f);
	}
}

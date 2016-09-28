package org.phantomapi.nest;

import java.io.IOException;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.phantomapi.async.A;
import org.phantomapi.clust.DataCluster;
import org.phantomapi.clust.DataFile;
import org.phantomapi.lang.GList;
import org.phantomapi.lang.GMap;
import org.phantomapi.lang.GSet;
import org.phantomapi.lang.Title;
import org.phantomapi.sync.S;
import org.phantomapi.text.ProgressSpinner;
import org.phantomapi.util.C;
import org.phantomapi.util.ExceptionUtil;
import org.phantomapi.util.F;
import org.phantomapi.world.W;

/**
 * Implementation of a NestedChunk
 * 
 * @author cyberpwn
 */
public class PhantomChunkNest implements NestedChunk
{
	private DataFile df;
	private GMap<Location, NestedBlock> nested;
	private Chunk chunk;
	
	/**
	 * Create a phantom Chunk Nest
	 * 
	 * @param chunk
	 *            the chunk
	 */
	public PhantomChunkNest(Chunk chunk)
	{
		this.df = new DataFile();
		this.nested = new GMap<Location, NestedBlock>();
		this.chunk = chunk;
		
		try
		{
			this.load();
		}
		
		catch(IOException e)
		{
			ExceptionUtil.print(e);
		}
	}
	
	/**
	 * Load this chunk data
	 * 
	 * @throws IOException
	 *             shit happens
	 */
	public void load() throws IOException
	{
		new A()
		{
			@Override
			public void async()
			{
				Chunk ac = W.toAsync(chunk);
				nested = new GMap<Location, NestedBlock>();
				
				if(!NestUtil.getFile(ac).exists())
				{
					return;
				}
				
				try
				{
					df.load(NestUtil.getFile(ac));
				}
				
				catch(IOException e)
				{
					ExceptionUtil.print(e);
				}
				
				GSet<String> blocks = new GSet<String>();
				
				for(String i : df.keys())
				{
					if(i.contains("."))
					{
						blocks.add(i.split("\\.")[0]);
					}
				}
				
				int k = 128;
				int v = 0;
				ProgressSpinner s = new ProgressSpinner();
				String pc = "";
				
				for(String i : blocks)
				{
					if(!chunk.isLoaded())
					{
						return;
					}
					
					int x = Integer.valueOf(i.split("_")[0]);
					int y = Integer.valueOf(i.split("_")[1]);
					int z = Integer.valueOf(i.split("_")[2]);
					Block bb = ac.getBlock(x, y, z);
					nested.put(bb.getLocation(), new PhantomBlockNest(bb, df.crop(i)));
					k--;
					v++;
					
					if(k <= 0)
					{
						k = 64;
						pc = F.pc((double)((double)v / (double)blocks.size()), 0);
						String cc = pc;
						
						new S()
						{
							@Override
							public void sync()
							{
								Title t = new Title(C.LIGHT_PURPLE + s.toString() + " " + "Loading " + cc, 0, 20, 20);
								
								for(Entity i : chunk.getEntities())
								{
									if(i instanceof Player)
									{
										Player p = (Player) i;
										t.send(p);
									}
								}
							}
						};
					}
				}
				
				new S()
				{
					@Override
					public void sync()
					{
						Title t = new Title(C.LIGHT_PURPLE + s.toString() + " " + "Complete", 0, 20, 20);
						
						for(Entity i : chunk.getEntities())
						{
							if(i instanceof Player)
							{
								Player p = (Player) i;
								t.send(p);
							}
						}
					}
				};
			}
		};
	}
	
	/**
	 * Saves this set of data into a nest file. Any blocks with no nest data
	 * will not be saved. If there is no nest data at all, any nest file will be
	 * remved, as it is saving changes
	 * 
	 * @throws IOException
	 *             shit happens
	 */
	public void save() throws IOException
	{
		for(Location i : nested.k())
		{
			if(nested.get(i).getData().getData().isEmpty())
			{
				nested.remove(i);
			}
		}
		
		if(nested.isEmpty())
		{
			NestUtil.getFile(chunk).delete();
			return;
		}
		
		for(Location i : nested.k())
		{
			df.add(nested.get(i).getData(), W.getChunkX(i.getBlock()) + "_" + i.getBlockY() + "_" + W.getChunkZ(i.getBlock()) + ".");
		}
		
		new A()
		{
			@Override
			public void async()
			{
				try
				{
					df.save(NestUtil.getFile(chunk));
				}
				
				catch(IOException e)
				{
					ExceptionUtil.print(e);
					
					new S()
					{
						
						@Override
						public void sync()
						{
							try
							{
								df.save(NestUtil.getFile(chunk));
							}
							
							catch(IOException e)
							{
								ExceptionUtil.print(e);
							}
						}
					};
				}
			}
		};
	}
	
	public NestedBlock getBlock(Block block)
	{
		if(!nested.containsKey(block.getLocation()))
		{
			nested.put(block.getLocation(), new PhantomBlockNest(block, new DataCluster()));
		}
		
		return nested.get(block.getLocation());
	}
	
	public Chunk getChunk()
	{
		return chunk;
	}

	public GList<NestedBlock> getBlocks()
	{
		return nested.v();
	}
}

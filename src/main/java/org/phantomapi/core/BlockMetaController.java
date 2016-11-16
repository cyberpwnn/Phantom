package org.phantomapi.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import org.apache.commons.io.IOUtils;
import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.phantomapi.async.A;
import org.phantomapi.blockmeta.ChunkMeta;
import org.phantomapi.clust.Configurable;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Controller;
import org.phantomapi.construct.Ticked;
import org.phantomapi.event.MetaChunkLoadEvent;
import org.phantomapi.event.MetaChunkUnloadEvent;
import org.phantomapi.lang.GChunk;
import org.phantomapi.lang.GList;
import org.phantomapi.lang.GMap;
import org.phantomapi.statistics.Monitorable;
import org.phantomapi.sync.S;
import org.phantomapi.util.C;
import org.phantomapi.util.Chunks;
import org.phantomapi.util.F;

@Ticked(200)
public class BlockMetaController extends Controller implements Monitorable
{
	private GMap<GChunk, ChunkMeta> cache;
	
	public BlockMetaController(Controllable parentController)
	{
		super(parentController);
		
		cache = new GMap<GChunk, ChunkMeta>();
	}
	
	@Override
	public void onStart()
	{
		for(Chunk i : Chunks.getLoadedChunks())
		{
			loadChunk(new GChunk(i), true);
		}
	}
	
	@Override
	public void onStop()
	{
		for(Chunk i : Chunks.getLoadedChunks())
		{
			if(!hasChunk(new GChunk(i)))
			{
				continue;
			}
			
			callEvent(new MetaChunkUnloadEvent(i, getChunk(new GChunk(i))));
			saveChunk(new GChunk(i), false);
		}
	}
	
	@Override
	public void onTick()
	{
		GList<Chunk> c = Chunks.getLoadedChunks();
		
		if(cache.k().size() != c.size())
		{
			for(GChunk i : cache.k())
			{
				boolean b = false;
				
				for(Chunk j : c)
				{
					if(new GChunk(j).equals(i))
					{
						b = true;
						break;
					}
				}
				
				if(!b)
				{
					new S()
					{
						@Override
						public void sync()
						{
							saveChunk(i, true);
						}
					};
				}
			}
		}
	}
	
	@EventHandler
	public void on(ChunkLoadEvent e)
	{
		GChunk gc = new GChunk(e.getChunk());
		
		if(!hasChunk(gc))
		{
			loadChunk(gc, true);
		}
	}
	
	@EventHandler
	public void on(ChunkUnloadEvent e)
	{
		GChunk gc = new GChunk(e.getChunk());
		
		if(hasChunk(gc))
		{
			callEvent(new MetaChunkUnloadEvent(e.getChunk(), getChunk(gc)));
			saveChunk(gc, true);
		}
	}
	
	public ChunkMeta getChunk(GChunk gc)
	{
		return cache.get(gc);
	}
	
	public boolean hasChunk(GChunk gc)
	{
		return cache.containsKey(gc);
	}
	
	public void loadChunk(GChunk chunk, boolean async)
	{
		if(!hasChunk(chunk))
		{
			ChunkMeta cm = new ChunkMeta(chunk);
			
			if(async)
			{
				new A()
				{
					@Override
					public void async()
					{
						try
						{
							read(cm.getFile(), cm);
							cache.put(chunk, cm);
						}
						
						catch(IOException e)
						{
							return;
						}
						
						new S()
						{
							@Override
							public void sync()
							{
								callEvent(new MetaChunkLoadEvent(chunk.toChunk(), cm));
							}
						};
					}
				};
			}
			
			else
			{
				try
				{
					read(cm.getFile(), cm);
					cache.put(chunk, cm);
				}
				
				catch(IOException e)
				{
					return;
				}
			}
		}
	}
	
	public void saveChunk(GChunk chunk, boolean async)
	{
		ChunkMeta cm = cache.get(chunk);
		cache.remove(chunk);
		
		if(async)
		{
			new A()
			{
				@Override
				public void async()
				{
					try
					{
						write(cm.getFile(), cm);
					}
					
					catch(IOException e)
					{
						return;
					}
				}
			};
		}
		
		else
		{
			try
			{
				write(cm.getFile(), cm);
			}
			
			catch(IOException e)
			{
				return;
			}
		}
	}
	
	public void read(File file, Configurable cc) throws IOException
	{
		if(!file.exists())
		{
			file.getParentFile().mkdirs();
		}
		
		else
		{
			byte[] data = Files.readAllBytes(file.toPath());
			cc.getConfiguration().clear();
			cc.getConfiguration().addCompressed(data);
		}
	}
	
	public void write(File file, Configurable cc) throws IOException
	{
		if(cc.getConfiguration().keys().isEmpty())
		{
			return;
		}
		
		if(!file.exists())
		{
			file.getParentFile().mkdirs();
			file.createNewFile();
		}
		
		FileOutputStream fos = new FileOutputStream(file, false);
		IOUtils.write(cc.getConfiguration().compress(), fos);
		fos.close();
	}
	
	@Override
	public String getMonitorableData()
	{
		return "Cache: " + C.LIGHT_PURPLE + F.f(cache.size());
	}
}

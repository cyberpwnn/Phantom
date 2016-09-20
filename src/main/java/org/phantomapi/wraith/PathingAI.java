package org.phantomapi.wraith;

import org.bukkit.Location;
import org.bukkit.Material;
import org.phantomapi.async.A;
import org.phantomapi.lang.GMap;
import org.phantomapi.sync.S;
import org.phantomapi.util.M;
import org.phantomapi.world.Area;
import org.phantomapi.world.W;
import net.citizensnpcs.api.persistence.Persist;

public class PathingAI extends WraithTrait
{
	private GMap<Material, Double> priorities;
	
	@Persist
	private Double visionRange = 12.7;
	
	@Persist
	private Double activity = 0.046;
	private Boolean scanning;
	private Boolean running;
	private Double range;
	private Location beacon;
	
	public PathingAI()
	{
		super("ai-path");
		
		this.priorities = new GMap<Material, Double>();
		this.visionRange = 24.7;
		this.activity = 0.6;
		this.range = 128.0;
		this.running = true;
		this.scanning = false;
		this.beacon = null;
	}
	
	@Override
	public void run()
	{
		if(beacon == null)
		{
			beacon = getNPC().getStoredLocation();
		}
		
		if(!running)
		{
			return;
		}
		
		if(!Area.within(beacon, getNPC().getStoredLocation(), range))
		{
			getNPC().getNavigator().setTarget(beacon);
			getNPC().faceLocation(beacon);
			
			return;
		}
		
		if(M.r(activity))
		{
			if(getNPC().getNavigator().isNavigating())
			{
				getNPC().faceLocation(getNPC().getNavigator().getTargetAsLocation());
			}
			
			else if(!scanning)
			{
				Area a = new Area(new Location(W.getAsyncWorld(getNPC().getStoredLocation().getWorld().getName()), getNPC().getStoredLocation().getX(), getNPC().getStoredLocation().getY(), getNPC().getStoredLocation().getZ()), visionRange);
				scanning = true;
				
				new A()
				{
					@Override
					public void async()
					{
						Location k = null;
						
						for(int i = 0; i < 1024; i++)
						{
							Location l = a.random();
							
							if(priorities.k().contains(l.getBlock().getType()))
							{
								k = l;
								break;
							}
						}
						
						Location vv = k;
						
						new S()
						{
							@Override
							public void sync()
							{
								try
								{
									scanning = false;
									Location mx = new Location(W.getSyncWorld(vv.getWorld()), vv.getX(), vv.getY(), vv.getZ());
									getNPC().getNavigator().setTarget(mx);
									getNPC().faceLocation(mx);
								}
								
								catch(Exception e)
								{
									
								}
							}
						};
					}
				};
			}
		}
	}
	
	@Override
	public void onAttach()
	{
		
	}
	
	@Override
	public void onDespawn()
	{
		
	}
	
	@Override
	public void onSpawn()
	{
		
	}
	
	@Override
	public void onRemove()
	{
		
	}
	
	public GMap<Material, Double> getPriorities()
	{
		return priorities;
	}
	
	public void setPriorities(GMap<Material, Double> priorities)
	{
		this.priorities = priorities;
	}
	
	public Double getVisionRange()
	{
		return visionRange;
	}
	
	public void setVisionRange(Double visionRange)
	{
		this.visionRange = visionRange;
	}
	
	public void addPriority(Material material, Double chance)
	{
		priorities.put(material, chance);
	}
	
	public Double getActivity()
	{
		return activity;
	}
	
	public void setActivity(Double activity)
	{
		this.activity = activity;
	}
	
	public Boolean getScanning()
	{
		return scanning;
	}
	
	public void setScanning(Boolean scanning)
	{
		this.scanning = scanning;
	}
	
	public Boolean getRunning()
	{
		return running;
	}
	
	public void setRunning(Boolean running)
	{
		this.running = running;
	}
}

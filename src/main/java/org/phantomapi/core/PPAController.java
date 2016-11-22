package org.phantomapi.core;

import org.phantomapi.Phantom;
import org.phantomapi.async.A;
import org.phantomapi.clust.Comment;
import org.phantomapi.clust.ConfigurableController;
import org.phantomapi.clust.Keyed;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Ticked;
import org.phantomapi.lang.GList;
import org.phantomapi.ppa.PPA;
import org.phantomapi.ppa.PPAP;
import org.phantomapi.statistics.Monitorable;
import org.phantomapi.sync.S;
import org.phantomapi.util.C;
import org.phantomapi.util.M;
import redis.clients.jedis.Jedis;

@Ticked(20)
public class PPAController extends ConfigurableController implements Monitorable
{
	@Keyed("identifier")
	@Comment("The identifier for this server.\nUse a human readable name such as the server name.")
	public String id = "null";
	
	private GList<PPA> out;
	private Boolean running;
	private Jedis r;
	private int in = 0;
	private int outt = 0;
	private int wait = 0;
	
	public PPAController(Controllable parentController)
	{
		super(parentController, "ppa");
		
		running = false;
		out = new GList<PPA>();
	}
	
	@Override
	public void onStart()
	{
		loadCluster(this);
		
		r = Phantom.instance().getRedisConnectionController().createSplitConnection();
	}
	
	@Override
	public void onTick()
	{
		if(!running)
		{
			GList<PPA> o = out.copy();
			out.clear();
			running = true;
			
			new A()
			{
				@Override
				public void async()
				{
					PPAP p = new PPAP();
					String s = r.get("ppa:inbox");
					GList<PPA> h = new GList<PPA>();
					in = o.size();
					
					if(s != null)
					{
						p.setData(s);
					}
					
					for(PPA i : o)
					{
						p.add(i);
					}
					
					for(PPA i : p.getPPAS().copy())
					{
						if(i.getDestination().equals(id) || i.getDestination().equals("all"))
						{
							h.add(i);
							p.getPPAS().remove(i);
						}
					}
					
					outt = h.size();
					
					for(PPA i : p.getPPAS().copy())
					{
						if((M.ms() - i.getTime()) > 10000)
						{
							f("Packet Timed Out: " + i.toJSON().toString());
							p.getPPAS().remove(i);
						}
					}
					
					if(!p.getPPAS().isEmpty())
					{
						r.set("ppa:inbox", p.getData());
					}
					
					else
					{
						r.del("ppa:inbox");
					}
					
					wait = p.getPPAS().size();
					
					if(!h.isEmpty())
					{
						new S()
						{
							@Override
							public void sync()
							{
								handle(h);
							}
						};
					}
					
					new S()
					{
						@Override
						public void sync()
						{
							running = false;
						}
					};
				}
			};
		}
	}
	
	public void handle(GList<PPA> handles)
	{
		for(PPA i : handles)
		{
			s(i.getDestination() + " -> " + i.getSource() + " : " + i.toJSON().toString());
		}
	}
	
	public void send(PPA ppa)
	{
		w(ppa.getSource() + " -> " + ppa.getDestination() + " : " + ppa.toJSON().toString());
		out.add(ppa);
	}
	
	@Override
	public void onStop()
	{
		r.disconnect();
	}
	
	@Override
	public String getMonitorableData()
	{
		return C.GREEN + "> " + in + C.YELLOW + " > " + wait + " > " + C.RED + outt + " >";
	}
}

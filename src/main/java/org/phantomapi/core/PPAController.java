package org.phantomapi.core;

import org.phantomapi.Phantom;
import org.phantomapi.async.A;
import org.phantomapi.clust.Comment;
import org.phantomapi.clust.ConfigurableController;
import org.phantomapi.clust.Keyed;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Ticked;
import org.phantomapi.event.PPAReceiveEvent;
import org.phantomapi.lang.GList;
import org.phantomapi.lang.GSet;
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
	
	@Keyed("identifiers")
	@Comment("The list of all identifiers on the network. Keep THIS server's id in this list also.")
	public GList<String> ids = new GList<String>().qadd("null");
	
	private GSet<PPA> out;
	private Boolean running;
	private Jedis r;
	private int invd = 0;
	private int outt = 0;
	private int wait = 0;
	
	public PPAController(Controllable parentController)
	{
		super(parentController, "ppa");
		
		running = false;
		out = new GSet<PPA>();
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
			GList<PPA> o = new GList<PPA>(out);
			out.clear();
			running = true;
			
			new A()
			{
				@Override
				public void async()
				{
					if(r == null || !r.isConnected())
					{
						f("Lost Connection to Redis.");
						f("Attempting to reconnect");
						r = Phantom.instance().getRedisConnectionController().createSplitConnection();
					}
					
					if(r == null)
					{
						f("No Connection");
						return;
					}
					
					PPAP p = new PPAP();
					String s = r.get("ppa:inbox");
					GList<PPA> h = new GList<PPA>();
					invd = o.size();
					
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
						if(i.getDestination().equals(PPAController.this.id))
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
							f("Packet Timed Out: " + i.getSource() + " -- " + i.getDestination() + " (" + i.getType() + ")");
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
			s(i.getSource() + " -> " + i.getDestination() + " (" + i.getType() + ")");
			callEvent(new PPAReceiveEvent(i));
		}
	}
	
	public void send(PPA ppa)
	{
		if(ppa.getDestination().equals("all"))
		{
			for(String i : ids)
			{
				if(!id.equals(i))
				{
					PPA ppx = new PPA("?");
					ppx.setData(ppa.copy().getData());
					ppx.set("ppad", i);
					send(ppx);
				}
			}
			
			return;
		}
		
		s(ppa.getSource() + " -> " + ppa.getDestination() + " (" + ppa.getType() + ")");
		
		out.add(ppa);
	}
	
	@Override
	public void onStop()
	{
		String l = r.get("ppa:client");
		
		if(l != null)
		{
			GList<String> des = new GList<String>();
			
			if(l.contains(":"))
			{
				des = new GList<String>(l.split(":"));
			}
			
			else
			{
				des.add(l);
			}
			
			des.remove(id);
			
			if(des.isEmpty())
			{
				v("Unreg: FINAL");
				r.del("ppa:client");
			}
			
			else
			{
				v("Unreg: " + des.toString(":"));
				r.set("ppa:client", des.toString(":"));
			}
		}
		
		r.disconnect();
	}
	
	@Override
	public String getMonitorableData()
	{
		return C.GREEN + "> " + invd + C.YELLOW + " > " + wait + " > " + C.RED + outt + " >";
	}
}

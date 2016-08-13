package org.phantomapi;

import java.util.UUID;
import org.phantomapi.clust.Comment;
import org.phantomapi.clust.Configurable;
import org.phantomapi.clust.DataCluster;
import org.phantomapi.clust.Keyed;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Controller;
import org.phantomapi.construct.Ticked;
import org.phantomapi.lang.GList;
import org.phantomapi.sync.TaskLater;
import org.phantomapi.transmit.Transmission;
import org.phantomapi.transmit.TransmissionClient;
import org.phantomapi.transmit.TransmissionFilter;
import org.phantomapi.transmit.TransmissionListener;

@Ticked(0)
public class TransmissionController extends Controller implements Configurable
{
	private GList<TransmissionListener> listeners;
	private DataCluster cc;
	private TransmissionClient client;
	private static GList<Transmission> transmissionOBuffer;
	private static GList<Transmission> transmissionIBuffer;
	
	@Comment("Enable Transmission?")
	@Keyed("transmission.enabled")
	public boolean enabled = false;
	
	@Comment("The address to the transmission server")
	@Keyed("transmission.address")
	public String address = "localhost";
	
	@Comment("The alias for this server. You should change it to the bungeecord server name.")
	@Keyed("transmission.alias")
	public String alias = "change-me-" + UUID.randomUUID();
	
	@Comment("The port of communication")
	@Keyed("transmission.port")
	public int port = 4412;
	
	public TransmissionController(Controllable parentController)
	{
		super(parentController);
		
		listeners = new GList<TransmissionListener>();
		transmissionOBuffer = new GList<Transmission>();
		transmissionIBuffer = new GList<Transmission>();
		cc = new DataCluster();
	}
	
	public void onStart()
	{
		loadCluster(this);
		
		if(enabled)
		{
			new TaskLater(20)
			{
				public void run()
				{
					s("Starting Transmission server...");
					client = new TransmissionClient(address, port);
					client.start();
				}
			};
		}
	}
	
	public void onStop()
	{
		if(client != null)
		{
			client.interrupt();
		}
	}
	
	public void onTick()
	{
		while(enabled && !transmissionIBuffer.isEmpty())
		{
			pop(transmissionIBuffer.pop());
		}
	}
	
	private void pop(Transmission pop)
	{
		for(TransmissionListener i : listeners)
		{
			try
			{
				if(i.getClass().getMethod("onTransmissionReceived", Transmission.class).isAnnotationPresent(TransmissionFilter.class))
				{
					TransmissionFilter tf = i.getClass().getMethod("onTransmissionReceived", Transmission.class).getAnnotation(TransmissionFilter.class);
					
					if(pop.getChannel().equals(tf.value()))
					{
						i.onTransmissionReceived(pop);
					}
				}
				
				else
				{
					i.onTransmissionReceived(pop);
				}
			}
			
			catch(Exception e)
			{
				
			}
		}
	}
	
	public GList<TransmissionListener> getListeners()
	{
		return listeners;
	}
	
	/**
	 * Register a transmission listener
	 * 
	 * @param listener
	 *            the listener
	 */
	public void registerListener(TransmissionListener listener)
	{
		listeners.add(listener);
	}
	
	/**
	 * Transmit a packet
	 * 
	 * @param packet
	 *            the packet
	 */
	public void transmitPacket(Transmission packet)
	{
		transmissionOBuffer.add(packet);
	}
	
	public static Transmission getNext()
	{
		return transmissionOBuffer.pop();
	}
	
	public static void dispatch(Transmission it)
	{
		transmissionIBuffer.add(it);
	}
	
	@Override
	public void onNewConfig()
	{
		// Dynamic
	}
	
	@Override
	public void onReadConfig()
	{
		// Dynamic
	}
	
	@Override
	public DataCluster getConfiguration()
	{
		return cc;
	}
	
	@Override
	public String getCodeName()
	{
		return "transmission";
	}
	
	public static void fail(String string)
	{
		Phantom.instance().getTransmissionController().f("Network Failure: " + string);
	}
}

package org.phantomapi.transmit;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import org.phantomapi.TransmissionController;
import org.phantomapi.clust.JSONObject;

public class TransmissionClient extends Thread
{
	private String address;
	private int port;
	private Socket s;
	
	public TransmissionClient(String address, int port)
	{
		this.address = address;
		this.port = port;
		this.s = null;
	}
	
	public void run()
	{
		try
		{
			s = new Socket(address, port);
			while(!interrupted())
			{
				try
				{
					DataInputStream i = new DataInputStream(s.getInputStream());
					DataOutputStream o = new DataOutputStream(s.getOutputStream());
					
					Transmission t = null;
					
					while((t = TransmissionController.getNext()) != null)
					{
						o.writeUTF(t.toString());
					}
					
					o.flush();
					
					while(i.available() != 0)
					{
						Transmission it = new Transmission(new JSONObject(i.readUTF()));
						TransmissionController.dispatch(it);
					}
					
					o.flush();
					sleep(50);
				}
				
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			
			s.close();
		}
		
		catch(UnknownHostException e)
		{
			TransmissionController.fail("Unknown Host Exception " + address);
			
			if(s != null)
			{
				try
				{
					s.close();
				}

				catch(IOException e1)
				{
					
				}
			}
		}
		
		catch(IOException e)
		{
			TransmissionController.fail("Failed to bind to port " + port + " OR Connection timed out.");
			
			if(s != null)
			{
				try
				{
					s.close();
				}

				catch(IOException e1)
				{
					
				}
			}
		}
	}
}

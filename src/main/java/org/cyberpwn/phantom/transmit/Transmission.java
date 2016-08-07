package org.cyberpwn.phantom.transmit;

import org.cyberpwn.phantom.clust.JSONObject;

/**
 * Transmit packet structure
 * 
 * @author cyberpwn
 *
 */
public class Transmission
{
	private String source;
	private String destination;
	private String channel;
	private JSONObject data;
	
	public Transmission(JSONObject jso)
	{
		this.source = jso.getString("source");
		this.destination = jso.getString("destination");
		this.channel = jso.getString("channel");
		this.data = jso.getJSONObject("data");
	}
	
	public Transmission(String destination, String channel, JSONObject data)
	{
		this.source = "this";
		this.destination = destination;
		this.channel = channel;
		this.data = data;
	}
	
	public String toString()
	{
		JSONObject jso = new JSONObject();
		jso.put("data", data);
		jso.put("source", source);
		jso.put("destination", destination);
		jso.put(channel, channel);
		
		return jso.toString();
	}

	public String getSource()
	{
		return source;
	}

	public void setSource(String source)
	{
		this.source = source;
	}

	public String getDestination()
	{
		return destination;
	}

	public void setDestination(String destination)
	{
		this.destination = destination;
	}

	public String getChannel()
	{
		return channel;
	}

	public void setChannel(String channel)
	{
		this.channel = channel;
	}

	public JSONObject getData()
	{
		return data;
	}

	public void setData(JSONObject data)
	{
		this.data = data;
	}
}

package org.phantomapi.text;

import org.phantomapi.clust.JSONArray;
import org.phantomapi.clust.JSONObject;
import org.phantomapi.lang.GList;

/**
 * Raw Text EXTRA
 * 
 * @author cyberpwn
 */
public class RTEX
{
	private GList<ColoredString> extras;
	
	/**
	 * Create a new raw text base
	 * 
	 * @param extras
	 *            the extras
	 */
	public RTEX(ColoredString... extras)
	{
		this.extras = new GList<ColoredString>(extras);
	}
	
	/**
	 * Get the json object for this
	 * 
	 * @return
	 */
	public JSONObject toJSON()
	{
		JSONObject js = new JSONObject();
		JSONArray jsa = new JSONArray();
		
		for(ColoredString i : extras)
		{
			JSONObject extra = new JSONObject();
			extra.put("text", i.getS());
			extra.put("color", i.getC().name().toLowerCase());
			jsa.put(extra);
		}
		
		js.put("text", "");
		js.put("extra", jsa);
		
		return js;
	}
}

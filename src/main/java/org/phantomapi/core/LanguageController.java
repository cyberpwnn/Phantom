package org.phantomapi.core;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import org.phantomapi.clust.ConfigurableController;
import org.phantomapi.clust.JSONObject;
import org.phantomapi.clust.Keyed;
import org.phantomapi.construct.Controllable;

public class LanguageController extends ConfigurableController
{
	@Keyed("api-key")
	public String apiKey = "trnsl.1.1.20161130T053201Z.3346e68fba44191d.0e315fdde72be006749cd3278ebca78ee3801079";
	
	public LanguageController(Controllable parentController)
	{
		super(parentController, "lang");
	}
	
	@Override
	public void onStart()
	{
		
	}
	
	@Override
	public void onStop()
	{
		
	}
	
	public String translate(String text, String from, String to) throws Exception
	{
		URL url = new URL("https://translate.yandex.net/api/v1.5/tr.json/translate?key=" + apiKey + "&text=" + text + "&lang=" + from + "-" + to);
		JSONObject o = request(url);
		
		return o.getJSONArray("text").getString(0);
	}
	
	public String getLanguageCode(String text) throws Exception
	{
		URL url = new URL("https://translate.yandex.net/api/v1.5/tr.json/detect?key=" + apiKey + "&text=" + text);
		JSONObject o = request(url);
		
		return o.getString("lang");
	}
	
	public JSONObject request(URL url) throws Exception
	{
		String s = "";
		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
		String inputLine;
		
		while((inputLine = in.readLine()) != null)
		{
			s += inputLine;
		}
		
		in.close();
		
		return new JSONObject(s);
	}
}

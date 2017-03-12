package org.phantomapi.core;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import org.phantomapi.Phantom;
import org.phantomapi.clust.Configurable;
import org.phantomapi.clust.ConfigurableController;
import org.phantomapi.clust.DataCluster;
import org.phantomapi.clust.JSONObject;
import org.phantomapi.clust.Keyed;
import org.phantomapi.construct.Controllable;
import org.phantomapi.lang.GMap;
import org.phantomapi.text.Lang;
import org.phantomapi.text.LanguagUtil;

public class LanguageController extends ConfigurableController
{
	@Keyed("api-key")
	public String apiKey = "trnsl.1.1.20161130T053201Z.3346e68fba44191d.0e315fdde72be006749cd3278ebca78ee3801079";
	
	private GMap<Field, Lang> langMap;
	private GMap<Field, Object> instMap;
	
	public LanguageController(Controllable parentController)
	{
		super(parentController, "lang");
		
		langMap = new GMap<Field, Lang>();
		instMap = new GMap<Field, Object>();
	}
	
	@Override
	public void onStart()
	{
		
	}
	
	@Override
	public void onStop()
	{
		
	}
	
	public static void scan(Class<?> c, Object ix) throws IllegalArgumentException, IllegalAccessException
	{
		GMap<Field, Lang> m = LanguagUtil.getFields(c);
		
		for(Field i : m.k())
		{
			if(i.getType().equals(String.class))
			{
				Phantom.instance().getLanguageController().getLangMap().put(i, m.get(i));
				Phantom.instance().getLanguageController().getInstMap().put(i, ix);
				
				((Configurable) Phantom.instance().getLanguageController()).getConfiguration().set(m.get(i).value(), (String) i.get(ix));
			}
		}
	}
	
	public void modify() throws IllegalArgumentException, IllegalAccessException
	{
		DataCluster cc = ((Configurable) Phantom.instance().getLanguageController()).getConfiguration();
		
		for(Field i : langMap.k())
		{
			if(cc.contains(langMap.get(i).value()))
			{
				i.set(instMap.get(i), langMap.get(i).value());
			}
		}
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
	
	public GMap<Field, Lang> getLangMap()
	{
		return langMap;
	}

	public GMap<Field, Object> getInstMap()
	{
		return instMap;
	}
}


package org.phantomapi.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.phantomapi.clust.JSONObject;

/**
 * Paste to the web
 * 
 * @author cyberpwn
 */
public class Paste
{
	/**
	 * Paste to throw.volmit.com/
	 * 
	 * @param s
	 *            the paste text (use newline chars for new lines)
	 * @return the url to access the paste
	 * @throws Exception
	 *             shit happens
	 */
	public static String paste(String s) throws Exception
	{
		URL url = new URL("http://throw.volmit.com/documents");
		HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
		httpCon.setDoOutput(true);
		httpCon.setRequestMethod("POST");
		httpCon.getOutputStream().write(s.getBytes());
		BufferedReader in = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
		JSONObject jso = new JSONObject(in.readLine());
		
		return "http://throw.volmit.com/" + jso.getString("key");
	}
}

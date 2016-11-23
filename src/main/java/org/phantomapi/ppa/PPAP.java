package org.phantomapi.ppa;

import org.phantomapi.clust.JSONObject;
import org.phantomapi.lang.GList;
import org.phantomapi.util.D;

public class PPAP
{
	private GList<PPA> ppas;
	
	public PPAP()
	{
		ppas = new GList<PPA>();
	}
	
	public void add(PPA ppa)
	{
		ppas.add(ppa);
	}
	
	public void setData(String compressed)
	{
		GList<PPA> pas = new GList<PPA>();
		
		for(String i : compressed.split(":::"))
		{
			try
			{
				PPA ppa = new PPA("?");
				ppa.addJson(new JSONObject(i));
				pas.add(ppa);
			}
			
			catch(Exception e)
			{
				new D("PPA").f("Invalid PPA: " + i);
			}
		}
		
		ppas = pas;
	}
	
	public String getData()
	{
		String k = "";
		
		if(ppas.isEmpty())
		{
			return null;
		}
		
		for(PPA i : ppas)
		{
			k = k + ":::" + i.toJSON().toString();
		}
		
		return k.substring(3);
	}
	
	public GList<PPA> getPPAS()
	{
		return ppas;
	}
}

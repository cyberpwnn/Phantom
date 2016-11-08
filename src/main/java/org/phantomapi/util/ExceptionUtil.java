package org.phantomapi.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.phantomapi.Phantom;
import org.phantomapi.async.A;
import org.phantomapi.lang.GList;
import org.phantomapi.lang.GMap;

/**
 * Exception utils
 * 
 * @author cyberpwn
 */
public class ExceptionUtil
{
	/**
	 * Print the exception
	 * 
	 * @param e
	 *            the exception
	 */
	public static void print(Throwable e)
	{
		e.printStackTrace();
	}
	
	public static void writeIssues(File destination)
	{
		destination.mkdirs();
		GList<String> plugs = new GList<String>();
		
		for(Plugin i : Bukkit.getPluginManager().getPlugins())
		{
			plugs.add(i.getName());
		}
		
		new A()
		{
			@Override
			public void async()
			{
				File logs = new File(Phantom.instance().getDataFolder().getParentFile().getParentFile(), "logs");
				D d = new D("Log Scanner");
				GList<String> errors = new GList<String>();
				GList<String> warnings = new GList<String>();
				
				GMap<String, GList<String>> errorsp = new GMap<String, GList<String>>();
				GMap<String, GList<String>> warningsp = new GMap<String, GList<String>>();
				
				GList<String> locks = new GList<String>();
				long k = 0;
				
				if(logs.exists())
				{
					d.s("Scanning Logs");
					int s = logs.listFiles().length;
					int vx = 0;
					
					for(File i : logs.listFiles())
					{
						File fout = new File(i.getParentFile(), i.getName() + ".temp");
						
						if(i.getName().endsWith(".log.gz"))
						{
							try
							{
								byte[] buffer = new byte[1024];
								FileInputStream fis = new FileInputStream(i);
								FileOutputStream fos = new FileOutputStream(fout);
								GZIPInputStream gzi = new GZIPInputStream(fis);
								
								int len;
								
								while((len = gzi.read(buffer)) > 0)
								{
									fos.write(buffer, 0, len);
								}
								
								gzi.close();
								fos.close();
								
								FileReader fi = new FileReader(fout);
								BufferedReader bu = new BufferedReader(fi);
								String line;
								long v = 0;
								
								while((line = bu.readLine()) != null)
								{
									k++;
									v++;
									
									if(line.contains("[Server thread/ERROR]:"))
									{
										errors.add("ERROR @ " + i.getName() + " LINE " + v + " > " + line);
										
										for(String j : plugs)
										{
											if(line.contains(j))
											{
												if(!errorsp.containsKey(j))
												{
													errorsp.put(j, new GList<String>());
												}
												
												errorsp.get(j).add("ERROR @ " + i.getName() + " LINE " + v + " > " + line);
											}
										}
									}
									
									if(line.contains("[Server thread/WARN]:"))
									{
										warnings.add("WARN @ " + i.getName() + " LINE " + v + " > " + line);
										
										for(String j : plugs)
										{
											if(line.contains(j))
											{
												if(!warningsp.containsKey(j))
												{
													warningsp.put(j, new GList<String>());
												}
												
												warningsp.get(j).add("WARN @ " + i.getName() + " LINE " + v + " > " + line);
											}
										}
									}
									
									if(line.contains("[Spigot Watchdog Thread/ERROR]: The server has stopped responding!"))
									{
										locks.add("LOCKOUT @ " + i.getName() + " LINE " + v + " > " + line);
									}
								}
								
								bu.close();
								fout.delete();
							}
							
							catch(Exception e)
							{
								e.printStackTrace();
							}
						}
						
						vx++;
						d.v("Scanning Logs: " + C.AQUA + F.pc((double) vx / (double) s, 2));
					}
					
					d.s("Scanned " + F.f(k) + " Elements");
					d.f("Contains " + F.f(errors.size()) + " Errors");
					
					for(String i : errorsp.k())
					{
						d.f("  " + C.LIGHT_PURPLE + i + C.RED + ": " + F.f(errorsp.get(i).size()) + " (" + F.pc((double) errorsp.get(i).size() / (double) errors.size(), 2) + ")");
					}
					
					d.w("Contains " + F.f(warnings.size()) + " Warnings");
					
					for(String i : warningsp.k())
					{
						d.w("  " + C.LIGHT_PURPLE + i + C.YELLOW + ": " + F.f(warningsp.get(i).size()) + " (" + F.pc((double) warningsp.get(i).size() / (double) warnings.size(), 2) + ")");
					}
					
					d.v("Contains " + F.f(locks.size()) + " Dead Locks");
					
					File er = new File(destination, "errors.txt");
					File wa = new File(destination, "warnings.txt");
					
					d.s("Writing Plugin Errors");
					
					for(String i : errorsp.k())
					{
						File ff = new File(destination, i + "-errors.txt");
						
						try
						{
							BufferedWriter bw = new BufferedWriter(new FileWriter(ff));
							
							for(String j : errorsp.get(i))
							{
								bw.write(j + "\n");
							}
							
							bw.close();
						}
						
						catch(IOException e)
						{
							e.printStackTrace();
						}
					}
					
					d.s("Writing Plugin Warnings");
					
					for(String i : warningsp.k())
					{
						File ff = new File(destination, i + "-warnings.txt");
						
						try
						{
							BufferedWriter bw = new BufferedWriter(new FileWriter(ff));
							
							for(String j : warningsp.get(i))
							{
								bw.write(j + "\n");
							}
							
							bw.close();
						}
						
						catch(IOException e)
						{
							e.printStackTrace();
						}
					}
					
					d.s("Writing Errors");
					
					try
					{
						BufferedWriter bw = new BufferedWriter(new FileWriter(er));
						
						for(String i : errors)
						{
							bw.write(i + "\n");
						}
						
						bw.close();
					}
					
					catch(IOException e)
					{
						e.printStackTrace();
					}
					
					d.s("Writing Warnings");
					
					try
					{
						BufferedWriter bw = new BufferedWriter(new FileWriter(wa));
						
						for(String i : warnings)
						{
							bw.write(i + "\n");
						}
						
						bw.close();
					}
					
					catch(IOException e)
					{
						e.printStackTrace();
					}
					
					d.s("COMPLETE");
				}
			}
		};
	}
}

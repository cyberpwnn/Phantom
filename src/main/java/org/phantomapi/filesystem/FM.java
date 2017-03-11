package org.phantomapi.filesystem;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.phantomapi.util.F;

public class FM
{
	public static void createAll(File root, File folder)
	{
		int si = root.listFiles().length;
		int m = 0;
		
		Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "------------------------------------------------------------");

		for(File i : root.listFiles())
		{
			if(i.isFile())
			{
				try
				{
					create(i, new File(folder, i.getName()));
					m++;
					String pc = F.pc(((double)m) / (double)si, 0);
					Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "Encrypted: " + ChatColor.GREEN + i.getPath() + ChatColor.LIGHT_PURPLE + " " + pc);
				}
				
				catch(IOException e)
				{

				}
				
			}
		}
		
		Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "------------------------------------------------------------");
	}
	
	public static void parseAll(File root, File folder)
	{
		int si = root.listFiles().length;
		int m = 0;
		
		Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "------------------------------------------------------------");

		for(File i : root.listFiles())
		{
			if(i.isFile())
			{
				try
				{
					parse(i, new File(folder, i.getName()));
					m++;
					String pc = F.pc(((double)m) / (double)si, 0);
					Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "Decrypted: " + ChatColor.GREEN + i.getPath() + ChatColor.LIGHT_PURPLE + " " + pc);
				}
				
				catch(IOException e)
				{
					e.printStackTrace();
				}
				
			}
		}
		
		Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "------------------------------------------------------------");
	}
	
	public static void create(File in, File out) throws IOException
	{
		String data = Base64.encodeFromFile(in.getPath());
		FileOutputStream fos = new FileOutputStream(out);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		GZIPOutputStream gzo = new GZIPOutputStream(baos);
        gzo.write(data.getBytes("UTF-8"));
        gzo.close();
        out.delete();
        out.createNewFile();
        fos.write(baos.toByteArray());
        fos.close();
	}
	
	public static void parse(File in, File out) throws IOException
	{
		Path path = Paths.get(in.getPath());
		GZIPInputStream gzi = new GZIPInputStream(new ByteArrayInputStream(Files.readAllBytes(path)));
        BufferedReader br = new BufferedReader(new InputStreamReader(gzi, "UTF-8"));
        String data = "";
        String line;
        
        while((line = br.readLine()) != null)
        {
            data += line;
        }
        
        br.close();
        Base64.decodeToFile(data, out.getPath());
	}
}

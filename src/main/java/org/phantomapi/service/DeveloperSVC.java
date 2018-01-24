package org.phantomapi.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.phantomapi.Phantom;

import phantom.dispatch.PD;
import phantom.lang.GList;
import phantom.pawn.Name;
import phantom.pawn.Register;
import phantom.pawn.Singular;
import phantom.pawn.Start;
import phantom.pawn.Stop;
import phantom.service.IService;
import phantom.util.developer.Developer;
import phantom.util.metrics.Documented;

/**
 * Hive service for storing data on blocks, entities, chunks, and worlds
 *
 * @author cyberpwn
 */
@Documented
@Register
@Name("SVC Hive")
@Singular
public class DeveloperSVC implements IService
{
	private GList<Developer> developers;

	public Developer addDeveloper(String name, int number) throws NoSuchAlgorithmException, IOException
	{
		Developer d = new Developer(name, number);
		developers.add(d);
		File f = new File(Phantom.getDataFolder(), "developers");
		f.mkdirs();
		File g = new File(f, name + ".phak");
		FileOutputStream fos = new FileOutputStream(g);
		fos.write(d.getKey().getKey());
		fos.close();
		PD.l("Developer " + name + " added to keychain.");

		return d;
	}

	@Start
	public void start()
	{
		developers = new GList<Developer>();
		File f = new File(Phantom.getDataFolder(), "developers");
		f.mkdirs();

		for(File i : f.listFiles())
		{
			if(i.getName().endsWith(".phak"))
			{
				try
				{
					FileInputStream fin = new FileInputStream(i);
					byte[] d = new byte[(int) i.length()];
					fin.read(d);
					fin.close();
					developers.add(new Developer(d));
					PD.l("Developer " + i.getName().split("\\.")[0] + " added to keychain.");
				}

				catch(Exception e)
				{

				}
			}
		}
	}

	@Stop
	public void stop()
	{

	}
}

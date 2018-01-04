package org.phantomapi.service;

import java.io.File;

import org.phantomapi.Phantom;

import phantom.dispatch.PD;
import phantom.lang.GList;
import phantom.lang.GMap;
import phantom.lang.format.F;
import phantom.pawn.Name;
import phantom.pawn.Singular;
import phantom.pawn.Start;
import phantom.pawn.Stop;
import phantom.service.IService;
import phantom.util.metrics.Anchor;
import phantom.util.metrics.JarScanner;

/**
 * Class anchor service used for anchoring classes, and crawling jars
 *
 * @author cyberpwn
 */
@Name("SVC Class Anchor")
@Singular
public class ClassAnchorService implements IService
{
	private GMap<String, GList<Class<?>>> anchors;

	@Start
	public void start()
	{
		anchors = new GMap<String, GList<Class<?>>>();
	}

	@Stop
	public void stop()
	{

	}

	/**
	 * Get all tags found in crawled jars
	 *
	 * @return a list of anchor tags
	 */
	public GList<String> getAnchorTags()
	{
		return anchors.k();
	}

	/**
	 * Get anchored classes for the given tag
	 *
	 * @param anchorTag
	 *            the anchor tag
	 * @return a list of classes or empty list
	 */
	public GList<Class<?>> getAnchoredClasses(String anchorTag)
	{
		if(!anchors.containsKey(anchorTag))
		{
			return new GList<Class<?>>();
		}

		return anchors.get(anchorTag);
	}

	/**
	 * Crawl a jar for anchors
	 *
	 * @param jarFile
	 *            the jar file to crawl
	 */
	public void crawl(File jarFile)
	{
		try
		{
			JarScanner scanner = new JarScanner(jarFile);
			scanner.scan();
			int a = 0;
			int c = 0;
			int s = 0;

			for(Class<?> i : scanner.getClasses())
			{
				s++;

				if(i.isAnnotationPresent(Anchor.class))
				{
					String id = i.getDeclaredAnnotation(Anchor.class).value();
					c++;

					if(!anchors.containsKey(id))
					{
						a++;
						anchors.put(id, new GList<Class<?>>());
					}

					anchors.get(id).add(i);
				}
			}

			PD.l("Crawled " + jarFile.getPath() + " Found " + a + " anchors in " + F.f(c) + " anchored classes out of " + F.f(s) + " scanned classes.");
		}

		catch(Throwable e)
		{
			Phantom.kick(e);
		}
	}
}

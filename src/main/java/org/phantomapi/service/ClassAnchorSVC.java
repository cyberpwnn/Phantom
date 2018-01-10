package org.phantomapi.service;

import java.io.File;
import java.lang.annotation.Annotation;

import org.phantomapi.Phantom;

import phantom.dispatch.PD;
import phantom.event.PhantomJarScannedEvent;
import phantom.lang.GList;
import phantom.lang.GMap;
import phantom.lang.format.F;
import phantom.pawn.Name;
import phantom.pawn.Singular;
import phantom.pawn.Start;
import phantom.pawn.Stop;
import phantom.service.IService;
import phantom.util.metrics.Anchor;
import phantom.util.metrics.Documented;
import phantom.util.metrics.JarScanner;

/**
 * Class anchor service used for anchoring classes, and crawling jars
 *
 * @author cyberpwn
 */
@Documented
@Name("SVC Class Anchor")
@Singular
public class ClassAnchorSVC implements IService
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
			int d = 0;

			GMap<String, GList<Class<?>>> dta = new GMap<String, GList<Class<?>>>();

			for(Class<?> i : scanner.getClasses())
			{
				s++;

				if(i.isAnnotationPresent(Documented.class))
				{
					d++;
				}

				boolean found = false;
				boolean annot = Annotation.class.isAssignableFrom(i);

				if(annot)
				{
					continue;
				}

				if(i.isAnnotationPresent(Anchor.class))
				{
					String id = i.getDeclaredAnnotation(Anchor.class).value();
					c++;

					if(!anchors.containsKey(id))
					{
						a++;
						anchors.put(id, new GList<Class<?>>());
						dta.put(id, new GList<Class<?>>());
					}

					anchors.get(id).add(i);
					dta.get(id).add(i);
					found = true;
				}

				if(!found)
				{
					for(Annotation j : i.getDeclaredAnnotations())
					{
						Class<? extends Annotation> type = j.annotationType();

						if(type.isAnnotationPresent(Anchor.class))
						{
							String id = type.getAnnotation(Anchor.class).value();
							c++;

							if(!anchors.containsKey(id))
							{
								a++;
								anchors.put(id, new GList<Class<?>>());
								dta.put(id, new GList<Class<?>>());
							}

							anchors.get(id).add(i);
							dta.get(id).add(i);
						}
					}
				}
			}

			PD.l("Crawled " + jarFile.getPath() + " Found " + a + " anchors in " + F.f(c) + " anchored classes out of " + F.f(s) + " scanned classes.");
			PD.l("Found " + F.f(d) + " of " + F.f(s) + " documented classes (" + F.pc((double) (d) / (double) (s)) + ")");
			Phantom.callEvent(new PhantomJarScannedEvent(jarFile, dta));
		}

		catch(Throwable e)
		{
			Phantom.kick(e);
		}
	}
}

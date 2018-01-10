package org.phantomapi.service;

import java.lang.reflect.InvocationTargetException;

import org.phantomapi.Phantom;
import org.phantomapi.nmsw.NMSWX;

import phantom.dispatch.PD;
import phantom.nms.INMSWrapper;
import phantom.nms.NMSPackage;
import phantom.pawn.DeployableService;
import phantom.pawn.Name;
import phantom.pawn.Singular;
import phantom.pawn.Start;
import phantom.pawn.Stop;
import phantom.service.IService;
import phantom.util.metrics.Documented;

/**
 * Class anchor service used for anchoring classes, and crawling jars
 *
 * @author cyberpwn
 */
@Documented
@Name("SVC NMS Binder")
@Singular
public class NMSBinderSVC implements IService
{
	@DeployableService
	private INMSWrapper wrapper;

	@Start
	public void start()
	{
		try
		{
			rebind();
		}

		catch(Throwable e)
		{
			PD.f("Could not bind NMSX wrapper");
			Phantom.kick(e);
		}
	}

	@Stop
	public void stop()
	{

	}

	private void rebind() throws NoSuchFieldException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException
	{
		String nmsVersion = Phantom.getNMSPackageVersion();

		for(Class<?> i : Phantom.getAnchors("phantom-nmsw"))
		{
			if(i.getDeclaredAnnotation(NMSPackage.class).value().equals(nmsVersion))
			{
				wrapper = (INMSWrapper) i.getConstructor().newInstance();
				activateWrapper();
				return;
			}
		}

		rebindUAW();
	}

	private void rebindUAW() throws NoSuchFieldException, SecurityException
	{
		wrapper = new NMSWX();
		activateWrapper();
	}

	private void activateWrapper() throws NoSuchFieldException, SecurityException
	{
		PD.l("Using NMS Wrapper: " + wrapper.getPackageVersion());
		Phantom.activate(wrapper);
		Phantom.claim(this, wrapper, this.getClass().getDeclaredField("wrapper"));
	}
}

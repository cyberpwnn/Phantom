package org.phantomapi.nmsw;

import phantom.net.Protocol;
import phantom.nms.NMSPackage;
import phantom.nms.NMSWrapper;
import phantom.pawn.Name;
import phantom.pawn.Singular;
import phantom.pawn.Start;
import phantom.pawn.Stop;
import phantom.util.metrics.Documented;

/**
 * 1.9.[3/4] NMS Wrapper<br/>
 * > Supports 1.9.4<br/>
 * > Supports 1.9.3
 *
 * @author cyberpwn
 */
@Documented
@Singular
@Name("NMS Wrapper 1.9.[3/4]")
@NMSPackage("v1_9_R2")
public class NMSW9_2 extends NMSWrapper
{
	public NMSW9_2()
	{
		super(Protocol.R1_9_3.to(Protocol.R1_9_4));
	}

	@Start
	public void onStart()
	{

	}

	@Stop
	public void onStop()
	{

	}
}

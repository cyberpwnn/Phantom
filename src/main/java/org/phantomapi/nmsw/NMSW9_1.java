package org.phantomapi.nmsw;

import phantom.net.Protocol;
import phantom.nms.NMSPackage;
import phantom.nms.NMSWrapper;
import phantom.pawn.Name;
import phantom.pawn.Singular;
import phantom.pawn.Start;
import phantom.pawn.Stop;
import phantom.util.metrics.Anchor;
import phantom.util.metrics.Documented;

/**
 * 1.9.[0/1/2] NMS Wrapper<br/>
 * > Supports 1.9.2<br/>
 * > Supports 1.9.1<br/>
 * > Supports 1.9
 *
 * @author cyberpwn
 */
@Documented
@Singular
@Anchor("phantom-nmsw")
@Name("NMS Wrapper 1.9.[0/1/2]")
@NMSPackage("v1_9_R1")
public class NMSW9_1 extends NMSWrapper
{
	public NMSW9_1()
	{
		super(Protocol.R1_9.to(Protocol.R1_9_2));
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

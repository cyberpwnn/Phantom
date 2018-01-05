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
 * 1.8.X NMS Wrapper<br/>
 * > Supports 1.8.9<br/>
 * > Supports 1.8.8<br/>
 * > Supports 1.8.7<br/>
 * > Supports 1.8.6<br/>
 * > Supports 1.8.5<br/>
 * > Supports 1.8.4<br/>
 * > Supports 1.8.3<br/>
 * > Supports 1.8.2<br/>
 * > Supports 1.8.1<br/>
 * > Supports 1.8
 *
 * @author cyberpwn
 */
@Documented
@Singular
@Name("NMS Wrapper 1.8.X")
@NMSPackage("v1_8_R3")
public class NMSW8 extends NMSWrapper
{
	public NMSW8()
	{
		super(Protocol.R1_8.to(Protocol.R1_8_9));
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

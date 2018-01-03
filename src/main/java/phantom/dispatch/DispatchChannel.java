package phantom.dispatch;

import phantom.text.C;

public enum DispatchChannel
{
	INFO(C.WHITE),
	WARN(C.YELLOW),
	FAIL(C.RED),
	VERBOSE(C.LIGHT_PURPLE);
	
	private C text;
	
	private DispatchChannel(C text)
	{
		this.text = text;
	}
	
	public String toString()
	{
		return text.toString();
	}
}

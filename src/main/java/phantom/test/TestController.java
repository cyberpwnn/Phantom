package phantom.test;

import phantom.annotation.Async;
import phantom.annotation.Control;
import phantom.annotation.Disable;
import phantom.annotation.Enable;
import phantom.annotation.Instance;
import phantom.annotation.MasterController;
import phantom.annotation.Sync;
import phantom.annotation.Tick;

@MasterController
public class TestController
{
	@Control
	public SomeSubController subController;

	@Instance
	TestController instance;

	@Enable
	public void enable()
	{

	}

	@Disable
	public void disable()
	{

	}

	@Sync
	@Tick(5)
	public void onTick()
	{

	}

	@Async
	@Tick(0)
	public void onTickAsync()
	{

	}
}

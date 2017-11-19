package phantom.test;

import phantom.annotation.Async;
import phantom.annotation.Controller;
import phantom.annotation.Disable;
import phantom.annotation.Enable;
import phantom.annotation.Instance;
import phantom.annotation.Sync;
import phantom.annotation.Tick;

@Controller
public class SomeSubController
{
	@Instance
	public static SomeSubController instance;

	@Enable
	public void enable()
	{
		System.out.println("Sub Called Enable");
	}

	@Disable
	public void disable()
	{

	}

	@Sync
	@Tick(20)
	public void onTick()
	{

	}

	@Async
	@Tick(40)
	public void onTickAsync()
	{

	}
}

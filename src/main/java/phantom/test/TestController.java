package phantom.test;

import phantom.annotation.Async;
import phantom.annotation.Control;
import phantom.annotation.Disable;
import phantom.annotation.Enable;
import phantom.annotation.Instance;
import phantom.annotation.MasterController;
import phantom.annotation.Sync;
import phantom.annotation.Tick;

// The top level controllers are defined with @MasterController
@MasterController
public class TestController
{
	// Define a subcontroller
	@Control
	public SomeSubController subController;

	// This is an instance of ... well... This
	@Instance
	TestController instance;

	@Async
	@Enable
	public void enable()
	{
		// Using @Async will call this method off of the main thread
		// Called when all subcontrollers have enabled
	}

	@Disable
	public void disable()
	{
		// Called when all subcontrollers have disabled
	}

	@Sync
	@Tick(5)
	public void onTick()
	{
		// Called 4 times a second (every 5 ticks) ON the main thread
	}

	@Async
	@Tick(0)
	public void onTickAsync()
	{
		// Called 20 times a second off the main thread
	}
}

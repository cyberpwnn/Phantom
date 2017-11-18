package phantom.test;

import phantom.annotation.Control;
import phantom.annotation.Controller;
import phantom.annotation.Disable;
import phantom.annotation.Enable;
import phantom.annotation.Instance;

// Since this isnt a top level controller
// It's defined as @Controller
@Controller
public class SomeSubController
{
	// Oh look, this sub controller has a sub-sub controller
	@Control
	private SubController2 subController2;

	// Instance of this controller
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
}

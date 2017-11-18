package phantom.test;

import phantom.annotation.Controller;
import phantom.annotation.Disable;
import phantom.annotation.Enable;
import phantom.annotation.Instance;

@Controller
public class SubController2
{
	@Instance
	SubController2 instance;

	@Enable
	public void enable()
	{
		System.out.println("Sub2 Called Enable");
	}

	@Disable
	public void disable()
	{

	}
}

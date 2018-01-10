package phantom.sched;

import org.phantomapi.Phantom;
import org.phantomapi.service.ThreadPoolSVC;

public abstract class A extends Execution
{
	public A()
	{
		Phantom.getService(ThreadPoolSVC.class).run(this);
	}

	@Override
	public abstract void run();
}

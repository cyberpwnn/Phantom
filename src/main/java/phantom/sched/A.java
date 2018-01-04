package phantom.sched;

import org.phantomapi.Phantom;
import org.phantomapi.service.ThreadPoolService;

public abstract class A extends Execution
{
	public A()
	{
		Phantom.getService(ThreadPoolService.class).run(this);
	}

	@Override
	public abstract void run();
}

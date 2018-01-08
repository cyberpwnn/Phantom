package phantom.event;

import org.bukkit.event.Cancellable;

public class PhantomCancellableEvent extends PhantomEvent implements Cancellable
{
	private boolean cancelled;

	@Override
	public boolean isCancelled()
	{
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel)
	{
		this.cancelled = cancel;
	}
}

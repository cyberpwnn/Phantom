package com.volmit.phantom.api.intercom;

import java.io.Serializable;

public interface IntercomCoordinator
{
	public IntercomCoordinator addMonitor(CoordinatorMonitor monitor);

	public IntercomCoordinator removeMonitor(CoordinatorMonitor monitor);

	public <T extends Serializable, X extends Serializable> IntercomCoordinator accept(Class<? extends T> c, IntercomServerHelper<T, X> h);

	public CoordinatedIntercom getChannel(String s);

	public boolean hasChannel(String s);

	public void shutdown();
}

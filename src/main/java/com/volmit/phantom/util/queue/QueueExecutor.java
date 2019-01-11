package com.volmit.phantom.util.queue;

public interface QueueExecutor<T>
{
	public QueueExecutor<T> queue(Queue<T> t);

	public Queue<T> getQueue();

	public QueueExecutor<T> start();

	public QueueExecutor<T> stop();

	public QueueExecutor<T> update();

	public QueueExecutor<T> async(boolean async);

	public QueueExecutor<T> interval(int ticks);
}

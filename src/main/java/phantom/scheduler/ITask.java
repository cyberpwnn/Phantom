package phantom.scheduler;
public interface ITask
{
	public int getId();

	public void run();

	public boolean isRepeating();

	public double getComputeTime();

	public double getTotalComputeTime();

	public double getActiveTime();

	public boolean hasCompleted();
}
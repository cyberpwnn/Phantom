package phantom.pawn;

@Controller("Test Controller") // Define its controlled
@Registered // Define it needs to be bukkit event registered
public class TestPawn
{
	@Start
	public void start()
	{
		// When started, event listeners are registered
	}

	@Async
	@Stop
	public void stop()
	{
		// When stopped (called async) listener unregistered
	}

	@Tick(2)
	public void syncEveryOtherTick()
	{
		// Ticks sync every other tick while running
	}

	@Async
	@Tick(0)
	public void asyncEveryTick()
	{
		// Ticks async every tick alongside main thread while running
	}
}

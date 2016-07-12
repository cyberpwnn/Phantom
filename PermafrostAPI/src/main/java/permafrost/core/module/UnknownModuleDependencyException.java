package permafrost.core.module;

public class UnknownModuleDependencyException extends Exception
{
	private static final long serialVersionUID = 1L;
	
	public UnknownModuleDependencyException(Module module, Class<? extends Module> c)
	{
		super(module.getClass().toString() + ": UNKNOWN DEPENDENCY (" + c.toString() + ")");
	}
}

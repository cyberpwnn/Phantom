package com.volmit.phantom.plugin;

public class R
{
	private Runnable r;
	private R child;
	private R parent;
	private boolean async;

	public R(R parent)
	{
		this();
		this.parent = parent;
	}

	public R()
	{
		async = false;
	}

	public R sync(Runnable r)
	{
		async = false;
		this.r = r;
		return this;
	}

	public R async(Runnable r)
	{
		async = true;
		this.r = r;
		return this;
	}

	public R then()
	{
		R nt = new R(this);
		child = nt;
		return child;
	}

	public void start()
	{
		if(parent != null)
		{
			parent.start();
		}

		else if(r != null)
		{
			if(async)
			{
				new A()
				{
					@Override
					public void run()
					{
						r.run();
						callChild();
					}
				};
			}

			else
			{
				new S()
				{
					@Override
					public void run()
					{
						r.run();
						callChild();
					}
				};
			}
		}

		else
		{
			callChild();
		}
	}

	protected void callChild()
	{
		if(child != null)
		{
			child.parent = null;
			child.start();
		}
	}
}

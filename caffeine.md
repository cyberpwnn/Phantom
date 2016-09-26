# Get Started Fast
This guide is designed to get you started quickly in phantom. Bigger subjects like handling controllers and more should be looked at in the actual documentation. This guide is designed to help with finding what utilities can help you when needed.

* [Phantom Core](#phantom-core)
 * [Event Listeners](#event-listeners)
 * [Networked Servers](#networked-servers)
 * [Formatting](#formatting)
 * [Math](#math)
 * [Dispatcher](#dispatcher)
 * [Timers](#timers)
* [Threading & Scheduling](#threading--scheduling)
 * [Schedulers](#schedulers)
 * [Asynchronous Executions](#asynchronous-executions)
 * [Multithreading](#multithreading)

## Phantom Core
The core contains several useful apis and ultilities for you to use while developing. 

### Event Listeners
You can easily register and unregister listeners. Remember, Controllers by default are event listeners and probobly registered.
1. Implement Listener like normal
2. Use These for speed
``` java
Phantom.instance().registerListener(Listener);
Phantom.instance().unRegisterListener(Listener);
```
from [ControllablePlugin](http://cyberpwnn.github.io/Phantom/org/phantomapi/construct/ControllablePlugin.html#registerListener-org.bukkit.event.Listener-)

### Networked Servers
You can grab a virtual instance of the bungeecord network which is updated live. 
``` java
Network n = Phantom.getBungeeNetwork();

for(NetworkedServer i : n.getServers())
{
	if(i.isRemote())
	{
		i.getPlayers().size();
		i.getName();
		i.sendPlayer(playerFromThisServer);
	}
}
```

from [Network](http://cyberpwnn.github.io/Phantom/org/phantomapi/network/Network.html)
and [NetworkedServer](http://cyberpwnn.github.io/Phantom/org/phantomapi/network/NetworkedServer.html)

### Formatting
You can format text in many ways with the F class.

``` java
//Basic Formatting
F.f(1024); //Returns 1,024
F.f(3.3333, 2); //Returns 3.34 (2 decimals)
F.fileSize(1024); //Returns 1 KB
F.fd(1.03, 5); //Returns 1.03000 (force 5 decimals)
F.color("&athis is green"); //Returns C.GREEN + "this is green"
F.b(new BigInteger("1121231223423423111442")); //Returns suffixed name

//Roman numerals
M.toRoman(15); //Returns XV
M.fromRoman("XI"); //Returns 11 (int)
```
from [F](http://cyberpwnn.github.io/Phantom/org/phantomapi/util/F.html)
and [M](http://cyberpwnn.github.io/Phantom/org/phantomapi/util/M.html)

### Math
There are many things that can be done with math.

``` java
M.ms(); //Current Time Milliseconds
M.ns(); //Current Time Nanoseconds
M.avg(new GList<Double>().qadd(0.0).qadd(10.0)); //Returns 5
M.max(-4, 4, 77); //Returns 77
M.min(2, 7, 0, 55, -1); //Returns -1
M.r(0.6); //Returns true 60% of the time (from 0 to 1)

//JS Values
double value = M.evaluate("Math.sin(Math.random() * 4) - 1");

//Expressions
Formula f = new Formula("($0 * $1) / $2");
f.evaluate(2.5, 2.5, 1.4); //Effective: (2.5 * 2.5) / 1.4
f.evaluate(21.0, 1.5, 1.423); //Effective: (21.0 * 1.5) / 1.423
```
from [M](http://cyberpwnn.github.io/Phantom/org/phantomapi/util/M.html)
and [Formula](http://cyberpwnn.github.io/Phantom/org/phantomapi/util/Formula.html)

### Dispatcher
You can dispatch information to the console anytime within controllers or outside of controllers. **Async Safe**

``` java
//Built into controllers and controllable plugins
i("Info! (White. Not Gray.)");
s("Success! (Green)");
w("WARNING! (Yellow)");
f("FAILURE! (Red)");
v("Verbose! (Light Purple)");
o("OVERBOSE! (Aqua)");

//Use it anywhere
D d = new D("Custom Tag");

d.i("Info! (White. Not Gray.)");
d.s("Success! (Green)");
d.w("WARNING! (Yellow)");
d.f("FAILURE! (Red)");
d.v("Verbose! (Light Purple)");
d.o("OVERBOSE! (Aqua)");
```

from [D](http://cyberpwnn.github.io/Phantom/org/phantomapi/util/D.html)

### Timers
Timers are a great way to keep track of what you are doing and how long the execution takes. It is essential for accuracy, so we measure by nanoseconds, for decimal milliseconds. Here is an example of how to use the timer.

``` java
T t = new T()
{
	@Override
	public void onStop(long nsTime, double msTime)
	{
		//Print ms.000 decimal format
		System.out.println("Took " + F.f(msTime, 3));
	}
};	

//DO LONG WORK 

//Runnable is called and reports time
t.stop();
```

#### Simple Timers
Some times you need simpler timers for more complex tasks
``` java
Timer t = new Timer();

t.start();
//Do work
t.stop();

//Format the print msg
String time = F.nsMs(t.getTime()) + " ms";
System.out.println("Took " + time);
```

from [T](http://cyberpwnn.github.io/Phantom/org/phantomapi/util/T.html)
and [Timer](http://cyberpwnn.github.io/Phantom/org/phantomapi/util/Timer.html)

## Threading & Scheduling
With phantom, you can dive in and out of multiple async threads, use ticked schedulers with simplicity and much more.

### Schedulers
You can now run quick schedulers with ease.
#### Run Later
``` java
new TaskLater(20)
{
	@Override
	public void run()
	{
		//Run this 20 ticks in the future
	}
};
```

#### Run Repetedly
``` java
new Task(20)
{
	@Override
	public void run()
	{
		//Run this every 20 ticks until cancelled
	}
};
```

from [Task](http://cyberpwnn.github.io/Phantom/org/phantomapi/sync/Task.html)
and [TaskLater](http://cyberpwnn.github.io/Phantom/org/phantomapi/sync/TaskLater.html)

### Asynchronous Executions
You can simply dive in and out of async threads through execution anywhere.

``` java
//Start something 
new A()
{
	@Override
	public void async()
	{
		//Do something that will take a while
		//Then, once complete, break off into sync
		
		new S()
		{
			@Override
			public void sync()
			{
				//Do something on the sync main thread
				//Then once complete just finish off
			}
		};
		
		//Or resume execution on async thread after sync finished (about 50ms)
	}
};
```

from [A](http://cyberpwnn.github.io/Phantom/org/phantomapi/async/A.html)
and [S](http://cyberpwnn.github.io/Phantom/org/phantomapi/sync/S.html)

### Multithreading

Use multithreading to plow through queues async.

#### Set up the queue
Create the queue
``` java
//Let's pretend there are loads of strings in there
GList<String> data = new GList<String>();

//Let's create a queue that
//1. Can process strings
//2. Can have up to 12 working threads
MultithreadedQueueExecutor<String> queue = new MultithreadedQueueExecutor<String>(12)
{
	@Override
	public void onProcess(String t)
	{
		//Do something with the next string (process it)
		t.length();
	}
};

//Let's add our data
for(String i : data)
{
	queue.queue(i);
}
```

#### Tick the queue
You need to tick the queue at any interval you like to dispatch new threads and essentially beat the multithreaded heart.

``` java
new Task(0)
{
	@Override
	public void run()
	{
		//Called every tick
		//Create new threads and take several items out of the queue
		queue.dispatch();
	}
};
```

from [MultithreadedQueueExecutor](http://cyberpwnn.github.io/Phantom/org/phantomapi/async/MultithreadedQueueExecutor.html)


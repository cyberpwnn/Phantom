# Get Started Fast
This guide is designed to get you started quickly in phantom. Bigger subjects like handling controllers and more should be looked at in the actual documentation. This guide is designed to help with finding what utilities can help you when needed.

* [Phantom Core](#phantom-core)
 * [Event Listeners](#event-listeners)
 * [Networked Servers](#networked-servers)
 * [Schedulers](#schedulers)
 * [Formatting](#formatting)

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

from [TaskLater](http://cyberpwnn.github.io/Phantom/org/phantomapi/sync/TaskLater.html)

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

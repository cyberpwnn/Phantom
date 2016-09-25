# Get Started Fast
This guide is designed to get you started quickly in phantom. Bigger subjects like handling controllers and more should be looked at in the actual documentation. This guide is designed to help with finding what utilities can help you when needed.

* Phantom Core
 * Event Listeners

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

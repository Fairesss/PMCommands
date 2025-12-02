# PMCommands

A lightweight library designed to simplify the creation of custom Minecraft commands using Paper.


## ⚙️ Limitations
- Only supports static methods
- Commands must explicitly accept the command context \
this means all CommandClass must be like this:
```java
public static void call(CommandContext<CommandSourceStack> ctx)
```

##  Example

```java
package org.example;

public class Example extends JavaPlugin {

    @Override
    public void onEnable() {
        CommandParser cr = new CommandParser("org.example");
        CommandRecorder  r = new CommandRecorder(this, cr.getCommandTree(), "example");
        r.register();
    }
}


```

```java

package org.example;

@CommandClass(alias = "call")
public class CallCommand {

    @CommandCall
    public static void call(CommandContext<CommandSourceStack> ctx) {
        var player = ctx.getSource().getExecutor();
        player.sendMessage(MiniMessage.miniMessage().deserialize("Calling:  " + player.getName()));
    }
}
```
this makes a command /example call call;
full example can be seen [here](https://github.com/Fairesss/PMCommands/tree/master/app/src/main/java/org/example)

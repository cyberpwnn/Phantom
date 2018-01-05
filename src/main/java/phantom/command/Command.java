package phantom.command;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import phantom.util.metrics.Anchor;

@Anchor("phantom-command")
@Retention(RUNTIME)
@Target(TYPE)
public @interface Command
{

}

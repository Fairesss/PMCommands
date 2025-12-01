package it.fair.McCommands;



import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;


@Target(METHOD)
@Retention(RUNTIME)
public @interface CommandCall {}

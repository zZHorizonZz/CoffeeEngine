package com.horizon.engine.input.other;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InputHandler {

    /**
     * The name of the input method.
     */
    String name();

    /**
     * Id of the input.
     */
    int input();

    /**
     * Type of key action.
     */
    InputType inputType();
}

package org.solq.fm.common.console.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.solq.fm.common.console.anno.ConsoleCommand;
import org.solq.fm.common.convert.StringConvertJavaService;

public class MethodCommand implements Command {

    private String name;
    private String description;

    private final Object target;
    private final Method method;
    private final Class<?>[] types;

    public MethodCommand(Object target, Method method) {
	this.target = target;
	this.method = method;
	this.types = method.getParameterTypes();

	ConsoleCommand annotation = method.getAnnotation(ConsoleCommand.class);
	this.name = annotation.value().trim();
	this.description = annotation.description().trim();

	method.setAccessible(true);
    }

    @Override
    public String name() {
	return name;
    }

    @Override
    public String description() {
	return description;
    }

    @Override
    public void execute(String[] arguments) {
	Object[] args = new Object[types.length];
	for (int i = 0; i < arguments.length; i++) {
	    args[i] = StringConvertJavaService.convert(types[i], arguments[i]);
	}
	try {
	    method.invoke(target, args);
	} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
	    e.printStackTrace();
	}
    }

}

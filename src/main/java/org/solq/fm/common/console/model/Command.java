package org.solq.fm.common.console.model;

public interface Command {

    public String name();

    public String description();

    public void execute(String[] arguments);
}

package org.solq.fm.common.console;

public interface Command {

    public String name();

    public String description();

    public void execute(String[] arguments);
}

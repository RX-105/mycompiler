package io.n0sense.mycompiler.commands;

public abstract class AbstractCommand {
    public abstract void run(String[] args, int index);

    public abstract void showHelp();
}

package io.n0sense.mycompiler.commands;

public class HelpCommand extends AbstractCommand {
    @Override
    public void run(String[] args, int index) {
        showHelp();
    }

    @Override
    public void showHelp() {
        String help = """
                        Demo compiler, exp#1.
                        Available commands:
                         analyze: Analyze code from give file, and output analyze result.
                                                    
                        For example:
                         mycompiler analyze -i main.c -o main.o
                        """;
        System.out.println(help);
    }
}

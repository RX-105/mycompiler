package io.n0sense.mycompiler;

import io.n0sense.mycompiler.commands.AnalyzeCommand;
import io.n0sense.mycompiler.commands.HelpCommand;
import io.n0sense.mycompiler.constant.ErrorConstants;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

public class Main {
    public static void main(String[] args) {
        AnsiConsole.systemInstall();
        if (args.length == 0)
            System.err.println(Ansi.ansi().fgRed().a(ErrorConstants.noArgSpecified).reset());
        else {
            if (args[0].equals("-h") || args[0].equals("--help")) {
                new HelpCommand().run(args, 1);
            }
            if (args[0].equals("analyze")) {
                new AnalyzeCommand().run(args, 1);
            }
        }
    }
}

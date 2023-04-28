package io.n0sense.mycompiler;

import io.n0sense.mycompiler.commands.AnalyzeCommand;
import io.n0sense.mycompiler.commands.HelpCommand;
import org.fusesource.jansi.AnsiConsole;

import java.util.Locale;
import java.util.ResourceBundle;

public class Main {
    public static void main(String[] args) {
        Locale locale_en_US = new Locale("en", "US");
        ResourceBundle bundle = ResourceBundle.getBundle("bundle", locale_en_US);
        System.out.println(bundle.getString("general_help"));
        AnsiConsole.systemInstall();
        if (args.length == 0)
            new HelpCommand().run(args, -1);
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

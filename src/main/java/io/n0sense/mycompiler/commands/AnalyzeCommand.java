package io.n0sense.mycompiler.commands;

import io.n0sense.mycompiler.parser.LexicalAnalyzer;
import org.fusesource.jansi.Ansi;

import java.io.File;
import java.io.IOException;

import static io.n0sense.mycompiler.constant.ErrorConstants.*;

public class AnalyzeCommand extends AbstractCommand{
    String inFileName, outFileName;
    public void run(String[] args, int index) {
        for (int i = index; i < args.length; i++) {
            if (args[i].startsWith("-h") || args[i].startsWith("--help")) {
                showHelp();
                System.exit(0);
            }
            if (args[i].startsWith("-i")) {
                if (args[i].length() == "-i".length()) {
                    inFileName = args[i + 1];
                    i += 1;
                } else {
                    inFileName = args[i].substring("-i".length());
                }
                continue;
            }
            if (args[i].startsWith("-o")) {
                if (args[i].length() == "-o".length()) {
                    outFileName = args[i + 1];
                    i += 1;
                } else {
                    outFileName = args[i].substring("-o".length());
                }
            }
        }
        if (inFileName == null || outFileName == null) {
            System.out.println(Ansi.ansi().fgRed().a("Error: No file specified!").reset());
            showHelp();
            System.exit(-1);
        }
        checkFile();
        try {
            new LexicalAnalyzer(inFileName, outFileName).process();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showHelp() {
        String helpText = bundle.getString("analyze_help");
        System.out.println(helpText);
    }

    private void checkFile() {
        File file = new File(inFileName);
        if (!file.exists() || file.isDirectory()) {
            System.err.printf(error + invalidFile.formatted(invalidFile));
            System.exit(-1);
        }
        file = new File(outFileName);
        if (!file.canWrite()) {
            System.err.println(error + cantWriteFile.formatted(outFileName));
            System.exit(-1);
        }
    }
}

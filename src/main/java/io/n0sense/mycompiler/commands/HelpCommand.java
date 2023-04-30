package io.n0sense.mycompiler.commands;

import io.n0sense.mycompiler.util.I18nUtil;

import java.util.ResourceBundle;

public class HelpCommand extends AbstractCommand {
    @Override
    public void run(String[] args, int index) {
        showHelp();
    }

    @Override
    public void showHelp() {
        ResourceBundle bundle = ResourceBundle.getBundle("bundle", I18nUtil.getLocale());
        String help = bundle.getString("general_help");
        System.out.println(help);
    }
}

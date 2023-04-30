package io.n0sense.mycompiler.constant;

import io.n0sense.mycompiler.util.I18nUtil;

import java.util.ResourceBundle;

public interface ErrorConstants {
    ResourceBundle bundle = ResourceBundle.getBundle("bundle", I18nUtil.getLocale());
    String error = bundle.getString("execution_error_header");
    String noArgSpecified = bundle.getString("no_arg_specified");
    String invalidFile = bundle.getString("invalid_file");
    String cantWriteFile = bundle.getString("cant_write_file");
    String invalidNumber = bundle.getString("invalid_number");
    String invalidIdentifier = bundle.getString("invalid_identifier");
    String bracketMismatched = bundle.getString("bracket_mismatched");
}

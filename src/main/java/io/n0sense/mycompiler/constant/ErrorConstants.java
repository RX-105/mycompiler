package io.n0sense.mycompiler.constant;

import java.util.Locale;
import java.util.ResourceBundle;

public interface ErrorConstants {
    ResourceBundle bundle = ResourceBundle.getBundle("bundle", Locale.getDefault());
    String error = bundle.getString("execution_error_header");
    String invalidFile = bundle.getString("invalid_file");
    String cantWriteFile = bundle.getString("cant_write_file");
    String invalidNumber = bundle.getString("invalid_number");
    String invalidIdentifier = bundle.getString("invalid_identifier");
    String bracketMismatched = bundle.getString("bracket_mismatched");
}

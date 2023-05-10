package io.n0sense.mycompiler.exception;

import static io.n0sense.mycompiler.constant.ErrorConstants.bundle;

public class SyntaxError extends Exception {
    public SyntaxError(int line, int col, String fragment, String lineContent) {
        super(bundle.getString("syntax_error").formatted(line, col, fragment) +
                lineContent + System.lineSeparator() +
                " ".repeat(col) + "^");
    }
}

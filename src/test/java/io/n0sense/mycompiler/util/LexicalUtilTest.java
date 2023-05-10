package io.n0sense.mycompiler.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

public class LexicalUtilTest {
    @Test
    void testBracketMatcher() {
        File testFile = new File("C:\\Users\\kazuha\\projects\\java-projects\\mycompiler\\source.c");
        boolean result = LexicalUtil.isBracketMatched(testFile);
        System.out.println("result = " + result);
        Assertions.assertTrue(result);
    }

    @Test
    void testSplit() {
        String line1 = "int main()";
        String line2 = "int main(int argv, char* argc)";
        System.out.println(LexicalUtil.split(line1));
        System.out.println(LexicalUtil.split(line2));
    }

    @Test
    void testRemoveComments() {
        File testFile = new File("C:\\Users\\kazuha\\projects\\java-projects\\mycompiler\\source.c");
        boolean res = LexicalUtil.removeComments(testFile);
        Assertions.assertTrue(res);
    }
}

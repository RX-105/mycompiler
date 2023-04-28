package io.n0sense.mycompiler.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SyntaxUtilTest {
    @Test
    void testBracketMatcher() {
        String testFile = "C:\\Users\\kazuha\\projects\\java-projects\\mycompiler\\source.c";
        boolean result = SyntaxUtil.isBracketMatched(testFile);
        System.out.println("result = " + result);
        Assertions.assertTrue(result);
    }

    @Test
    void testSplit() {
        String line1 = "int main()";
        String line2 = "int main(int argv, char* argc)";
        System.out.println(SyntaxUtil.split(line1));
        System.out.println(SyntaxUtil.split(line2));
    }
}

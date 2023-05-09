package io.n0sense.mycompiler.parser;

import io.n0sense.mycompiler.parser.automaton.DFA;
import io.n0sense.mycompiler.parser.automaton.RegexDFA;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class TestDFA {
    @Test
    void testDFA() {
        String startState = "a";
        HashSet<String> acceptStates = new HashSet<>(Arrays.asList("b", "c", "d", "e", "f", "g"));
        HashSet<String> setOfStates = new HashSet<>(Arrays.asList("a", "b", "c", "d", "e", "f", "g"));
        ArrayList<String[]> transition = new ArrayList<>();
        // c语言标识符。可以以字母和下划线开头
        transition.add(new String[] { "a", "[A-Za-z_]", "b" });
        transition.add(new String[] { "b", "[A-Za-z0-9_]", "b" });

        transition.add(new String[] {"a", "[0-9]", "c"});
        transition.add(new String[] {"b", "[0-9]", "c"});

        transition.add(new String[] {"a", "[-+*/]", "d"});
        transition.add(new String[] {"a", "[!<>=]", "e"});
        transition.add(new String[] {"e", "[=]", "f"});

        transition.add(new String[] {"a", "[,;{}()]", "g"});

        String input1 = "_ab1";
        String input2 = "1000";
        DFA myDFA = new RegexDFA(setOfStates, startState, acceptStates, transition);
        Assertions.assertTrue(myDFA.test(input1));
        Assertions.assertFalse(myDFA.test(input2));
    }
}

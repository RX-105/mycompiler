package io.n0sense.mycompiler.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class DFA {
    List<DFANode> exp1 = new ArrayList<>();

    public DFA() {
        exp1.add(new DFANode(0, ' ', x -> {
            if (x == ' ')
                return 0;
            else if (Character.isLetter(x))
                return 1;
            else if (Character.isDigit(x))
                return 3;
            else return -1;
        }));
    }

    static class DFANode {
        public Integer index;
        public Character chars;
        public Function<Character, Integer> parseNext;


        public DFANode(Integer index, Character chars, Function<Character, Integer> parseNext) {
            this.index = index;
            this.chars = chars;
            this.parseNext = parseNext;
        }
    }
}

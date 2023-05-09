package io.n0sense.mycompiler.parser.automaton;

import java.util.ArrayList;
import java.util.HashSet;

public class RegexDFA extends AbstractDFA{
    public HashSet<String> acceptStates;
    public RegexDFA(HashSet<String> setOfStates, String startState, HashSet<String> acceptStates, ArrayList<String[]> transition) {
        super();

        this.setOfStates = setOfStates;
        this.startState = startState;
        this.acceptStates = acceptStates;
        this.transition = transition;

        for (String stateName : this.setOfStates) {
            if (this.acceptStates.contains(stateName)) {
                Node node = new Node();
                node.changeToAcceptState();
                this.map.put(stateName, node);
            } else {
                this.map.put(stateName, new Node());
            }
        }

        for (String[] t : transition) {
            Node fromState = (Node) this.map.get(t[0]);
            String value = t[1];
            Node toState = (Node) this.map.get(t[2]);
            Edge eVal = new Edge(value, toState);
            fromState.addEdge(eVal);
            // 2023/05/09, n0sense:
            // A dot matches a single character in regex, which alters result.
            // Thus, this will be disabled
        }
    }

    class Node extends AbstractDFA.Node{
        @Override
        public Node goToNext(String c) {
            for (Edge edge : this.edges) {
                // test if arg c matches by regex
                if (c.replaceAll(edge.val, "").length() == 0) {
                    return (Node) edge.pointsTo;
                }
            }
            return null;
        }
    }
}

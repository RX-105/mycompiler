package io.n0sense.mycompiler.parser.automaton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

// Code referenced from https://github.com/artisan1218/Turing-Machine/blob/main/DFA.java
public abstract class AbstractDFA implements DFA{
    String input;
    HashSet<String> setOfStates;
    String startState;
    String acceptState;
    ArrayList<String[]> transition;
    HashMap<String, Node> map = new HashMap<>();

    AbstractDFA(HashSet<String> setOfStates, String startState, String acceptState, ArrayList<String[]> transition) {
        this.setOfStates = setOfStates;
        this.startState = startState;
        this.acceptState = acceptState;
        this.transition = transition;

        // 构造状态集
        for (String stateName : this.setOfStates) {
            if (stateName.equals(this.acceptState)) {
                Node node = new Node();
                node.changeToAcceptState();
                this.map.put(stateName, node);
            } else {
                this.map.put(stateName, new Node());
            }
        }

        for (String[] t : transition) {
            Node fromState = this.map.get(t[0]);
            String value = t[1];
            Node toState = this.map.get(t[2]);
            Edge eVal = new Edge(value, toState);
            fromState.addEdge(eVal);
            // this is to enable "." feature
            fromState.addEdge(new Edge(".", toState));
        }
    }

    public AbstractDFA() {
    }

    @Override
    public boolean test(String input) {
        this.input = input;
        boolean match = false;

        Node curr = this.map.get(this.startState);
        for (int i = 0; i < this.input.length(); i++) {
            String c = String.valueOf(input.charAt(i));
            curr = curr.goToNext(c);
            if (curr == null) {
                break;
            }
        }

        if (curr != null && curr.isAcceptState) {
            match = true;
        }

        return match;
    }

    class Node {
        ArrayList<Edge> edges;
        boolean isAcceptState;

        // a node can go to different edges
        Node() {
            this.edges = new ArrayList<>();
            this.isAcceptState = false;
        }

        public Node goToNext(String c) {
            for (Edge edge : this.edges) {
                // go to the next state if the current char matches or the val is dot
                if (edge.val.equals(c) || edge.val.equals(".")) {
                    return edge.pointsTo;
                }
            }
            return null;
        }

        public void addEdge(Edge e) {
            this.edges.add(e);
        }

        public void changeToAcceptState() {
            this.isAcceptState = true;
        }

        public boolean isAcceptState() {
            return this.isAcceptState;
        }

    }

    class Edge {
        String val;
        Node pointsTo;

        // an edge has a value and points to another state
        Edge(String v, Node to) {
            this.val = v;
            this.pointsTo = to;
        }
    }
}

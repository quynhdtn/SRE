package liir.sre.nfa;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by quynhdo on 07/11/16.
 * This class is used to represent a Fragment for building special non-deterministic finite automata
 * proposed in Thompson's construction theory
 * Each fragment consists of an input state and a list of output states
 */
public class Fragment {

    State start; // input state
    List<State> connectors = new ArrayList<State>(); // output states

    public Fragment(State start) {
        this.start = start;
    }

    public Fragment(State start, State out) {
        this.start = start;
        this.connectors.add(out);
    }

    public Fragment(State start, List<State> connectors) {
        this.start = start;
        this.connectors = connectors;
    }

    public State getStart() {
        return start;
    }

    public List<State> getConnectors() {
        return connectors;
    }

    public void setStart(State start) {
        this.start = start;
    }






}

package liir.sre.nfa;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by quynhdo on 04/11/16.
 * This class is used to represent a state of a special non-deterministic finite automata
 * proposed in Thompson's construction theory
 * Each state has a name and two output states
 */

public class State{
    char name;
    State out1;   // output 1
    State out2;  // output 2

    public final static char SPLIT = ':';  //we use ':' as the name of SPLIT state

    public final static char END = '$'; //'$' is used as the name of FINAL state

    public State(char name) {
        this.name = name;
    }

    public void setName(char name) {
        this.name = name;
    }

    public void setOut1(State out1) {
        this.out1 = out1;
    }

    public void setOut2(State out2) {
        this.out2 = out2;
    }

    public char getName() {

        return name;
    }

    public State getOut1() {
        return out1;
    }

    public State getOut2() {
        return out2;
    }


    /***
     * connect this state to another state, by default we connect s to out1,
     * if this is a split state then connect s to both out1 and out2
     * @param s
     */
    public void addOut(State s){
        if (this.out1 == null)
            this.out1 = s;
        if (this.name == SPLIT && this.out2 ==null) // split state
            this.out2 = s;
    }

    /***
     * check if this is a final state
     * @return true if this is a final state, false otherwise
     */
    public boolean isEnd(){
        return this.name == END;
    }

    /***
     * check if this is a SPLIT state
     * @return true if this is a split state, false otherwise
     */
    public boolean isSplit(){
        return this.name == SPLIT;
    }


}

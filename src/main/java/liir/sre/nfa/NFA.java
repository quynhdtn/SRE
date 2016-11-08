package liir.sre.nfa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

/**
 * Created by quynhdo on 04/11/16.
 * This class is used to convert a string to a corresponding NFA using Thompson's construction theory:
 * ref: https://swtch.com/~rsc/regexp/regexp1.html
 * To check if a string matches a regular expression,
 * we simulate the corresponding NFA when scanning the given string.
 * If the NFA enter a FINAL state then the string and the expression match.
 *
 */
public class NFA {

    /***
     * convert a letter to corresponding fragment
     * @param c input character
     * @return corresponding fragment
     */
    public static Fragment convertLetter(char c){
        State s = new State(c);
        return new Fragment(s,s);

    }

    /***
     * convert concatenation operator to corresponding fragment
     * @param f1 the first input fragment
     * @param f2 the second input fragment
     * @return concatenated fragment
     */
    public static Fragment convertConcat(Fragment f1, Fragment f2){
        List<State> patch = f1.getConnectors();
        for (int i=0; i<patch.size(); i++)
            patch.get(i).addOut(f2.getStart());

        return new Fragment(f1.getStart(),f2.getConnectors());
    }

    /***
     * convert alternation operator to corresponding fragment
     * @param f1 the first input fragment
     * @param f2 the second input fragment
     * @return corresponding fragment
     */
    public static Fragment convertAlternation(Fragment f1, Fragment f2){
        State split_state= new State(State.SPLIT);
        split_state.setOut1(f1.getStart());
        split_state.setOut2(f2.getStart());

        List<State> outstates = new ArrayList<State>();
        outstates.addAll(f1.getConnectors());
        outstates.addAll(f2.getConnectors());

        return new Fragment(split_state, outstates);
    }


    /***
     * convert Kleene star operator to corresponding fragment
     * @param f the input fragment
     * @return corresponding fragment representing the Kleene star opt applied to the input fragment
     */
    public static Fragment convertStar(Fragment f){
        State split_state= new State(State.SPLIT);
        split_state.setOut1(f.getStart());
        List<State> patch = f.getConnectors();
        for (int i=0; i<patch.size(); i++)
            patch.get(i).addOut(split_state);

        return new Fragment(split_state, split_state);


    }

    /***
     * main function to convert post-fix format regular expression to a NFA
     * @param re post-fix format regular expression
     * @return the first state representing the output NFA
     */
    public static  State convertRE2NFA(String re){
        Stack<Fragment> stack = new Stack<Fragment>();
        for (int i = 0; i < re.length(); i++){
            Character c= re.charAt(i);
            if (Character.isLetter(c)){
                stack.push(convertLetter(c));
            }
            else if (c == '.'){
                Fragment f2= stack.pop();
                Fragment f1= stack.pop();
                stack.push(convertConcat(f1,f2));
            }
            else if (c == '|'){
                Fragment f2= stack.pop();
                Fragment f1= stack.pop();
                stack.push(convertAlternation(f1,f2));

            }
            else if (c == '*'){
                stack.push(convertStar(stack.pop()));
            }
        }

        Fragment rs = stack.pop();

        State end_state= new State(State.END);

        List<State> patch = rs.getConnectors();
        for (int i=0; i<patch.size(); i++)
            patch.get(i).addOut(end_state);


        return rs.getStart();

    }


    /***
     * simulate NFA to check if it accepts a given string
     * @param start  the first state of the created NFA
     * @param s string
     * @return true if NFA accepts the given string, false otherwise
     */

    public static boolean runNFA(State start, String s){

        // we scan through all the characters of the given string s
        // at each round, we store all the current states of the NFA and the possible next states after scanning a character
        // we use a hashmap --checkedRound to keep track of the round id that a state is checked, this is used to avoid infinite loop of the scanning

        ArrayList<State> currentStates = new ArrayList<State>();
        ArrayList<State> nextStates = new ArrayList<State>();
        HashMap<State,Integer> checkedRound = new HashMap<State, Integer>();
        addToList(start, currentStates, checkedRound,0);

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            for (int j=0;j<currentStates.size();j++){
                State current = currentStates.get(j);
                if (current.getName() == c){
                    addToList(current.getOut1(), nextStates, checkedRound, i+1);

                }
            }


            currentStates = nextStates;

            nextStates = new ArrayList<State>();

        }

        for (int i = 0; i < currentStates.size(); i++) {
            if (currentStates.get(i).isEnd())  // if in the list of current states, there is a final state then ACCEPT
                return true;
        }

        return false;

    }

    /***
     * check a state and add to a list of possible states that the NFA can enter
     * @param s state to be checked
     * @param lst list to which we add the state s
     * @param checkedRound hashmap that stores the round when a state is checked
     * @param round the order of the character scanned
     */
    private static void addToList(State s, List<State> lst, HashMap<State,Integer> checkedRound, int round){

        if (s==null || (checkedRound.containsKey(s) && checkedRound.get(s) == round))
            return;

        checkedRound.put(s, round);

        if (s.isSplit()){
            addToList(s.getOut1(), lst, checkedRound, round);
            addToList(s.getOut2(), lst, checkedRound, round);
            return;
        }
        lst.add(s);

    }


}

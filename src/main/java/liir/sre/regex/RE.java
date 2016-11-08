package liir.sre.regex;
import liir.sre.nfa.NFA;
import liir.sre.nfa.State;
import org.apache.commons.lang3.ArrayUtils;
import java.util.HashMap;
import java.util.Stack;


/**
 * Created by quynhdo on 07/11/16.
 * Implement regular expression tasks: validation, infix to postfix
 */
public class RE {

    public static final char[] operators = {'|',  '*', '(', ')','.'}; //definition of operator symbols, we use '.' to explicitly denote concatenation operation
    //public static final char[] letters = "abcdefghijklmnopqrstuvwxyz".toCharArray(); //normal letters
    public static final HashMap<Character, Integer> precedences = new HashMap<Character, Integer>(){
        {
            put('*', 0);
            put ('.',1);
            put('|', 2);
            put('(',3);
            put(')',3);
        }
    };

    State nfa;

    public RE(){
        this.nfa = null;
    }


    public RE(String re){
        compile(re);
    }

    /***
     * compile a regular expression
     * @param re
     * @return whether the compilation is successful or not
     */
    public boolean compile(String re){
        if (valid(re)) {
            re = toPostfix(re);
            this.nfa = NFA.convertRE2NFA(re);
            return true;
        }
        return false;
    }

    /***
     * test if the regular expression matches a String s
     * @param s
     * @return true if matches, false otherwise
     * @throws Exception
     */
    public boolean matches(String s) throws Exception {

        if (nfa==null)
            throw new Exception("Oops! You forgot to compile a valid regular expression before matching.");
        else
            return NFA.runNFA(nfa, s);
    }


    /***
     * check if a character c is an operator or not
     * @param c
     * @return true if c is an operator, false otherwise
     */
    public static boolean isOperator(char c){
        return ArrayUtils.contains(operators,c)? true:false;
    }

    /***
     * check if an expression is in good format or not
     * @param r -- e.g '(happy)+life'
     * @return true if good format, and false otherwise
     */
    public static boolean valid(String r){
        Stack pa_stack = new Stack();
        for (int i=0;i<r.length();i++){
            char c = r.charAt(i);
            if (! (Character.isLetter(c) || isOperator(c)))
                return false;       //a character is not in the vocabulary
            //check unary operator
            if (c =='*' && i==0)
                return false;
            //check binary operator
            if (c == '|' && (i == 0 || i == r.length() - 1)) // if binary operator occurs at the first or the end => wrong format
                    return false;
            //check parentheses
            if (c == '(') pa_stack.add(c);
            if (c == ')') {
                if (pa_stack.empty())
                    return false;
                else
                    pa_stack.pop();
            }

        }
        // check if parentheses match
        if (!pa_stack.empty())
            return false;

        return true;
    }

    /***
     * surround expression by (), and add explicit concatenation operator '.'
     * @param s
     * @return regular expression in standard format
     */
    public static String standardizeFormat(String s){
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (int i= 0; i< s.length()-1; i++){
            sb.append(String.valueOf(s.charAt(i)));
            if (Character.isLetter(s.charAt(i)) && ( Character.isLetter(s.charAt(i + 1)) || (s.charAt(i + 1)=='(') ))
                sb.append(String.valueOf('.'));
            else if ((s.charAt(i)=='*'||(s.charAt(i)==')' )) && (s.charAt(i+1)== '('||Character.isLetter(s.charAt(i+1))))
                sb.append(String.valueOf('.'));

        }
        sb.append(s.charAt(s.length()-1));
        sb.append(")");
        return sb.toString();
    }

    /***
     * get Precedence score of a character
     * @param c - input character
     * @return integer score
     */

    private static int getPrecedence(char c){
        return Character.isLetter(c)? -1 : precedences.get(c);

    }


    /***
     * convert an infix expression to postfix format
     * @param s : infix expression
     * @return : postfix format of the expression
     */
    public static String toPostfix(String s) {
        s = standardizeFormat(s);
        StringBuilder rs =  new StringBuilder();
        Stack<Character> rstack = new Stack<Character>();

        for (int i=0; i< s.length(); i ++){
            char c = s.charAt(i);
            switch (c){
                case '(':
                    if (!rstack.isEmpty() && rstack.peek().equals('*'))
                        rs.append(rstack.pop());
                    rstack.push(c);
                    break;
                case ')':
                    while (!rstack.peek().equals('(')){
                        rs.append(rstack.pop());
                    }
                    rstack.pop();
                    break;
                default:
                    while (!rstack.isEmpty()){
                        Character op_at_peek = rstack.pop();
                        if (getPrecedence(op_at_peek)<=getPrecedence(c)){
                            rs.append(op_at_peek);
                        }
                        else{
                            rstack.push(op_at_peek);
                            break;
                        }
                    }
                    rstack.push(c);
                    break;
            }
        }
        return rs.toString();

    }

    /**
     * check if String s matches Regular expression re
     * @param re - regular expression
     * @param s - string to test
     *
     * @return true if yes, false otherwise
     */
    public static boolean matches(String re, String s) throws Exception {
        if (! valid(re))
            throw new Exception("Invalid regular expression!");
        re = RE.toPostfix(re);
        State nfa  = NFA.convertRE2NFA(re);
        return NFA.runNFA(nfa, s);
    }


}

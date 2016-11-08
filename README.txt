// Simple Regular Expressions
 
// The simple regular expressions (SRE) we are here concerned with here are a subset
// of the regular expressions implemented in most modern programming languages.
// They can be specified using only the following characters:
//   - 'a' 'b' 'c' ... 'z' (the 26 lower-case latin letters)
//   - '*'      : for expressing zero of more matches of the preceding expression
//   - '|'      : for alternative expressions
//   - '(' ')'  : for grouping expressions
// 
// Examples:
//   a  matches a
//   ab matches ab
//   a(b|c) matches ab and ac
//   a(b|c)* matches a, ab, abc, abbbbb, abbccc, ...
//   a*(b|c)* matches the empty string, a, ab, aaaac, b, c, abbcb, ...
//   ((((a)))) matches a
//   ((((a))*)) matches the empty string, a, aa, ...
// 
// We call the set of strings, matching a given expression, its language.
// 
// The task is to implement a method to check if a given string is in the language
// of a given expression.

This is an implementation of SRE.
The main folders includes:
- src: source folder
- docs: java docs
- out: jar file

This implementation is based on Thompson's construction theory in which a regular expression is converted to a corresponding non-deterministic finite automata (NFA). A regular matches a string if and only if the corresponding NFA accepts the string.

The main steps:
- check if regular expression is in valid form or not
- convert regular expression to NFA
- simulate NFA to see if it accepts a string or not
——————

System requirement:
Java 8

—————-

Example of using this source code:

In code:
String re = “aa*”;
RE r = RE(re);
System.out.println(r.matches(“a”));
System.out.println(r.matches(“aa”))


Or: System.out.println(RE.matches(“aa*”,  “a”));


Or using the test from command-line interface (RETestCmd class):
- Test single pair of RE and string
Change working directory to the main SRE folder, then type:

java -cp out/artifacts/SRE_jar/SRE.jar liir.sre.interfaces.RETestCmd -r aa* -s aaaaa

- Test pairs in a test file, and write results to an output
Change working directory to the main SRE folder, then type:
java -cp out/artifacts/SRE_jar/SRE.jar liir.sre.interfaces.RETestCmd -f test.txt -o out.txt


—————



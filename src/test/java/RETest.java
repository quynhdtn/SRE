import liir.sre.regex.RE;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by quynhdo on 08/11/16.
 */
public class RETest {

    @Test
    public void testRE() throws Exception {
        String r1 = "aa*";
        assertEquals(RE.matches(r1,"aaaaa"),true);


        r1 = "(abc)*|(efg)";
        assertEquals(RE.matches(r1,"abcc"),false);

        assertEquals(RE.matches(r1,"abcabcefgefgefg"),false);

        assertEquals(RE.matches(r1,"abcabc"),true);

        r1 = "(abc)*|(efg)";
        assertEquals(RE.matches(r1, "wrong"),false);

        r1 = "a(b|c)";
        assertEquals(RE.matches(r1, "ab"),true);

        r1 = "a(b|c)";
        assertEquals(RE.matches(r1, "ac"),true);

        assertEquals(RE.matches(r1, "acc"),false);

        r1 = "a(b|c)*";
        assertEquals(RE.matches(r1, "a"),true);
        assertEquals(RE.matches(r1, "ab"),true);
        assertEquals(RE.matches(r1, "abc"),true);
        assertEquals(RE.matches(r1, "abbbbb"),true);
        assertEquals(RE.matches(r1, "abbccc"),true);
        assertEquals(RE.matches(r1, "bbccc"),false);
        assertEquals(RE.matches(r1, "abbcccd"),false);

        r1 = "a*(b|c)*";

        assertEquals(RE.matches(r1, "a"),true);

        assertEquals(RE.matches(r1, "ab"),true);
        assertEquals(RE.matches(r1, "aaaac"),true);
        assertEquals(RE.matches(r1, "b"),true);
        assertEquals(RE.matches(r1, "c"),true);
        assertEquals(RE.matches(r1, "abbcb"),true);

        r1 = "((((a))))";

        assertEquals(RE.matches(r1, "a"),true);

        r1 = "((((a))*))";

        assertEquals(RE.matches(r1, "a"),true);
        assertEquals(RE.matches(r1, "aa"),true);

        assertEquals(RE.matches(r1, ""),true);








    }

}

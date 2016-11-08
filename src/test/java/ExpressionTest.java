import liir.sre.regex.RE;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by quynhdo on 04/11/16.
 */
public class ExpressionTest {

    @Test
    public void testValidingExpression(){
        String r1 = "aa+";
        assertEquals(RE.valid(r1),false);

        r1 = "aa*";
        assertEquals(RE.valid(r1), true);

        r1 = "(abc)*|(efg)";
        assertEquals(RE.valid(r1),true);

        r1 = "abc)*|(efg)";
        assertEquals(RE.valid(r1),false);
    }




}


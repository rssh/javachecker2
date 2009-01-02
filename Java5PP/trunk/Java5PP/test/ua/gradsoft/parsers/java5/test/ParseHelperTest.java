/*
 * ParseHelperTest.java
 *
 */

package ua.gradsoft.parsers.java5.test;

import junit.framework.TestCase;
import ua.gradsoft.parsers.java5.ParserHelper;

/**
 *
 * @author rssh
 */
public class ParseHelperTest extends TestCase
{
    
    /** Creates a new instance of ParseHelperTest */
    public ParseHelperTest() {
    }
    
    public void testCodeCharacterLiteralN()
    {
      char ch = '\n';
      String s = ParserHelper.codeCharLiteral(ch);
      assertEquals("'\\n'",s);
    }

    public void testDecodeCharLiteralN() throws Exception
    {
      String s = "'\\n'";
      char ch = ParserHelper.decodeCharLiteral(s);
      assertEquals('\n',ch);
    }

    
    
}

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class Printtokens2Test {

    // -------- helper for verbose tests --------

    void printTestResult(String testName, Object input, Object expected, Object actual) {
        System.out.println("========================================");
        System.out.println("Test: " + testName);
        System.out.println("Test Input: " + input);
        System.out.println("Expected Output: " + expected);
        System.out.println("Actual Output: " + actual);
        System.out.println("========================================");
    }

    // -------- is_keyword --------

    @Test
    void testIsKeywordTrue() {
        String input1 = "and";
        boolean expected1 = true;
        boolean actual1 = Printtokens2.is_keyword(input1);
        printTestResult("testIsKeywordTrue_and", input1, expected1, actual1);
        assertEquals(expected1, actual1);

        String input2 = "or";
        boolean expected2 = true;
        boolean actual2 = Printtokens2.is_keyword(input2);
        printTestResult("testIsKeywordTrue_or", input2, expected2, actual2);
        assertEquals(expected2, actual2);

        String input3 = "if";
        boolean expected3 = true;
        boolean actual3 = Printtokens2.is_keyword(input3);
        printTestResult("testIsKeywordTrue_if", input3, expected3, actual3);
        assertEquals(expected3, actual3);

        String input4 = "xor";
        boolean expected4 = true;
        boolean actual4 = Printtokens2.is_keyword(input4);
        printTestResult("testIsKeywordTrue_xor", input4, expected4, actual4);
        assertEquals(expected4, actual4);

        String input5 = "lambda";
        boolean expected5 = true;
        boolean actual5 = Printtokens2.is_keyword(input5);
        printTestResult("testIsKeywordTrue_lambda", input5, expected5, actual5);
        assertEquals(expected5, actual5);

        String input6 = "=>";
        boolean expected6 = true;
        boolean actual6 = Printtokens2.is_keyword(input6);
        printTestResult("testIsKeywordTrue_=>", input6, expected6, actual6);
        assertEquals(expected6, actual6);
    }

    @Test
    void testIsKeywordFalse() {
        String input1 = "hello";
        boolean expected1 = false;
        boolean actual1 = Printtokens2.is_keyword(input1);
        printTestResult("testIsKeywordFalse_hello", input1, expected1, actual1);
        assertEquals(expected1, actual1);

        String input2 = "abc";
        boolean expected2 = false;
        boolean actual2 = Printtokens2.is_keyword(input2);
        printTestResult("testIsKeywordFalse_abc", input2, expected2, actual2);
        assertEquals(expected2, actual2);
    }

    // -------- is_comment --------

    @Test
    void testIsCommentTrue() {
        assertTrue(Printtokens2.is_comment(";this is a comment"));
    }

    @Test
    void testIsCommentFalse() {
        assertFalse(Printtokens2.is_comment("abc"));
    }

    // -------- is_char_constant --------

    @Test
    void testCharConstantValid() {
        String input = "#a";
        boolean expected = true;
        boolean actual = Printtokens2.is_char_constant(input);

        printTestResult("testCharConstantValid", input, expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    void testCharConstantInvalid() {
        assertFalse(Printtokens2.is_char_constant("a"));
    }

    // -------- is_identifier --------

    @Test
    void testIdentifierValid() {
        String input1 = "abc";
        boolean expected1 = true;
        boolean actual1 = Printtokens2.is_identifier(input1);
        printTestResult("testIdentifierValid_abc", input1, expected1, actual1);
        assertEquals(expected1, actual1);

        String input2 = "a1";
        boolean expected2 = true;
        boolean actual2 = Printtokens2.is_identifier(input2);
        printTestResult("testIdentifierValid_a1", input2, expected2, actual2);
        assertEquals(expected2, actual2);
    }

    @Test
    void testIdentifierInvalid() {
        String input1 = "1abc";
        boolean expected1 = false;
        boolean actual1 = Printtokens2.is_identifier(input1);
        printTestResult("testIdentifierInvalid_1abc", input1, expected1, actual1);
        assertEquals(expected1, actual1);

        assertFalse(Printtokens2.is_identifier("#a"));
    }

    // -------- is_spec_symbol --------

    @Test
    void testSpecSymbolTrue() {
        assertTrue(Printtokens2.is_spec_symbol('('));
        assertTrue(Printtokens2.is_spec_symbol(')'));
        assertTrue(Printtokens2.is_spec_symbol('['));
        assertTrue(Printtokens2.is_spec_symbol(']'));
    }

    @Test
    void testSpecSymbolFalse() {
        assertFalse(Printtokens2.is_spec_symbol('a'));
    }

    // -------- is_num_constant --------

    @Test
    void testNumericConstantValid() {
        String input1 = "123";
        boolean expected1 = true;
        boolean actual1 = Printtokens2.is_num_constant(input1);
        printTestResult("testNumericConstantValid_123", input1, expected1, actual1);
        assertEquals(expected1, actual1);

        String input2 = "9";
        boolean expected2 = true;
        boolean actual2 = Printtokens2.is_num_constant(input2);
        printTestResult("testNumericConstantValid_9", input2, expected2, actual2);
        assertEquals(expected2, actual2);
    }

    @Test
    void testNumericConstantInvalid() {
        String input = "12a";
        boolean expected = false;
        boolean actual = Printtokens2.is_num_constant(input);

        printTestResult("testNumericConstantInvalid", input, expected, actual);
        assertEquals(expected, actual);

        assertFalse(Printtokens2.is_num_constant("abc"));
    }

    // -------- is_str_constant --------

    @Test
    void testStringConstantValid() {
        String input = "\"hello\"";
        boolean expected = true;
        boolean actual = Printtokens2.is_str_constant(input);

        printTestResult("testStringConstantValid", input, expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    void testStringConstantInvalid() {
        String input = "hello";
        boolean expected = false;
        boolean actual = Printtokens2.is_str_constant(input);

        printTestResult("testStringConstantInvalid", input, expected, actual);
        assertEquals(expected, actual);
    }

    // -------- is_token_end --------

    @Test
    void testIsTokenEndEOF() {
        int strComId = 0;
        int res = -1;
        boolean expected = true;
        boolean actual = Printtokens2.is_token_end(strComId, res);

        printTestResult("testIsTokenEndEOF", "(0, -1)", expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    void testIsTokenEndStringQuote() {
        int strComId = 1;
        int res = (int) '"';
        boolean expected = true;
        boolean actual = Printtokens2.is_token_end(strComId, res);

        printTestResult("testIsTokenEndStringQuote", "(1, '\"')", expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    void testIsTokenEndStringNewline() {
        int strComId = 1;
        int res = (int) '\n';
        boolean expected = true;
        boolean actual = Printtokens2.is_token_end(strComId, res);

        printTestResult("testIsTokenEndStringNewline", "(1, '\\n')", expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    void testIsTokenEndStringNormalChar() {
        int strComId = 1;
        int res = (int) 'a';
        boolean expected = false;
        boolean actual = Printtokens2.is_token_end(strComId, res);

        printTestResult("testIsTokenEndStringNormalChar", "(1, 'a')", expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    void testIsTokenEndCommentNewline() {
        int strComId = 2;
        int res = (int) '\n';
        boolean expected = true;
        boolean actual = Printtokens2.is_token_end(strComId, res);

        printTestResult("testIsTokenEndCommentNewline", "(2, '\\n')", expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    void testIsTokenEndCommentCarriageReturn() {
        int strComId = 2;
        int res = (int) '\r';
        boolean expected = true;
        boolean actual = Printtokens2.is_token_end(strComId, res);

        printTestResult("testIsTokenEndCommentCarriageReturn", "(2, '\\r')", expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    void testIsTokenEndCommentTab() {
        int strComId = 2;
        int res = (int) '\t';
        boolean expected = true;
        boolean actual = Printtokens2.is_token_end(strComId, res);

        printTestResult("testIsTokenEndCommentTab", "(2, '\\t')", expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    void testIsTokenEndCommentNormalChar() {
        int strComId = 2;
        int res = (int) 'a';
        boolean expected = false;
        boolean actual = Printtokens2.is_token_end(strComId, res);

        printTestResult("testIsTokenEndCommentNormalChar", "(2, 'a')", expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    void testIsTokenEndNormalSpace() {
        int strComId = 0;
        int res = (int) ' ';
        boolean expected = true;
        boolean actual = Printtokens2.is_token_end(strComId, res);

        printTestResult("testIsTokenEndNormalSpace", "(0, ' ')", expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    void testIsTokenEndNormalNewline() {
        int strComId = 0;
        int res = (int) '\n';
        boolean expected = true;
        boolean actual = Printtokens2.is_token_end(strComId, res);

        printTestResult("testIsTokenEndNormalNewline", "(0, '\\n')", expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    void testIsTokenEndNormalSemicolon() {
        int strComId = 0;
        int res = (int) ';';
        boolean expected = true;
        boolean actual = Printtokens2.is_token_end(strComId, res);

        printTestResult("testIsTokenEndNormalSemicolon", "(0, ';')", expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    void testIsTokenEndNormalChar() {
        int strComId = 0;
        int res = (int) 'a';
        boolean expected = false;
        boolean actual = Printtokens2.is_token_end(strComId, res);

        printTestResult("testIsTokenEndNormalChar", "(0, 'a')", expected, actual);
        assertEquals(expected, actual);
    }
    // -------- token_type --------

    @Test
    void testTokenTypeKeyword() {
        String input = "if";
        int expected = Printtokens2.keyword;
        int actual = Printtokens2.token_type(input);

        printTestResult("testTokenTypeKeyword", input, expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    void testTokenTypeSpecSymbol() {
        String input = "(";
        int expected = Printtokens2.spec_symbol;
        int actual = Printtokens2.token_type(input);

        printTestResult("testTokenTypeSpecSymbol", input, expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    void testTokenTypeIdentifier() {
        String input = "myVar";
        int expected = Printtokens2.identifier;
        int actual = Printtokens2.token_type(input);

        printTestResult("testTokenTypeIdentifier", input, expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    void testTokenTypeNumber() {
        String input = "123";
        int expected = Printtokens2.num_constant;
        int actual = Printtokens2.token_type(input);

        printTestResult("testTokenTypeNumber", input, expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    void testTokenTypeString() {
        String input = "\"hello\"";
        int expected = Printtokens2.str_constant;
        int actual = Printtokens2.token_type(input);

        printTestResult("testTokenTypeString", input, expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    void testTokenTypeChar() {
        String input = "#a";
        int expected = Printtokens2.char_constant;
        int actual = Printtokens2.token_type(input);

        printTestResult("testTokenTypeChar", input, expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    void testTokenTypeComment() {
        String input = ";comment";
        int expected = Printtokens2.comment;
        int actual = Printtokens2.token_type(input);

        printTestResult("testTokenTypeComment", input, expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    void testTokenTypeError() {
        String input = "@";
        int expected = Printtokens2.error;
        int actual = Printtokens2.token_type(input);

        printTestResult("testTokenTypeError", input, expected, actual);
        assertEquals(expected, actual);
    }

    // -------- get_char --------

    @Test
    void testGetCharNormal() {
        Printtokens2 p = new Printtokens2();
        BufferedReader br = new BufferedReader(new StringReader("a"));

        int expected = (int) 'a';
        int actual = p.get_char(br);

        printTestResult("testGetCharNormal", "reader containing \"a\"", expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    void testGetCharEOF() {
        Printtokens2 p = new Printtokens2();
        BufferedReader br = new BufferedReader(new StringReader(""));

        int expected = -1;
        int actual = p.get_char(br);

        printTestResult("testGetCharEOF", "empty reader", expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    void testGetCharIOException() {
        Printtokens2 p = new Printtokens2();

        Reader badReader = new Reader() {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                throw new IOException("forced read failure");
            }

            @Override
            public void close() throws IOException {
            }
        };

        BufferedReader br = new BufferedReader(badReader);

        int expected = -1;
        int actual = p.get_char(br);

        printTestResult("testGetCharIOException", "reader that throws IOException", expected, actual);
        assertEquals(expected, actual);
    }

    // -------- unget_char --------

    @Test
    void testUngetCharReturnA() throws IOException {
        Printtokens2 p = new Printtokens2();
        BufferedReader br = new BufferedReader(new StringReader("A"));

        br.mark(10);
        int firstRead = br.read();   // read 'A'

        char expected = 'A';
        char actual = p.unget_char(firstRead, br);

        printTestResult("testUngetCharReturnA", "'A' with marked reader", expected, actual);
        assertEquals(expected, actual);

        int reread = br.read();
        assertEquals((int) 'A', reread);
    }

    @Test
    void testUngetCharReturnX() throws IOException {
        Printtokens2 p = new Printtokens2();
        BufferedReader br = new BufferedReader(new StringReader("x"));

        br.mark(10);
        int firstRead = br.read();   // read 'x'

        char expected = 'x';
        char actual = p.unget_char(firstRead, br);

        printTestResult("testUngetCharReturnX", "'x' with marked reader", expected, actual);
        assertEquals(expected, actual);

        int reread = br.read();
        assertEquals((int) 'x', reread);
    }

    @Test
    void testUngetCharResetIOException() {
        Printtokens2 p = new Printtokens2();

        Reader badReader = new Reader() {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                return -1;
            }

            @Override
            public void close() throws IOException {
            }
        };

        BufferedReader br = new BufferedReader(badReader) {
            @Override
            public void reset() throws IOException {
                throw new IOException("forced reset failure");
            }
        };

        char expected = 'B';
        char actual = p.unget_char((int) 'B', br);

        printTestResult("testUngetCharResetIOException", "reader that throws on reset()", expected, actual);
        assertEquals(expected, actual);
    }

    // -------- get_token --------

    @Test
    void testGetTokenEmpty() {
        Printtokens2 p = new Printtokens2();
        BufferedReader br = new BufferedReader(new StringReader(""));

        String expected = null;
        String actual = p.get_token(br);

        printTestResult("testGetTokenEmpty", "reader containing \"\"", expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    void testGetTokenWhitespaceOnly() {
        Printtokens2 p = new Printtokens2();
        BufferedReader br = new BufferedReader(new StringReader("\t\n\r"));

        String expected = null;
        String actual = p.get_token(br);

        printTestResult("testGetTokenWhitespaceOnly", "reader containing whitespace only", expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    void testGetTokenSimple() {
        Printtokens2 p = new Printtokens2();
        BufferedReader br = new BufferedReader(new StringReader("and"));

        String expected = "and";
        String actual = p.get_token(br);

        printTestResult("testGetTokenSimple", "reader containing \"and\"", expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    void testGetTokenStopsAtSpace() {
        Printtokens2 p = new Printtokens2();
        BufferedReader br = new BufferedReader(new StringReader("abc xyz"));

        String expected = "abc";
        String actual = p.get_token(br);

        printTestResult("testGetTokenStopsAtSpace", "reader containing \"abc xyz\"", expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    void testGetTokenComment() {
        Printtokens2 p = new Printtokens2();
        BufferedReader br = new BufferedReader(new StringReader(";comment text\n"));

        String expected = ";comment text";
        String actual = p.get_token(br);

        printTestResult("testGetTokenComment", "reader containing \";comment text\\n\"", expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    void testGetTokenString() {
        Printtokens2 p = new Printtokens2();
        BufferedReader br = new BufferedReader(new StringReader("\"hello\""));

        String expected = "\"hello\"";
        String actual = p.get_token(br);

        printTestResult("testGetTokenString", "reader containing \"\\\"hello\\\"\"", expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    void testGetTokenSpecialSymbol() {
        Printtokens2 p = new Printtokens2();
        BufferedReader br = new BufferedReader(new StringReader("("));

        String expected = "(";
        String actual = p.get_token(br);

        printTestResult("testGetTokenSpecialSymbol", "reader containing \"(\"", expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    void testGetTokenStopsAtSemicolon() {
        Printtokens2 p = new Printtokens2();
        BufferedReader br = new BufferedReader(new StringReader("abc;rest"));

        String expected = "abc";
        String actual = p.get_token(br);

        printTestResult("testGetTokenStopsAtSemicolon", "reader containing \"abc;rest\"", expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    void testGetTokenNumeric() {
        Printtokens2 p = new Printtokens2();
        BufferedReader br = new BufferedReader(new StringReader("123"));

        String expected = "123";
        String actual = p.get_token(br);

        printTestResult("testGetTokenNumeric", "reader containing \"123\"", expected, actual);
        assertEquals(expected, actual);
    }

    // -------- open_character_stream --------------

    @Test
    void testOpenCharacterStreamExistingFile() throws IOException {
        Printtokens2 p = new Printtokens2();

        // create a temporary file
        File tempFile = File.createTempFile("testfile", ".txt");

        // write something into the file
        FileWriter writer = new FileWriter(tempFile);
        writer.write("test");
        writer.close();

        BufferedReader actual = p.open_character_stream(tempFile.getAbsolutePath());

        printTestResult(
            "testOpenCharacterStreamExistingFile",
            tempFile.getAbsolutePath(),
            "non-null BufferedReader",
            actual == null ? "null" : "non-null BufferedReader"
        );

        assertNotNull(actual);

        // cleanup
        tempFile.delete();
    }

    @Test
    void testOpenCharacterStreamMissingFile() {
        Printtokens2 p = new Printtokens2();
        BufferedReader actual = p.open_character_stream("missing_file.txt");

        String actualStatus = (actual != null) ? "non-null BufferedReader" : "null";
        String expectedStatus = "null";

        printTestResult(
            "testOpenCharacterStreamMissingFile",
            "missing_file.txt",
            expectedStatus,
            actualStatus
        );

        assertEquals(null, actual);
    }

    @Test
    void testOpenCharacterStreamNull() {
        Printtokens2 p = new Printtokens2();
        BufferedReader actual = p.open_character_stream(null);

        String actualStatus = (actual != null) ? "non-null BufferedReader" : "null";
        String expectedStatus = "non-null BufferedReader";

        printTestResult(
            "testOpenCharacterStreamNull",
            "null filename",
            expectedStatus,
            actualStatus
        );

        assertNotNull(actual);
    }

    // -------- open_token_stream --------

    @Test
    void testOpenTokenStreamNull() {
        Printtokens2 p = new Printtokens2();
        BufferedReader actual = p.open_token_stream(null);

        String expected = "non-null BufferedReader";
        String actualStatus = (actual != null) ? "non-null BufferedReader" : "null";

        printTestResult("testOpenTokenStreamNull", "null", expected, actualStatus);
        assertNotNull(actual);
    }

    @Test
    void testOpenTokenStreamEmpty() {
        Printtokens2 p = new Printtokens2();
        BufferedReader actual = p.open_token_stream("");

        String expected = "non-null BufferedReader";
        String actualStatus = (actual != null) ? "non-null BufferedReader" : "null";

        printTestResult("testOpenTokenStreamEmpty", "\"\"", expected, actualStatus);
        assertNotNull(actual);
    }

    @Test
    void testOpenTokenStreamExistingFile() throws IOException {
        Printtokens2 p = new Printtokens2();

        File tempFile = File.createTempFile("tokentest", ".txt");
        FileWriter writer = new FileWriter(tempFile);
        writer.write("hello");
        writer.close();

        BufferedReader actual = p.open_token_stream(tempFile.getAbsolutePath());

        String expected = "non-null BufferedReader";
        String actualStatus = (actual != null) ? "non-null BufferedReader" : "null";

        printTestResult(
            "testOpenTokenStreamExistingFile",
            tempFile.getAbsolutePath(),
            expected,
            actualStatus
        );

        assertNotNull(actual);

        tempFile.delete();
    }

    @Test
    void testOpenTokenStreamMissingFile() {
        Printtokens2 p = new Printtokens2();
        BufferedReader actual = p.open_token_stream("missing_file.txt");

        String expected = "null";
        String actualStatus = (actual != null) ? "non-null BufferedReader" : "null";

        printTestResult(
            "testOpenTokenStreamMissingFile",
            "missing_file.txt",
            expected,
            actualStatus
        );

        assertEquals(null, actual);
    }

    // -------- unget_error --------

    @Test
    void testUngetError() {
        Printtokens2 p = new Printtokens2();
        BufferedReader br = new BufferedReader(new StringReader("test"));
        // Simple test: just call the function to ensure it doesn't crash
        Printtokens2.unget_error(br);
    }

    // -------- print_spec_symbol --------

    @Test
    void testPrintSpecSymbol() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        
        try {
            // Test {
            System.setOut(new PrintStream(outputStream));
            Printtokens2.print_spec_symbol("{");
            String actual1 = outputStream.toString();
            String expected1 = "lparen.\n";
            System.setOut(originalOut);
            printTestResult("testPrintSpecSymbol_{", "{", expected1, actual1);
            assertEquals(expected1, actual1);
            outputStream.reset();

            // Test )
            System.setOut(new PrintStream(outputStream));
            Printtokens2.print_spec_symbol(")");
            String actual2 = outputStream.toString();
            String expected2 = "rparen.\n";
            System.setOut(originalOut);
            printTestResult("testPrintSpecSymbol_)", ")", expected2, actual2);
            assertEquals(expected2, actual2);
            outputStream.reset();

            // Test [
            System.setOut(new PrintStream(outputStream));
            Printtokens2.print_spec_symbol("[");
            String actual3 = outputStream.toString();
            String expected3 = "lsquare.\n";
            System.setOut(originalOut);
            printTestResult("testPrintSpecSymbol_[", "[", expected3, actual3);
            assertEquals(expected3, actual3);
            outputStream.reset();

            // Test ]
            System.setOut(new PrintStream(outputStream));
            Printtokens2.print_spec_symbol("]");
            String actual4 = outputStream.toString();
            String expected4 = "rsquare.\n";
            System.setOut(originalOut);
            printTestResult("testPrintSpecSymbol_]", "]", expected4, actual4);
            assertEquals(expected4, actual4);
            outputStream.reset();

            // Test '
            System.setOut(new PrintStream(outputStream));
            Printtokens2.print_spec_symbol("'");
            String actual5 = outputStream.toString();
            String expected5 = "quote.\n";
            System.setOut(originalOut);
            printTestResult("testPrintSpecSymbol_'", "'", expected5, actual5);
            assertEquals(expected5, actual5);
            outputStream.reset();

            // Test `
            System.setOut(new PrintStream(outputStream));
            Printtokens2.print_spec_symbol("`");
            String actual6 = outputStream.toString();
            String expected6 = "bquote.\n";
            System.setOut(originalOut);
            printTestResult("testPrintSpecSymbol_`", "`", expected6, actual6);
            assertEquals(expected6, actual6);
        } finally {
            System.setOut(originalOut);
        }
    }
}
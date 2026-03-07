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
        assertTrue(Printtokens2.is_token_end(0, -1));
    }

    @Test
    void testIsTokenEndNormalChar() {
        assertFalse(Printtokens2.is_token_end(0, (int) 'a'));
    }

    // -------- token_type --------

    @Test
    void testTokenTypeKeyword() {
        String input = "and";
        int expected = 1;
        int actual = Printtokens2.token_type(input);

        printTestResult("testTokenTypeKeyword", input, expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    void testTokenTypeIdentifier() {
        String input = "abc";
        int expected = 3;
        int actual = Printtokens2.token_type(input);

        printTestResult("testTokenTypeIdentifier", input, expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    void testTokenTypeNumber() {
        String input = "123";
        int expected = 41;
        int actual = Printtokens2.token_type(input);

        printTestResult("testTokenTypeNumber", input, expected, actual);
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
    void testGetTokenSimple() {
        Printtokens2 p = new Printtokens2();
        BufferedReader br = new BufferedReader(new StringReader("and"));

        String expected = "and";
        String actual = p.get_token(br);

        printTestResult("testGetTokenSimple", "reader containing \"and\"", expected, actual);
        assertEquals(expected, actual);
    }


    // -------- optional extra useful tests --------

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

    @Test
    void testOpenTokenStreamEmpty() {
        Printtokens2 p = new Printtokens2();
        BufferedReader actual = p.open_token_stream("");

        String actualStatus = (actual != null) ? "non-null BufferedReader" : "null";
        String expectedStatus = "non-null BufferedReader";

        printTestResult(
            "testOpenTokenStreamEmpty",
            "\"\"",
            expectedStatus,
            actualStatus
        );

        assertNotNull(actual);
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
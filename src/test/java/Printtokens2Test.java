
import java.io.BufferedReader;
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
        assertTrue(Printtokens2.is_keyword("and"));
        assertTrue(Printtokens2.is_keyword("or"));
        assertTrue(Printtokens2.is_keyword("if"));
    }

    @Test
    void testIsKeywordFalse() {
        assertFalse(Printtokens2.is_keyword("hello"));
        assertFalse(Printtokens2.is_keyword("abc"));
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
    void testGetChar() {
        Printtokens2 p = new Printtokens2();
        BufferedReader br = new BufferedReader(new StringReader("a"));

        int expected = (int) 'a';
        int actual = p.get_char(br);

        printTestResult("testGetChar", "reader containing \"a\"", expected, actual);
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

    @Test
    void testOpenCharacterStreamNull() {
        Printtokens2 p = new Printtokens2();
        BufferedReader actual = p.open_character_stream(null);

        printTestResult(
            "testOpenCharacterStreamNull",
            "null filename",
            "non-null BufferedReader",
            actual
        );

        assertNotNull(actual);
    }

    @Test
    void testOpenTokenStreamEmpty() {
        Printtokens2 p = new Printtokens2();
        BufferedReader actual = p.open_token_stream("");

        printTestResult(
            "testOpenTokenStreamEmpty",
            "\"\"",
            "non-null BufferedReader",
            actual
        );

        assertNotNull(actual);
    }
}


    


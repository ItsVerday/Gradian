package gg.valgo.gradian;

import gg.valgo.gradian.input.StringParserInput;

import static org.junit.jupiter.api.Assertions.*;

class ParserStateTest {
    @org.junit.jupiter.api.Test
    void input() {
        StringParserInput input = new StringParserInput("Test");
        StringParserInput newInput = new StringParserInput("Test 2");
        ParserState<?> parserState = new ParserState<>(input);

        assertSame(input, parserState.getInput(), "ParserState should return proper input.");

        parserState.setInput(newInput);

        assertSame(newInput, parserState.getInput(), "Setting ParserState input should update the input.");
    }

    @org.junit.jupiter.api.Test
    void index() {
        StringParserInput input = new StringParserInput("Test");
        int index = 5;
        ParserState<?> parserState = new ParserState<>(input);

        assertEquals(parserState.getIndex(), 0, "ParserState should have an initial index of 0.");
        parserState.setIndex(index);
        assertEquals(parserState.getIndex(), index, "Setting ParserState index should update the index.");
    }

    @org.junit.jupiter.api.Test
    void exception() {
        ParserException exception = new ParserException("Test");
        StringParserInput input = new StringParserInput("Test");
        ParserState<?> parserState = new ParserState<>(input);

        assertFalse(parserState.isException(), "ParserState should not have an exception by default.");
        parserState.setException(exception);
        assertTrue(parserState.isException(), "ParserState should have an exception when the exception is set.");
        assertSame(parserState.getException(), exception, ".getException() returns the proper exception.");
        ParserState<?> newParserState = parserState.withException("Test 2");
        assertNotSame(parserState, newParserState, ".withException() should return new instance.");
        parserState = newParserState;
        assertTrue(parserState.isException(), ".withException() sets the new Exception with a message.");
        assertEquals(parserState.getException().getMessage(), "Test 2", "Exception has correct message");
        parserState.setException(null);
        assertFalse(parserState.isException(), ".setException(null) should remove the exception.");
    }

    @org.junit.jupiter.api.Test
    void result() {
        StringParserInput input = new StringParserInput("Test");
        ParserState<String> parserState = new ParserState<>(input);

        assertNull(parserState.getResult(), "Initial result should be null.");
        parserState.setResult("Test");
        assertEquals(parserState.getResult(), "Test", "Result should be the same as set result.");
    }

    @org.junit.jupiter.api.Test
    void isIgnoreResult() {
    }

    @org.junit.jupiter.api.Test
    void setIgnoreResult() {
    }

    @org.junit.jupiter.api.Test
    void duplicate() {
    }

    @org.junit.jupiter.api.Test
    void retype() {
    }

    @org.junit.jupiter.api.Test
    void updateState() {
    }

    @org.junit.jupiter.api.Test
    void withException() {
    }

    @org.junit.jupiter.api.Test
    void formatException() {
    }

    @org.junit.jupiter.api.Test
    void formatExpectedException() {
    }

    @org.junit.jupiter.api.Test
    void formatBadInputTypeException() {
    }
}
package gg.valgo.gradian.parsers.util;

import gg.valgo.gradian.ParserException;
import gg.valgo.gradian.Parser;
import gg.valgo.gradian.ParserState;
import gg.valgo.gradian.input.ParserInput;
import gg.valgo.gradian.util.interfaces.ParserStateMapper;

/**
 * A parser which maps results. Used internally by the Parser.map() method.
 * @param <OldResultType> The old result type, before mapping.
 * @param <NewResultType> The new result type, after mapping.
 */
public class MappedParser<OldResultType, NewResultType> extends Parser<NewResultType> {
    /**
     * The parser which is being mapped.
     */
    private Parser<?> parser;

    /**
     * The mapper, a lambda taking in a parser state and returning a new parser state.
     */
    private ParserStateMapper<OldResultType, NewResultType> mapper;

    /**
     * Creates a new MapperParser from an input parser and a mapper.
     * @param parser The input parser.
     * @param mapper The mapper.
     */
    public MappedParser(Parser<?> parser, ParserStateMapper<OldResultType, NewResultType> mapper) {
        this.parser = parser;
        this.mapper = mapper;

        setParserName(parser.getParserName());
    }

    /**
     * Runs the parsing logic of the parser. This method will only be called if the parser is not currently in an errored state, and the input is valid. This method should only be called internally, use execute() instead.
     * @param state The current parser state.
     * @return The updated parser state, after parsing.
     */
    @Override
    public ParserState<NewResultType> parse(ParserState<?> state) {
        state = parser.execute(state);
        if (state.isException()) {
            return state.retype();
        }

        try {
            return mapper.map((ParserState<OldResultType>) state);
        } catch (ParserException exception) {
            return state.setException(exception).retype();
        }
    }

    /**
     * Gets the name of this parser, used in error messages.
     * @return The name of this parser.
     */
    @Override
    public String getParserName() {
        return parser.getParserName();
    }

    /**
     * Checks whether a given input is valid for this parser. If not, and false is returned, the parser will be put into an errored state. If a parser works with all input types (a combinator), it should
     * @param input The parser input.
     * @return Whether the parser input is valid for this parser.
     */
    @Override
    public boolean inputIsValid(ParserInput<?> input) {
        return parser.inputIsValid(input);
    }

    /**
     * Gets the expected input type as a string, used for error messages. If a parser overrides inputIsValid(), this should also be overridden.
     * @return The name of the expected input type.
     */
    @Override
    public String getExpectedInputName() {
        return parser.getExpectedInputName();
    }
}
package gg.valgo.gradian.util.interfaces;

import gg.valgo.gradian.ParserException;
import gg.valgo.gradian.ParserState;

/**
 * Maps a parser state to a new parser state.
 * @param <OldResultType> The result type of the input state.
 * @param <NewResultType> The result type of the output state.
 */
public interface ParserStateMapper<OldResultType, NewResultType> {
    /**
     * Maps a parser state to a new parser state.
     * @param state The input parser state.
     * @return The output parser state.
     * @throws ParserException Thrown if the mapping fails.
     */
    ParserState<NewResultType> map(ParserState<OldResultType> state) throws ParserException;
}

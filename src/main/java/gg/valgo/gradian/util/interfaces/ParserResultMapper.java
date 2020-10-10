package gg.valgo.gradian.util.interfaces;

import gg.valgo.gradian.ParserException;

/**
 * Represents a mapping function between 2 types.
 * @param <OldResultType> The input result type.
 * @param <NewResultType> The output result type.
 */
public interface ParserResultMapper<OldResultType, NewResultType> {
    /**
     * Maps an object to a new object.
     * @param object The input object.
     * @return The output object.
     * @throws ParserException Thrown if the mapping fails.
     */
    NewResultType map(OldResultType object) throws ParserException;
}
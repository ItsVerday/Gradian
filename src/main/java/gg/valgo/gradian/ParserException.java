package gg.valgo.gradian;

/**
 * An exception thrown by a parser, due to some parsing error.
 */
public class ParserException extends Exception {
    /**
     * Creates a new ParserException with a specified message and throwable.
     * @param message The message.
     */
    public ParserException(String message) {
        super(message);
    }
}
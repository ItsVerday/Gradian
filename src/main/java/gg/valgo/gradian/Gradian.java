package gg.valgo.gradian;

import gg.valgo.gradian.input.Token;
import gg.valgo.gradian.parsers.util.FailParser;
import gg.valgo.gradian.parsers.util.LookAheadParser;
import gg.valgo.gradian.parsers.util.RecursiveParser;
import gg.valgo.gradian.parsers.util.SucceedWithParser;
import gg.valgo.gradian.parsers.util.peek.PeekBytesParser;
import gg.valgo.gradian.parsers.util.peek.PeekStringParser;
import gg.valgo.gradian.parsers.util.peek.PeekTokensParser;
import gg.valgo.gradian.util.coroutine.CoroutineExecutor;
import gg.valgo.gradian.util.interfaces.ParserProducer;
import gg.valgo.gradian.parsers.combinators.*;
import gg.valgo.gradian.parsers.matchers.*;

/**
 * The main Gradian class, with methods to create and combine parsers, as well as static parsers.
 */
public class Gradian {
    /**
     * A parser which matches a digit. This parser results in a string. This parser fails if the next character in the input is not a digit. This parser returns a string.
     */
    public static final RegexParser digit = (RegexParser) regex("^[0-9]").setParserName("digit");

    /**
     * A parser which matches 1 or more digits. This parser results in a string. This parser fails if the next character in the input is not a digit. Otherwise, it will match digits until the next character is not a digit. This parser returns a string.
     */
    public static final RegexParser digits = (RegexParser) regex("^[0-9]+").setParserName("digits");

    /**
     * A parser which matches a letter. This parser results in a string. This parser fails if the next character in the input is not a letter.
     * This parser returns a string.
     */
    public static final RegexParser letter = (RegexParser) regex("^[A-Za-z]").setParserName("letter");

    /**
     * A parser which parses 1 or more letters. This parser results in a string. This parser fails if the next character in the input is not a letter. Otherwise, it will match letters until the next character is not a letter.
     * This parser returns a string.
     */
    public static final RegexParser letters = (RegexParser) regex("^[A-Za-z]+").setParserName("letters");


    /**
     * A parser which matches any whitespace characters, up until the next non-whitespace character. This parser fails if the next character in the input is not a whitespace character. If you wish for whitespace to be optional, use `Gradian.optionalWhitespace` instead. The parser will match whitespace characters until the next character is not a whitespace character.
     * This parser returns a string.
     */
    public static final RegexParser whitespace = (RegexParser) regex("^\\s+").setParserName("whitespace");

    /**
     * A parser which optionally matches any whitespace character. If there is no whitespace present, it returns an empty string. Otherwise, it will return a string with the whitespace. This parser will match all whitespace characters, up until a non-whitespace character.
     * This parser returns a string.
     */
    public static final Parser<String> optionalWhitespace = maybe(whitespace).valueIfAbsent("");

    /**
     * A parser which matches any character. It results in a character, or a string if `.asString()` is called. This parser will only fail if the end of input has been reached.
     * This parser returns a character, or a string if `.asString()` is called.
     */
    public static final AnyCharacterParser anyCharacter = new AnyCharacterParser();

    /**
     * This parser matches the end of the input. It results in a null value. This parser will fail if the end of input has not been reached.
     * This parser returns a null value.
     */
    public static final EndOfInputParser endOfInput = new EndOfInputParser();

    /**
     * A parser which matches a string, resulting in the input string if successful. If the string cannot be matched, the parser will fail. This parser accepts a string or byte array input.
     * @param string The string to match.
     * @return The string parser.
     */
    public static StringParser string(String string) {
        return new StringParser(string);
    }

    /**
     * A parser which matches a single character, resulting in that character if successful. If this parser cannot match the specified character, it will fail. This parser accepts a string or byte array input.
     * @param character The character to match.
     * @return The character parser.
     */
    public static CharacterParser character(char character) {
        return new CharacterParser(character);
    }

    /**
     * A parser which matches one of several characters in a given string, resulting in that character. If the end of input has been reached or none of the characters could be matched, this parser will fail. Otherwise, this parser will succeed.
     * @param string The string containing the choices.
     * @return The anyOfString parser.
     */
    public static ChoiceOfCharactersParser anyOfString(String string) {
        return new ChoiceOfCharactersParser(string);
    }

    /**
     * A parser which matches one of several characters, resulting in that character. If the end of input has been reached or none of the characters could be matched, this parser will fail. Otherwise, this parser will succeed.
     * @param chars The choices for characters to match.
     * @return The choiceOfCharacters parser.
     */
    public static ChoiceOfCharactersParser choiceOfCharacters(char... chars) {
        return new ChoiceOfCharactersParser(chars);
    }

    /**
     * A parser which matches a regular expression in a string. If matching was successful, the match is returned. Otherwise, the parser fails. This parser only accepts string input.
     * @param pattern The pattern to match. All patterns should start with a "^" character, so that only the current input is matched.
     * @return The regex parser.
     */
    public static RegexParser regex(String pattern) {
        return new RegexParser(pattern);
    }

    /**
     * A parser which matches a regular expression in a string. If matching was successful, the match (or a group within the match) is returned. Otherwise, the parser fails. This parser only accepts string input.
     * @param pattern The pattern to match. All patterns should start with a "^" character, so that only the current input is matched.
     * @param flags The regex flags, use 0 for no flags.
     * @param group The group in the regex to use as the result. Use 0 to result in the entire match.
     * @return The regex parser.
     */
    public static RegexParser regex(String pattern, int flags, int group) {
        return new RegexParser(pattern, flags, group);
    }

    /**
     * A parser which matches a sequence of tokens in a token list, based only on id (data is ignored). If the sequence of tokens cannot be matched, this parser will fail. This parser only accepts a tokens input.
     * @param tokens The tokens to match.
     * @return The tokens parser.
     */
    public static TokensParser tokens(Token<?>... tokens) {
        return new TokensParser(tokens);
    }

    /**
     * A parser which matches a single token, based only on id (data is ignored). If the token cannot be matched, this parser will fail. This parser only accepts a tokens input.
     * @param token The token to match.
     * @return The token parser.
     */
    public static Parser<Token<?>> token(Token<?> token) {
        return tokens(token).index(0).setParserName("token");
    }

    /**
     * Parses a single byte in a byte array input. If the byte could not be found, or the the end of input has been reached, this parser will fail.
     * @param b The byte to match.
     * @return The oneByte parser.
     */
    public static Parser<Byte> oneByte(byte b) {
        return bytes(b).index(0).setParserName("oneByte");
    }

    /**
     * Parses a sequence of bytes in a byte array input. If the bytes could not be found, or the remaining input isn't long enough, this parser will fail.
     * @param bytes The bytes to match.
     * @return The bytes parser.
     */
    public static BytesParser bytes(byte... bytes) {
        return new BytesParser(bytes);
    }

    /**
     * Parses an unsigned 1-byte binary value. This parser results in a long. This parser will fail if not enough bytes are left in the input.
     */
    public static final BinaryParser u8 = binary(1, false, false);

    /**
     * Parses a signed 1-byte binary value. This parser results in a long. This parser will fail if not enough bytes are left in the input.
     */
    public static final BinaryParser s8 = binary(1, true, false);

    /**
     * Parses an unsigned 2-byte binary value, big-endian. This parser results in a long. This parser will fail if not enough bytes are left in the input.
     */
    public static final BinaryParser u16BE = binary(2, false, false);

    /**
     * Parses an unsigned 2-byte binary value, little-endian. This parser results in a long. This parser will fail if not enough bytes are left in the input.
     */
    public static final BinaryParser u16LE = binary(2, false, true);

    /**
     * Parses a signed 2-byte binary value, big-endian. This parser results in a long. This parser will fail if not enough bytes are left in the input.
     */
    public static final BinaryParser s16BE = binary(2, true, false);

    /**
     * Parses a signed 2-byte binary value, little-endian. This parser results in a long. This parser will fail if not enough bytes are left in the input.
     */
    public static final BinaryParser s16LE = binary(2, true, true);

    /**
     * Parses an unsigned 4-byte binary value, big-endian. This parser results in a long. This parser will fail if not enough bytes are left in the input.
     */
    public static final BinaryParser u32BE = binary(4, false, false);

    /**
     * Parses an unsigned 4-byte binary value, little-endian. This parser results in a long. This parser will fail if not enough bytes are left in the input.
     */
    public static final BinaryParser u32LE = binary(4, false, true);

    /**
     * Parses a signed 4-byte binary value, big-endian. This parser results in a long. This parser will fail if not enough bytes are left in the input.
     */
    public static final BinaryParser s32BE = binary(4, true, false);

    /**
     * Parses a signed 4-byte binary value, little-endian. This parser results in a long. This parser will fail if not enough bytes are left in the input.
     */
    public static final BinaryParser s32LE = binary(4, true, true);

    /**
     * Parses a binary value. This parser results in a long. This parser will fail if not enough bytes are left in the input.
     * @param bytes The amount of bytes to parse.
     * @param signed Whether the result should be signed or not.
     * @param littleEndian Whether the result should be little-endian or not.
     * @return The binary parser.
     */
    public static BinaryParser binary(int bytes, boolean signed, boolean littleEndian) {
        return new BinaryParser(bytes, signed, littleEndian);
    }

    /**
     * Parses an exact unsigned 1-byte binary value. This parser results in a long. This parser will fail if it cannot parse the correct value.
     * @param value The expected value.
     * @return The exactU8 parser.
     */
    public static ExactBinaryParser exactU8(long value) {
        return exactBinary(1, false, false, value);
    }

    /**
     * Parses an exact signed 1-byte binary value. This parser results in a long. This parser will fail if it cannot parse the correct value.
     * @param value The expected value.
     * @return The exactS8 parser.
     */
    public static ExactBinaryParser exactS8(long value) {
        return exactBinary(1, true, false, value);
    }

    /**
     * Parses an exact unsigned 2-byte binary value, big-endian. This parser results in a long. This parser will fail if it cannot parse the correct value.
     * @param value The expected value.
     * @return The exactU16BE parser.
     */
    public static ExactBinaryParser exactU16BE(long value) {
        return exactBinary(2, false, false, value);
    }

    /**
     * Parses an exact unsigned 2-byte binary value, little-endian. This parser results in a long. This parser will fail if it cannot parse the correct value.
     * @param value The expected value.
     * @return The exactU16LE parser.
     */
    public static ExactBinaryParser exactU16LE(long value) {
        return exactBinary(2, false, true, value);
    }

    /**
     * Parses an exact signed 2-byte binary value, big-endian. This parser results in a long. This parser will fail if it cannot parse the correct value.
     * @param value The expected value.
     * @return The exactS16BE parser.
     */
    public static ExactBinaryParser exactS16BE(long value) {
        return exactBinary(2, true, false, value);
    }

    /**
     * Parses an exact signed 2-byte binary value, little-endian. This parser results in a long. This parser will fail if it cannot parse the correct value.
     * @param value The expected value.
     * @return The exactS16LE parser.
     */
    public static ExactBinaryParser exactS16LE(long value) {
        return exactBinary(2, true, true, value);
    }

    /**
     * Parses an exact unsigned 4-byte binary value, big-endian. This parser results in a long. This parser will fail if it cannot parse the correct value.
     * @param value The expected value.
     * @return The exactU32BE parser.
     */
    public static ExactBinaryParser exactU32BE(long value) {
        return exactBinary(4, false, false, value);
    }

    /**
     * Parses an exact unsigned 4-byte binary value, little-endian. This parser results in a long. This parser will fail if it cannot parse the correct value.
     * @param value The expected value.
     * @return The exactU32LE parser.
     */
    public static ExactBinaryParser exactU32LE(long value) {
        return exactBinary(4, false, true, value);
    }

    /**
     * Parses an exact signed 4-byte binary value, big-endian. This parser results in a long. This parser will fail if it cannot parse the correct value.
     * @param value The expected value.
     * @return The exactS32BE parser.
     */
    public static ExactBinaryParser exactS32BE(long value) {
        return exactBinary(4, true, false, value);
    }

    /**
     * Parses an exact signed 4-byte binary value, little-endian. This parser results in a long. This parser will fail if it cannot parse the correct value.
     * @param value The expected value.
     * @return The exactS32LE parser.
     */
    public static ExactBinaryParser exactS32LE(long value) {
        return exactBinary(4, true, true, value);
    }

    /**
     * Parses an exact binary value. This parser results in a long. This parser will fail if it cannot parse the correct value.
     * @param bytes The amount of bytes to parse.
     * @param signed Whether the result should be signed or not.
     * @param littleEndian Whether the result should be little-endian or not.
     * @param value The expected value.
     * @return The exactU32BE parser.
     */
    public static ExactBinaryParser exactBinary(int bytes, boolean signed, boolean littleEndian, long value) {
        return new ExactBinaryParser(bytes, signed, littleEndian, value);
    }

    /**
     * Optionally parses a value, resulting in null if the value could not be matched. This parser will never fail. This parser accepts any type of input.
     * @param parser The parser to optionally match.
     * @param <ResultType> The type of the parser result.
     * @return The maybe parser.
     */
    public static <ResultType> MaybeParser<ResultType> maybe(Parser<ResultType> parser) {
        return new MaybeParser<>(parser);
    }

    /**
     * Parses a value from a list of choices. The choices are attempted in the order they were specified, and the first one to succeed is the result. Order matters! If none of the parsers succeed, this parser will fail. This parser works with any input type.
     * @param parsers The parsers to choose from.
     * @param <ResultType> The type of the result.
     * @return The choice parser.
     */
    public static <ResultType> ChoiceParser<ResultType> choice(Parser<ResultType>... parsers) {
        return new ChoiceParser<>(parsers);
    }

    /**
     * Parses a value from a list of choices. The choices are attempted in the order they were specified, and the first one to succeed is the result. Order matters! If none of the parsers succeed, this parser will fail. This parser works with any input type.
     * @param parsers The parsers to choose from.
     * @return The anyTypeChoice parser.
     */
    public static ChoiceParser<Object> anyTypeChoice(Parser<?>... parsers) {
        return (ChoiceParser<Object>) ChoiceParser.anyTypeChoice(parsers).setParserName("anyTypeChoice");
    }

    /**
     * Parses a sequence of parsers in order, and returns the results of each parser in order. If any of the parsers in the sequence fail, this parser will fail  This parser works with any input type.
     * @param parsers The sequence of parsers.
     * @param <ResultType> The type of the result.
     * @return The sequence parser.
     */
    public static <ResultType> SequenceParser<ResultType> sequence(Parser<ResultType>... parsers) {
        return new SequenceParser<>(parsers);
    }

    /**
     * Parses a sequence of parsers in order, and returns the results of each parser in order. If any of the parsers in the sequence fail, this parser will fail  This parser works with any input type.
     * @param parsers The sequence of parsers.
     * @return The anyTypeSequence parser.
     */
    public static SequenceParser<Object> anyTypeSequence(Parser<?>... parsers) {
        return (SequenceParser<Object>) SequenceParser.anyTypeSequence(parsers).setParserName("anyTypeSequence");
    }

    /**
     * Parses a parser between two other parsers, which are ignored. This is internally treated as a sequence, and fails under the same circumstances.
     * @param before The parser to parse before the value.
     * @param after The parser to parse after the value.
     * @param value The value parser, whose result will be returned.
     * @param <ResultType> The result type of this parser.
     * @return The between parser.
     */
    public static <ResultType> Parser<ResultType> between(Parser<?> before, Parser<?> after, Parser<ResultType> value) {
        return anyTypeSequence(before, value, after).index(1).castMap();
    }

    /**
     * Parses a value repeatedly until it cannot parse any more of that value. This parser will always succeed. This parser works with any input type.
     * @param parser The parser to repeat.
     * @param <ResultType> The result type of the parser.
     * @return The many parser.
     */
    public static <ResultType> ManyParser<ResultType> many(Parser<ResultType> parser) {
        return (ManyParser<ResultType>) manyRange(parser, -1, -1).setParserName("many");
    }

    /**
     * Parses a value repeatedly until it cannot parse any more of that value. If the amount of matches is not in the specified range, this parser will fail. This parser works with any input type.
     * @param parser The parser to repeat.
     * @param minimumCount The minimum amount of times to repeat. Use -1 for no minimum.
     * @param maximumCount The maximum amount of times to repeat. Use -1 for no maximum.
     * @param <ResultType> The result type of the parser.
     * @return The manyRange parser.
     */
    public static <ResultType> ManyParser<ResultType> manyRange(Parser<ResultType> parser, int minimumCount, int maximumCount) {
        return (ManyParser<ResultType>) new ManyParser<>(parser, minimumCount, maximumCount).setParserName("manyRange");
    }

    /**
     * Parses a value repeatedly until it cannot parse any more of that value. If the amount of matches is too small, this parser will fail. This parser works with any input type.
     * @param parser The parser to repeat.
     * @param minimumCount The minimum amount of times to repeat. Use -1 for no minimum.
     * @param <ResultType> The result type of the parser.
     * @return The atLeast parser.
     */
    public static <ResultType> ManyParser<ResultType> atLeast(Parser<ResultType> parser, int minimumCount) {
        return (ManyParser<ResultType>) manyRange(parser, minimumCount, -1).setParserName("atLeast");
    }

    /**
     * Parses a value repeatedly until it cannot parse any more of that value. If no matches could be made, this parser will fail. This parser works with any input type.
     * @param parser The parser to repeat.
     * @param <ResultType> The result type of the parser.
     * @return The atLeastOne parser.
     */
    public static <ResultType> ManyParser<ResultType> atLeastOne(Parser<ResultType> parser) {
        return (ManyParser<ResultType>) atLeast(parser, 1).setParserName("atLeastOne");
    }

    /**
     * Parses a value repeatedly until it cannot parse any more of that value. If the amount of matches is too large, this parser will fail. This parser works with any input type.
     * @param parser The parser to repeat.
     * @param maximumCount The maximum amount of times to repeat. Use -1 for no maximum.
     * @param <ResultType> The result type of the parser.
     * @return The atMost parser.
     */
    public static <ResultType> ManyParser<ResultType> atMost(Parser<ResultType> parser, int maximumCount) {
        return (ManyParser<ResultType>) manyRange(parser, -1, maximumCount).setParserName("atMost");
    }

    /**
     * Parses a value repeatedly a certain number of times. If the parser cannot match exactly that many values, this parser will fail. This parser works with any input type.
     * @param parser The parser to repeat.
     * @param count The amount of times to repeat.
     * @param <ResultType> The result type of the parser.
     * @return The exactly parser.
     */
    public static <ResultType> ManyParser<ResultType> exactly(Parser<ResultType> parser, int count) {
        return (ManyParser<ResultType>) manyRange(parser, count, count).setParserName("exactly");
    }

    /**
     * Parses a value separated by a separator repeatedly until it cannot parse any more of that value. This parser will always succeed. This parser works with any input type.
     * @param separator The separator between values.
     * @param values The parser to repeat.
     * @param <ResultType> The result type of the parser.
     * @return The separatedBy parser.
     */
    public static <ResultType> SeparatedByParser<ResultType> separatedBy(Parser<?> separator, Parser<ResultType> values) {
        return (SeparatedByParser<ResultType>) rangeSeparatedBy(separator, values, -1, -1).setParserName("separatedBy");
    }

    /**
     * Parses a value separated by a separator repeatedly until it cannot parse any more of that value. If the amount of matches is not in the specified range, this parser will fail. This parser works with any input type.
     * @param separator The separator between values.
     * @param values The parser to repeat.
     * @param minimumCount The minimum amount of times to repeat. Use -1 for no minimum.
     * @param maximumCount The maximum amount of times to repeat. Use -1 for no maximum.
     * @param <ResultType> The result type of the parser.
     * @return The rangeSeparatedBy parser.
     */
    public static <ResultType> SeparatedByParser<ResultType> rangeSeparatedBy(Parser<?> separator, Parser<ResultType> values, int minimumCount, int maximumCount) {
        return (SeparatedByParser<ResultType>) new SeparatedByParser<>(separator, values, minimumCount, maximumCount).setParserName("rangeSeparatedBy");
    }

    /**
     * Parses a value separated by a separator repeatedly until it cannot parse any more of that value. If the amount of matches is too small, this parser will fail. This parser works with any input type.
     * @param separator The separator between values.
     * @param values The parser to repeat.
     * @param minimumCount The minimum amount of times to repeat. Use -1 for no minimum.
     * @param <ResultType> The result type of the parser.
     * @return The atLeastSeparatedBy parser.
     */
    public static <ResultType> SeparatedByParser<ResultType> atLeastSeparatedBy(Parser<?> separator, Parser<ResultType> values, int minimumCount) {
        return (SeparatedByParser<ResultType>) rangeSeparatedBy(separator, values, minimumCount, -1).setParserName("atLeastSeparatedBy");
    }

    /**
     * Parses a value separated by a separator repeatedly until it cannot parse any more of that value. If no matches could be made, this parser will fail. This parser works with any input type.
     * @param separator The separator between values.
     * @param values The parser to repeat.
     * @param <ResultType> The result type of the parser.
     * @return The atLeastOneSeparatedBy parser.
     */
    public static <ResultType> SeparatedByParser<ResultType> atLeastOneSeparatedBy(Parser<?> separator, Parser<ResultType> values) {
        return (SeparatedByParser<ResultType>) atLeastSeparatedBy(separator, values, 1).setParserName("atLeastOneSeparatedBy");
    }

    /**
     * Parses a value separated by a separator repeatedly until it cannot parse any more of that value. If the amount of matches is too large, this parser will fail. This parser works with any input type.
     * @param separator The separator between values.
     * @param values The parser to repeat.
     * @param maximumCount The maximum amount of times to repeat. Use -1 for no maximum.
     * @param <ResultType> The result type of the parser.
     * @return The atMostSeparatedBy parser.
     */
    public static <ResultType> SeparatedByParser<ResultType> atMostSeparatedBy(Parser<?> separator, Parser<ResultType> values, int maximumCount) {
        return (SeparatedByParser<ResultType>) rangeSeparatedBy(separator, values, -1, maximumCount).setParserName("atLeastSeparatedBy");
    }

    /**
     * Parses a value separated by a separator repeatedly a certain number of times. If the parser cannot match exactly that many values, this parser will fail. This parser works with any input type.
     * @param separator The separator between values.
     * @param values The parser to repeat.
     * @param count The amount of times to repeat.
     * @param <ResultType> The result type of the parser.
     * @return The exactlySeparatedBy parser.
     */
    public static <ResultType> SeparatedByParser<ResultType> exactlySeparatedBy(Parser<?> separator, Parser<ResultType> values, int count) {
        return (SeparatedByParser<ResultType>) rangeSeparatedBy(separator, values, count, count).setParserName("exactlySeparatedBy");
    }

    /**
     * A parser which repeats another parser a specific amount of times. This parser will fail if it cannot match enough values. If there are too many values, the extras will be ignored. This parser works with any input type.
     * @param parser The parser to repeat.
     * @param count The amount of times to repeat.
     * @param <ResultType> The result type of the parser.
     * @return The repeat parser.
     */
    public static <ResultType> RepeatParser<ResultType> repeat(Parser<ResultType> parser, int count) {
        return new RepeatParser<>(parser, count);
    }

    /**
     * A parser which peeks a single character, returned as a string. If the end of input has been reached, an empty string will be returned.
     */
    public static Parser<String> peekCharacter = peekString(1).map(array -> {
        if (array.length > 0) {
            return array[0];
        }

        return "";
    }).asString().setParserName("peekCharacter");

    /**
     * A parser which peeks a single byte. If the end of input has been reached, null will be returned.
     */
    public static Parser<Byte> peekByte = peekBytes(1).map(array -> {
        if (array.length > 0) {
            return array[0];
        }

        return null;
    }).setParserName("peekByte");

    /**
     * A parser which peeks a single token. If the end of input has been reached, null will be returned.
     */
    public static Parser<? extends Token<?>> peekToken = peekTokens(1).map(array -> {
        if (array.length > 0) {
            return array[0];
        }

        return null;
    }).setParserName("peekToken");

    /**
     * A parser which "peeks" ahead at the upcoming string input, without consuming any input. If less characters were found than expected, the resulting string will be shortened.
     * @param count The amount of characters to peek.
     * @return The peekString parser.
     */
    public static PeekStringParser peekString(int count) {
        return new PeekStringParser(count);
    }

    /**
     * A parser which "peeks" ahead at the upcoming bytes input, without consuming any input. If less bytes were found than expected, the resulting bytes array will be shortened.
     * @param count The amount of bytes to peek.
     * @return The peekBytes parser.
     */
    public static PeekBytesParser peekBytes(int count) {
        return new PeekBytesParser(count);
    }

    /**
     * A parser which "peeks" ahead at the upcoming tokens input, without consuming any input. If less tokens were found than expected, the resulting tokens list will be shortened.
     * @param count The amount of tokens to peek.
     * @return The peekTokens parser.
     */
    public static PeekTokensParser peekTokens(int count) {
        return new PeekTokensParser(count);
    }

    /**
     * Matches anything except a certain parser. If that parser is not matched, one element (character, byte, token, ...) of input is consumed. This parser will fail if the specified parser succeeds, the end of input is reached, or the input type does not match the parser type. Use this version for string inputs.
     * @param parser The parser to not match.
     * @return The anythingExcept parser.
     */
    public static AnythingExceptParser<Character> stringAnythingExcept(Parser<?> parser) {
        return new AnythingExceptParser<>(parser);
    }

    /**
     * Matches anything except a certain parser. If that parser is not matched, one element (character, byte, token, ...) of input is consumed. This parser will fail if the specified parser succeeds, the end of input is reached, or the input type does not match the parser type. Use this version for byte array inputs.
     * @param parser The parser to not match.
     * @return The anythingExcept parser.
     */
    public static AnythingExceptParser<Byte> bytesAnythingExcept(Parser<?> parser) {
        return new AnythingExceptParser<>(parser);
    }

    /**
     * Matches anything except a certain parser. If that parser is not matched, one element (character, byte, token, ...) of input is consumed. This parser will fail if the specified parser succeeds, the end of input is reached, or the input type does not match the parser type. Use this version for token list inputs.
     * @param parser The parser to not match.
     * @return The anythingExcept parser.
     */
    public static AnythingExceptParser<Token<?>> tokensAnythingExcept(Parser<?> parser) {
        return new AnythingExceptParser<>(parser);
    }

    /**
     * Parses input elements until a certain value is reached in the input. If the end of input is reached or the input has an incorrect element type, this parser will fail. Otherwise, a string of the characters matched is returned. Use this parser for string inputs.
     * @param parser The parser to match everything until.
     * @return The stringUntil parser.
     */
    public static Parser<String> stringUntil(Parser<?> parser) {
        return everyCharacterUntil(parser).join("").setParserName("stringUntil");
    }

    /**
     * Parses input elements until a certain value is reached in the input. If the end of input is reached or the input has an incorrect element type, this parser will fail. Otherwise, an array of the elements matched is returned. Use this parser for string inputs.
     * @param parser The parser to match everything until.
     * @return The everythingUntil parser.
     */
    public static EverythingUntilParser<Character> everyCharacterUntil(Parser<?> parser) {
        return new EverythingUntilParser<>(parser);
    }

    /**
     * Parses input elements until a certain value is reached in the input. If the end of input is reached or the input has an incorrect element type, this parser will fail. Otherwise, an array of the elements matched is returned. Use this parser for byte array inputs.
     * @param parser The parser to match everything until.
     * @return The everythingUntil parser.
     */
    public static EverythingUntilParser<Byte> everyByteUntil(Parser<?> parser) {
        return new EverythingUntilParser<>(parser);
    }

    /**
     * Parses input elements until a certain value is reached in the input. If the end of input is reached or the input has an incorrect element type, this parser will fail. Otherwise, an array of the elements matched is returned. Use this parser for token list inputs.
     * @param parser The parser to match everything until.
     * @return The everythingUntil parser.
     */
    public static EverythingUntilParser<Token<?>> everyTokenUntil(Parser<?> parser) {
        return new EverythingUntilParser<>(parser);
    }

    /**
     * A parser which "looks ahead" by matching another parser, without consuming any input. If that parser cannot be matched, this parser will fail.
     * @param parser The parser to look ahead with.
     * @param <ResultType> The result type of this parser.
     * @return The lookAhead parser.
     */
    public static <ResultType> LookAheadParser<ResultType> lookAhead(Parser<ResultType> parser) {
        return new LookAheadParser<>(parser);
    }

    /**
     * An advanced parser which runs custom logic in a lambda. Parsers can be "yielded", and they will be parsed. If the parser succeeds, the result of the parser is returned from the yield method, and if the parser fails, the coroutine execution is stopped.
     * @param executor The executor, a lambda taking in a context which receives a context with yield() and reject() methods.
     * @param <ResultType> The result type of this parser.
     * @return The coroutine parser.
     */
    public static <ResultType> CoroutineParser<ResultType> coroutine(CoroutineExecutor<ResultType> executor) {
        return new CoroutineParser<>(executor);
    }

    /**
     * A utility parser which always succeeds with a given value.
     * @param result The result to succeed with.
     * @param <ResultType> The result type of this parser.
     * @return The succeedWith parser.
     */
    public static <ResultType> SucceedWithParser<ResultType> succeedWith(ResultType result) {
        return new SucceedWithParser<>(result);
    }

    /**
     * A utility parser which always fails with a given message. Useful for providing more detailed error messages in your parser.
     * @param message The message to fail with.
     * @return The fail parser.
     */
    public static FailParser fail(String message) {
        return new FailParser(message);
    }

    /**
     * A parser which accepts a lambda producing a parser, allowing for recursive parsers.
     * @param producer The parser producer.
     * @param <ResultType> The result type.
     * @return The recursive parser.
     */
    public static <ResultType> RecursiveParser<ResultType> recursive(ParserProducer<ResultType> producer) {
        return new RecursiveParser<>(producer);
    }
}
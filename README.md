# Gradian
Gradian is a [Parser Combinator](https://en.wikipedia.org/wiki/Parser_combinator) library for Java. It is based on Haskell's Parsec library, and the [arcsecond](https://github.com/francisrstokes/arcsecond) node.js package.

## What are Parser Combinators?
With parser combinator libraries, parsers are built up from smaller, simpler parsers. Parsers can be combined in a number of ways to produce more complex behavior. Gradian takes full advantage of this idea, providing a variety of "parser-combinators".

## Naming
This library is named Gradian because of the naming of the node package it is based on, [arcsecond](https://github.com/francisrstokes/arcsecond). Both an arcsecond and a gradian are (somewhat obscure) units of angle.

## Reference
### Parser Methods
- [`.run(String input)`](#parserrunstring-input---parserstate)
- [`.getResult(String input)`](#parsergetresultstring-input---)
- [`.map(ResultMapper mapper)`](#parsermapresultmapper-mapper---parser)
- [`.<NewResultType>mapType()`](#parsernewresulttypemaptype---parser)
- [`.mapState(StateMapper mapper)`](#parsermapstatestatemapper-mapper---parser)

### Parsers
- [`.digit`](#gradiandigit---string)
- [`.digits`](#gradiandigits---string)
- [`.letter`](#gradianletter---string)
- [`.letters`](#gradianletters---string)
- [`.whitespace`](#gradianwhitespace---string)
- [`.optionalWhitespace`](#gradianoptionalwhitespace---string)
- [`.anyCharacter`](#gradiananycharacter---char--string)
- [`.endOfInput`](#gradianendofinput---null)
- [`.string(String value)`](#gradianstringstring-value---string)
- [`.character(char value)`](#gradiancharacterchar-value---char--string)
- [`.maybe(Parser value)`](#gradianmaybeparser-value---result-type-of-value)
- [`.ignore(Parser value)`](#gradianignoreparser-value---null)
- [`.sequence(Parser... parsers)`](#gradiansequenceparser-parsers---object--arraylistobject--string)
- [`.between(Parser left, Parser right, Parser middle)`](#gradianbetweenparser-left-parser-right-parser-middle---result-type-of-middle)
- [`.peek1`](#gradianpeek1---string)
- [`.peek(int chars)`](#gradianpeekint-chars---string)
- [`.choice(Parser... parsers)`](#gradianchoiceparser-parsers---)
- [`.many(Parser parser)`](#gradianmanyparser-parser---array--arraylist-of-result-type-of-parser-or-string)
- [`.atLeast(Parser parser, int minimumCount)`](#gradianatleastparser-parser-int-minimumcount---array--arraylist-of-result-type-of-parser-or-string)
- [`.atLeastOne(Parser parser)`](#gradianatleastoneparser-parser---array--arraylist-of-result-type-of-parser-or-string)
- [`.atMost(Parser parser, int maximumCount)`](#gradianatmostparser-parser-int-maximumcount---array--arraylist-of-result-type-of-parser-or-string)
- [`.manyBetween(Parser parser, int minimumCount, int maximumCount)`](#gradianmanybetweenparser-parser-int-minimumcount-int-maximumcount---array--arraylist-of-result-type-of-parser-or-string)
- [`.exactly(Parser parser, int count)`](#gradianexactlyparser-parser-int-count---array--arraylist-of-result-type-of-parser-or-string)
- [`.separatedBy(Parser separator, Parser values)`](#gradianseparatedbyparser-separator-parser-values---array--arraylist-of-result-type-of-values-or-string)
- [`.anythingExcept(Parser parser)`](#gradiananythingexceptparser-parser---char)
- [`.coroutine(CoroutineExecutor executor)`](#gradiancoroutinecoroutineexecutor-executor---)
- [`.recursive(ParserProducer producer)`](#gradianrecursiveparserproducer-producer---)

## Parser Methods
### `parser.run(String input)` -> `ParserState`
Runs a parser on a given string. The returned value is a `ParserState` with a `.getResult()` method to get the result of parsing. If the parser fails, the value of `parserState.isException()` will be true, and the `parserState.getException()` will return the exception.
- `String input` -> The string to parse
<details>
    <summary>Examples</summary>

    *No examples yet...*
</details>

### `parser.getResult(String input)` -> `???`
Runs a parser on a given string and returns the result, or throws a ParserException if the parsing fails.
- `String input` -> The string to parse
<details>
    <summary>Examples</summary>

    *No examples yet...*
</details>

### `parser.map(ResultMapper mapper)` -> `Parser`
Maps the result of a parser to a new value. Useful for processing the result in the parser itself, instead of externally.
- `ResultMapper mapper` -> A lambda which takes a value, the result of the parser, and returns a new value
<details>
    <summary>Examples</summary>

    *No examples yet...*
</details>

### `parser.<NewResultType>mapType()` -> `Parser`
A utility method, used to cast the result of a parser to a different type. In many situations, this method will be called without the type generic, if the type can be inferred.
- `NewResultType` -> The type to cast the result to
<details>
    <summary>Examples</summary>

    *No examples yet...*
</details>

### `parser.mapState(StateMapper mapper)` -> `Parser`
Maps a resulting parser state to a new parser state. Can be useful for more advanced parsers, where the parser index, or the exception needs to be modified.
- `StateMapper mapper` -> A lambda which takes a parser state, the resulting state of the parser, and returns a new parser state

## Parsers
### `Gradian.digit` -> `String`
A parser which matches a digit. This parser results in a string. This parser fails if the next character in the input is not a digit.
<details>
    <summary>Examples</summary>

    *No examples yet...*
</details>

### `Gradian.digits` -> `String`
A parser which matches 1 or more digits. This parser results in a string. This parser fails if the next character in the input is not a digit. Otherwise, it will match digits until the next character is not a digit.
<details>
    <summary>Examples</summary>

    *No examples yet...*
</details>

### `Gradian.letter` -> `String`
A parser which matches a letter. This parser results in a string. This parser fails if the next character in the input is not a letter.
<details>
    <summary>Examples</summary>

    *No examples yet...*
</details>

### `Gradian.letters` -> `String`
A parser which parses 1 or more letters. This parser results in a string. This parser fails if the next character in the input is not a letter. Otherwise, it will match letters until the next character is not a letter.
<details>
    <summary>Examples</summary>

    *No examples yet...*
</details>

### `Gradian.whitespace` -> `String`
A parser which matches any whitespace characters, up until the next non-whitespace character. This parser fails if the next character in the input is not a whitespace character. If you wish for whitespace to be optional, use [`Gradian.optionalWhitespace`](#gradianoptionalwhitespace---string) instead. The parser will match whitespace characters until the next character is not a whitespace character.
<details>
    <summary>Examples</summary>

    *No examples yet...*
</details>

### `Gradian.optionalWhitespace` -> `String`
A parser which optionally matches any whitespace character. If there is no whitespace present, it returns an empty string. Otherwise, it will return a string with the whitespace. This parser will match all whitespace characters, up until a non-whitespace character.
<details>
    <summary>Examples</summary>

    *No examples yet...*
</details>

### `Gradian.anyCharacter` -> `char | String`
A parser which matches any character. It results in a character, or a string if `.asString()` is called. This parser will only fail if the end of input has been reached.
<details>
    <summary>Examples</summary>

    *No examples yet...*
</details>

### `Gradian.endOfInput` -> `null`
This parser matches the end of the input. It results in a null value. This parser will fail if the end of input has not been reached.
<details>
    <summary>Examples</summary>

    *No examples yet...*
</details>

### `Gradian.string(String value)` -> `String`
Returns a parser that matches a specific string. This parser will fail if it cannot match the string.
- `String value` -> The string to match
<details>
    <summary>Examples</summary>

    *No examples yet...*
</details>

### `Gradian.character(char value)` -> `char | String`
Returns a parser that matches a single character. Use `.asString()` if you want to receive a string as the result. This parser will fail if it cannot match the specified character.
- `char value` -> The character to match
<details>
    <summary>Examples</summary>

    *No examples yet...*
</details>

### `Gradian.regex(String pattern, int flags = 0)` -> `String`
A parser which matches a regular expression. This parser will fail if it cannot match the pattern.
- `String pattern` -> The regex pattern to match.
- `int flags = 0` -> The regex flags, defaulting to 0.
<details>
    <summary>Examples</summary>

    *No examples yet...*
</details>

### `Gradian.maybe(Parser value)` -> Result type of `value`
Returns a parser which optionally matches another parser. If a match cannot be made, the parser has a result of null. If you wish to ignore the result if it is absent, use `.ignoreIfAbsent()`. If you wish to return a specific value if the result is absent, use `.valueIfAbsent(Object value)`. This parser cannot fail.
- `Parser value` -> The parser to possibly match
<details>
    <summary>Examples</summary>

    *No examples yet...*
</details>

### `Gradian.ignore(Parser value)` -> `null`
Returns a parser which matches another parser, but ignores the result. Useful in parsers such as sequence, in order to omit unnecessary data. This parser cannot fail.
- `Parser value` -> The parser to ignore the result of
<details>
    <summary>Examples</summary>

    *No examples yet...*
</details>

### `Gradian.sequence(Parser... parsers)` -> `Object[] | ArrayList<Object> | String`
A parser which parses a sequence of parsers. If any parser in the sequence fails, then the sequence parser fails. By default, this parser results in an array. If you would like to receive an ArrayList back, use `.asArrayList()`. If you would like to join the resulting values, use `.join(String delimiter)`.
- `Parser... parsers` -> A list of parsers, which will be used in the sequence
<details>
    <summary>Examples</summary>

    *No examples yet...*
</details>

### `Gradian.between(Parser left, Parser right, Parser middle)` -> Result type of `middle`
A parser which parses a child parser between two other parsers. This parser will fail in the same way as a sequence parser. The result of the middle parser is returned.
- `Parser left` -> The left parser, which will be parsed before the middle parser
- `Parser right` -> The right parser, which will be parsed after the middle parser
- `Parser middle` -> The middle parser, which is the parser whose result will be returned
<details>
    <summary>Examples</summary>

    *No examples yet...*
</details>

### `Gradian.choice(Parser... parsers)` -> `???`
A parser which attempts to parse each "choice" it is given. The first parser that doesn't fail is the one that is chosen, and the result from that parser is used. If every choice fails, then this parser will fail.
- `Parser... parsers` -> A list of choices, which will be attempted in that order
<details>
    <summary>Examples</summary>

    *No examples yet...*
</details>

### `Gradian.peek1` -> `String`
A parser which "peeks" ahead in the string, without consuming any input. This parser will peek at the next character. If the end of input has been reached, this parser will result in an empty string. This parser cannot fail.
<details>
    <summary>Examples</summary>

    *No examples yet...*
</details>

### `Gradian.peek(int chars)` -> `String`
A parser which "peeks" ahead in the string, without consuming any input. This parser will peek at the next n chars, with n being the input to this method. If the input has less characters left than the amount of characters, the result will be truncated. This parser cannot fail.
- `int chars` -> The amount of characters to peek.

### `Gradian.many(Parser parser)` -> `Array | ArrayList` of result type of `parser`, or `String`
A parser which parses multiple instances of the parser passed into it. This parser repeats until it is not able to parse any more instances of the child parser. Results are returned in an array, or an ArrayList if `.asArrayList()` is called. If you would like to join the resulting values, use `.join(String delimiter)`.
- `Parser parser` -> The parser to repeat
<details>
    <summary>Examples</summary>

    *No examples yet...*
</details>

### `Gradian.atLeast(Parser parser, int minimumCount)` -> `Array | ArrayList` of result type of `parser`, or `String`
A parser which parses multiple instances of the parser passed into it. This parser repeats until it is not able to parse any more instances of the child parser. Results are returned in an array, or an ArrayList if `.asArrayList()` is called. If you would like to join the resulting values, use `.join(String delimiter)`. If the parser cannot parse enough instances, it fails.
- `Parser parser` -> The parser to repeat
- `int minimumCount` -> The minimum amount of repetitions to allow
<details>
    <summary>Examples</summary>

    *No examples yet...*
</details>

### `Gradian.atLeastOne(Parser parser)` -> `Array | ArrayList` of result type of `parser`, or `String`
A parser which parses at least one instance of the parser passed into it. This parser repeats until it is not able to parse any more instances of the child parser. Results are returned in an array, or an ArrayList if `.asArrayList()` is called. If you would like to join the resulting values, use `.join(String delimiter)`. If the parser cannot parse at least one instance, it fails.
- `Parser parser` -> The parser to repeat
<details>
    <summary>Examples</summary>

    *No examples yet...*
</details>

### `Gradian.atMost(Parser parser, int maximumCount)` -> `Array | ArrayList` of result type of `parser`, or `String`
A parser which parses multiple instances of the parser passed into it. This parser repeats until it is not able to parse any more instances of the child parser. Results are returned in an array, or an ArrayList if `.asArrayList()` is called. If you would like to join the resulting values, use `.join(String delimiter)`. If the parser parses too many instances, it fails.
- `Parser parser` -> The parser to repeat
- `int maximumCount` -> The maximum amount of repetitions to allow
<details>
    <summary>Examples</summary>

    *No examples yet...*
</details>

### `Gradian.manyBetween(Parser parser, int minimumCount, int maximumCount)` -> `Array | ArrayList` of result type of `parser`, or `String`
A parser which parses multiple instances of the parser passed into it, the amount of which will be in a range. This parser repeats until it is not able to parse any more instances of the child parser. Results are returned in an array, or an ArrayList if `.asArrayList()` is called. If you would like to join the resulting values, use `.join(String delimiter)`. If the parser parses too many of too few instances, it fails.
- `Parser parser` -> The parser to repeat
- `int minimumCount` -> The minimum amount of repetitions to allow
- `int maximumCount` -> The maximum amount of repetitions to allow
<details>
    <summary>Examples</summary>

    *No examples yet...*
</details>

### `Gradian.exactly(Parser parser, int count)` -> `Array | ArrayList` of result type of `parser`, or `String`
A parser which parses a certain amount of instances of the parser passes into it. This parser repeats until it is not able to parse any more instances of the child parser. Results are returned in an array, or an ArrayList if `.asArrayList()` is called. If you would like to join the resulting values, use `.join(String delimiter)`. If the parser doesn't parse the right amount of instances, it fails.
- `Parser parser` -> The parser to repeat
- `int count` -> The amount of repetitions to parse
<details>
    <summary>Examples</summary>

    *No examples yet...*
</details>

### `Gradian.separatedBy(Parser separator, Parser values)` -> `Array | ArrayList` of result type of `values`, or `String`
A parser which parses values, separated by a separator. This parser will fail if the separator is not followed by a value. Empty "lists" are allowed. An array of values, separated by the separator, is returned. If you would like to receive an ArrayList back, use `.asArrayList()`. If you would like to join the resulting values, use `.join(String delimiter)`.
- `Parser separator` -> The separator between values
- `Parser values` -> The values to parse between separators.
<details>
    <summary>Examples</summary>

    *No examples yet...*
</details>

### `Gradian.anythingExcept(Parser parser)` -> `char`
A parser which matches anything except the parser passed into it. If the child parser passes, this parser will fail. Otherwise, the result is the current character in the string.
- `Parser parser` -> The parser to not match
<details>
    <summary>Examples</summary>

    *No examples yet...*
</details>

### `Gradian.coroutine(CoroutineExecutor executor)` -> `???`
Creates a coroutine parser, allowing you to run custom logic in a parser. This is an advanced parser. It will fail if any of the parsers used inside of it fail. Otherwise, it will result in the value returned from the lambda.
- `CoroutineExecutor executor` -> A lambda taking in a context, with a `.yield()` method. When you want to parse a value, use `.yield(parser)` to parse that parser, and get its result back. The context also has a `.reject()` method, which will exit out of the coroutine and fail the parser.
<details>
    <summary>Examples</summary>

    *No examples yet...*
</details>

### `Gradian.recursive(ParserProducer producer)` -> `???`
Used to create recursive parsers.
- `ParserProducer producer` -> A lambda which returns a parser
<details>
    <summary>Examples</summary>

    *No examples yet...*
</details>
# Gradian
Gradian is a [Parser Combinator](https://en.wikipedia.org/wiki/Parser_combinator) library for Java. It is based on Haskell's Parsec library, and the [arcsecond](https://github.com/francisrstokes/arcsecond) node.js package.

## What are Parser Combinators?
With parser combinator libraries, parsers are built up from smaller, simpler parsers. Parsers can be combined in a number of ways to produce more complex behavior. Gradian takes full advantage of this idea, providing a variety of "parser-combinators".

## Naming
This library is named Gradian because of the naming of the node package it is based on, [arcsecond](https://github.com/francisrstokes/arcsecond). Both an arcsecond and a gradian are (somewhat obscure) units of angle.

## Reference
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
- [`.choice(Parser... parsers)`](#gradianchoiceparser-parsers---object)

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

### `Gradian.choice(Parser... parsers)` -> `Object`
A parser which attempts to parse each "choice" it is given. The first parser that doesn't fail is the one that is chosen, and the result from that parser is used. If every choice fails, then this parser will fail.
- `Parser... parsers` -> A list of choices, which will be attempted in that order
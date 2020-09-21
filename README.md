# Gradian
Gradian is a [Parser Combinator](https://en.wikipedia.org/wiki/Parser_combinator) library for Java. It is based on Haskell's Parsec library, and the [arcsecond](https://github.com/francisrstokes/arcsecond) node.js package.

## What are Parser Combinators?
With parser combinator libraries, parsers are built up from smaller, simpler parsers. Parsers can be combined in a number of ways to produce more complex behavior. Gradian takes full advantage of this idea, providing a variety of "parser-combinators".

## Reference
- [`.digit`](#gradiandigit---string)
- [`.digits`](#gradiandigits---string)
- [`.letter`](#gradianletter---string)
- [`.letters`](#gradianletters---string)
- [`.whitespace`](#gradianwhitespace---string)
- [`.optionalWhitespace`](#gradianoptionalwhitespace---string)

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
This parser matches any whitespace characters, up until the next non-whitespace character. This parser fails if the next character in the input is not a whitespace character. If you wish for whitespace to be optional, use [`Gradian.optionalWhitespace`](#gradianoptionalwhitespace---string) instead. The parser will match whitespace characters until the next character is not a whitespace character.
<details>
    <summary>Examples</summary>

    *No examples yet...*
</details>

### `Gradian.optionalWhitespace` -> `String`
This parser optionally matches any whitespace character. If there is no whitespace present, it returns an empty string. Otherwise, it will return a string with the whitespace. This parser will match all whitespace characters, up until a non-whitespace character.
<details>
    <summary>Examples</summary>

    *No examples yet...*
</details>

## Naming
This library is named Gradian because of the naming of the node package it is based on, [arcsecond](https://github.com/francisrstokes/arcsecond). Both an arcsecond and a gradian are (somewhat obscure) units of angle.
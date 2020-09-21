package gg.valgo.gradian.examples;

import gg.valgo.gradian.Gradian;
import gg.valgo.gradian.Parser;

import java.util.ArrayList;
import java.util.HashMap;

public class JSONParser {
    public static final Parser<Object> parseJSON = Gradian.recursive(() -> Gradian.choice(
            JSONParser.parseNumber,
            JSONParser.parseBoolean,
            JSONParser.parseNull,
            JSONParser.parseString,
            JSONParser.parseArray,
            JSONParser.parseObject
    ));

    /**
     * Parses a boolean.
     */
    public static final Parser<Boolean> parseBoolean = Gradian.choice(
            Gradian.string("true"),
            Gradian.string("false")
    ).map(result -> result.equals("true"));

    /**
     * Parses a plus or minus character.
     */
    public static final Parser<String> plusOrMinus = Gradian.choice(
            Gradian.string("+"),
            Gradian.string("-")
    ).mapType();

    /**
     * Optionally parses a plus or minus character, returning an empty string if absent.
     */
    public static final Parser<String> optionalPlusOrMinus = Gradian.maybe(plusOrMinus).valueIfAbsent("").mapType();

    /**
     * Parses a float, without the exponent.
     */
    public static final Parser<String> parseFloat = Gradian.sequence(
            optionalPlusOrMinus,
            Gradian.digits,
            Gradian.string("."),
            Gradian.digits
    ).join("");

    /**
     * Parses an int, without the exponent.
     */
    public static final Parser<String> parseInt = Gradian.sequence(
            optionalPlusOrMinus,
            Gradian.digits
    ).join("");

    /**
     * Parses a float/int with the exponent.
     */
    public static final Parser<String> scientificNotation = Gradian.sequence(
            Gradian.choice(
                    parseFloat,
                    parseInt
            ),
            Gradian.choice(
                    Gradian.string("e"),
                    Gradian.string("E")
            ),
            parseInt
    ).join("");

    /**
     * Parses a number into a double.
     */
    public static final Parser<Double> parseNumber = Gradian.choice(
            scientificNotation,
            parseFloat,
            parseInt
    ).<String>mapType().map(Double::parseDouble);

    /**
     * Parses a null value.
     */
    public static final Parser<Object> parseNull = Gradian.string("null").map(result -> null);

    /**
     * Parses an escaped quote.
     */
    public static final Parser<String> escapedQuote = Gradian.sequence(
            Gradian.string("\\"),
            Gradian.choice(
                    Gradian.string("\""),
                    Gradian.string("'")
            )
    ).join("");

    /**
     * Parses a string literal.
     */
    public static final Parser<String> parseString = Gradian.between(
            Gradian.string("\""),
            Gradian.string("\""),
            Gradian.many(
                    Gradian.choice(
                            escapedQuote,
                            Gradian.anythingExcept(Gradian.string("\"")
                    )
            )
    ).join("")).mapType();

    /**
     * Parses an array.
     */
    public static final Parser<ArrayList<Object>> parseArray = Gradian.between(
            Gradian.string("["),
            Gradian.string("]"),
            Gradian.separatedBy(
                    Gradian.between(
                            Gradian.optionalWhitespace,
                            Gradian.optionalWhitespace,
                            Gradian.string(",")
                    ),
                    parseJSON
            ).asArrayList()
    ).mapType();

    /**
     * Parses a separator between a key and a value.
     */
    public static final Parser<String> keyValueSeparator = Gradian.ignore(
            Gradian.between(
                    Gradian.optionalWhitespace,
                    Gradian.optionalWhitespace,
                    Gradian.string(":")
            )
    ).mapType();

    /**
     * Parses a key value pair.
     */
    public static final Parser<KeyValuePair> parseKeyValue = Gradian.between(
            Gradian.optionalWhitespace,
            Gradian.optionalWhitespace,
            Gradian.sequence(
                    parseString,
                    keyValueSeparator,
                    parseJSON
            ).map(values -> new KeyValuePair((String) values[0], values[1]))
    ).mapType();

    /**
     * Parses an object.
     */
    public static final Parser<HashMap<String, Object>> parseObject = Gradian.between(
            Gradian.between(
                    Gradian.optionalWhitespace,
                    Gradian.optionalWhitespace,
                    Gradian.string("{")
            ),
            Gradian.between(
                    Gradian.optionalWhitespace,
                    Gradian.optionalWhitespace,
                    Gradian.string("}")
            ),
            Gradian.separatedBy(
                    Gradian.between(
                            Gradian.optionalWhitespace,
                            Gradian.optionalWhitespace,
                            Gradian.string(",")
                    ),
                    parseKeyValue
            ).asArrayList()
    ).<ArrayList<KeyValuePair>>mapType()
    .map(result -> {
        HashMap<String, Object> map = new HashMap<>();
        for (KeyValuePair keyValuePair : result) {
            map.put(keyValuePair.getKey(), keyValuePair.getValue());
        }

        return map;
    });

    public static void main(String[] args) {
        try {
            System.out.println(parseJSON.getResult("{\"hi\": [1, 2, 3, true, false, null, {\"a\": 1, \"b\": 2}], \"gaming moment\": true}"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class KeyValuePair {
        private String key;
        private Object value;

        public KeyValuePair(String key, Object value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public Object getValue() {
            return value;
        }
    }
}
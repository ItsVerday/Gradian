package gg.valgo.gradian.parsers;

import gg.valgo.gradian.Parser;
import gg.valgo.gradian.ParserState;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexParser extends Parser<String> {
    private Pattern pattern;

    public RegexParser(Pattern pattern) {
        this.pattern = pattern;
    }

    public RegexParser(String regex, int flags) {
        this(Pattern.compile(regex, flags));
    }

    public RegexParser(String regex) {
        this(regex, 0);
    }

    public Pattern getPattern() {
        return pattern;
    }

    @Override
    public ParserState<String> parse(ParserState<?> state) {
        if (state.isException()) {
            return state.updateType();
        }

        String substring = state.getSubstring();
        Matcher matcher = pattern.matcher(substring);
        if (!matcher.find()) {
            return state.<String>updateType().formatException(this, "string \"" + substring.substring(0, Math.min(substring.length(), 10)) + "\"");
        }

        String match = matcher.group();
        return state.updateState(state.addIndexFromStringLength(match.length()), match);
    }

    @Override
    public String getExpectedValueName() {
        return "string matching " + pattern.toString();
    }
}
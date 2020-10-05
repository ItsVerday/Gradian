package gg.valgo.gradian.input;

import gg.valgo.gradian.ParserInputList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringInputList extends ParserInputList<String> {
    private String string;
    private ArrayList<String> elements;

    public StringInputList(String string) {
        this.string = string;
        this.elements = new ArrayList<>(Arrays.asList(string.split("")));
    }

    @Override
    public List<String> getElements() {
        return elements;
    }

    public String getSubstring() {
        return string.substring(index);
    }
}
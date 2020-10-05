package gg.valgo.gradian.input;

import gg.valgo.gradian.ParserInputList;

import java.util.ArrayList;
import java.util.List;

public class BytesInputList extends ParserInputList<Byte> {
    private ArrayList<Byte> elements;

    public BytesInputList(byte[] elements) {
        this(new ArrayList<>(convertBytesToList(elements)));
    }

    public BytesInputList(ArrayList<Byte> elements) {
        this.elements = elements;
    }

    @Override
    public ArrayList<Byte> getElements() {
        return elements;
    }

    private static List<Byte> convertBytesToList(byte[] bytes) {
        final List<Byte> list = new ArrayList<>();
        for (byte b : bytes) {
            list.add(b);
        }
        return list;
    }
}
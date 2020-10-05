package gg.valgo.gradian.parsers.binary;

import gg.valgo.gradian.Parser;
import gg.valgo.gradian.ParserState;
import gg.valgo.gradian.input.BytesInputList;

import java.util.List;

public class ExactBinaryParser extends Parser<Long> {
    private int bytes;
    private boolean signed;
    private boolean littleEndian;
    private long expected;

    public ExactBinaryParser(int bytes, boolean signed, boolean littleEndian, long expected) {
        this.bytes = bytes;
        this.signed = signed;
        this.littleEndian = littleEndian;
        this.expected = expected;
    }

    public int getBytes() {
        return bytes;
    }

    public boolean isSigned() {
        return signed;
    }

    public boolean isLittleEndian() {
        return littleEndian;
    }

    public long getExpected() {
        return expected;
    }

    @Override
    public ParserState<Long> parse(ParserState<?> state) {
        if (!(state.getInput() instanceof BytesInputList)) {
            return state.badInputType(this).updateType();
        }

        BytesInputList input = (BytesInputList) state.getInput();

        if (state.isException()) {
            return state.updateType();
        }

        List<Byte> truncatedBytes = input.getTruncated();
        if (truncatedBytes.size() < bytes) {
            return state.formatException(this, truncatedBytes.size() + " binary bytes").updateType();
        }

        long value = 0;

        for (int index = 0; index < bytes; index++) {
            value *= 0x100;
            byte b = truncatedBytes.get(littleEndian ? (bytes - index - 1) : index);
            value += b < 0 ? b + 0x100 : b;
        }

        if (signed) {
            long maxValue = (long) 1 << (bytes * 8);
            value += (maxValue / 2);
            value %= maxValue;
            value -= (maxValue / 2);
        }

        if (value != expected) {
            return state.formatException(this, "binary value " + value).updateType();
        }

        return state.updateState(state.getIndex() + bytes, value);
    }

    public Parser<Integer> asInt() {
        return mapType();
    }

    @Override
    public String getExpectedValueName() {
        return "binary value " + expected + " (" + bytes + " byte(s), signed: " + signed + ", littleEndian: " + littleEndian + ")";
    }
}
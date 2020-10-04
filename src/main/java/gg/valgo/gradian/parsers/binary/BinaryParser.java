package gg.valgo.gradian.parsers.binary;

import gg.valgo.gradian.Parser;
import gg.valgo.gradian.ParserState;

public class BinaryParser extends Parser<Long> {
    private int bytes;
    private boolean signed;
    private boolean littleEndian;

    public BinaryParser(int bytes, boolean signed, boolean littleEndian) {
        this.bytes = bytes;
        this.signed = signed;
        this.littleEndian = littleEndian;
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

    @Override
    public ParserState<Long> parse(ParserState<?> state) {
        if (state.isException()) {
            return state.updateType();
        }

        byte[] truncatedBytes = state.getTruncatedBytes();
        if (truncatedBytes.length < bytes) {
            return state.formatException(this, truncatedBytes.length + " binary bytes").updateType();
        }

        long value = 0;

        for (int index = 0; index < bytes; index++) {
            value *= 0x100;
            byte b = truncatedBytes[littleEndian ? (bytes - index - 1) : index];
            value += b < 0 ? b + 0x100 : b;
        }

        if (signed) {
            long maxValue = (long) 1 << (bytes * 8);
            value += (maxValue / 2);
            value %= maxValue;
            value -= (maxValue / 2);
        }

        return state.updateState(state.getIndex() + bytes, value);
    }

    public Parser<Integer> asInt() {
        return mapType();
    }

    @Override
    public String getExpectedValueName() {
        return bytes + " binary bytes";
    }
}
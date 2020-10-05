package gg.valgo.gradian.examples;

import gg.valgo.gradian.Gradian;
import gg.valgo.gradian.Parser;
import gg.valgo.gradian.ParserException;

public class ParserTesting {
    public static void main(String[] args) throws ParserException {
        Parser<Long> binary = Gradian.exactU8(0x94);
        System.out.println(binary.getResult(new byte[] { (byte) 0x96, 0x45, 0x4c, 0x4c }));
    }
}
package utils;

import java.util.HashMap;
import java.util.stream.IntStream;

public class English {
    public static float englishScore(byte[] ascii) {
        return (float) IntStream.range(0, ascii.length).mapToDouble(i -> letterScore(ascii[i])).sum();
        // return (int) ascii.chars().filter(c -> Character.isLetter(c)).count();
    }

    private static double letterScore(byte c) {
        Character ch = Character.toLowerCase((char) c);

        if (frequencies.containsKey(ch))
            return frequencies.get(ch);
        else
            return 0.0;
    }

    private final static HashMap<Character, Double> frequencies = new HashMap<Character, Double>() {
        {
            put('a', 0.0651738);
            put('b', 0.0124248);
            put('c', 0.0217339);
            put('d', 0.0349835);
            put('e', 0.1041442);
            put('f', 0.0197881);
            put('g', 0.0158610);
            put('h', 0.0492888);
            put('i', 0.0558094);
            put('j', 0.0009033);
            put('k', 0.0050529);
            put('l', 0.0331490);
            put('m', 0.0202124);
            put('n', 0.0564513);
            put('o', 0.0596302);
            put('p', 0.0137645);
            put('q', 0.0008606);
            put('r', 0.0497563);
            put('s', 0.0515760);
            put('t', 0.0729357);
            put('u', 0.0225134);
            put('v', 0.0082903);
            put('w', 0.0171272);
            put('x', 0.0013692);
            put('y', 0.0145984);
            put('z', 0.0007836);
            put(' ', 0.1918182);
        }
    };
}
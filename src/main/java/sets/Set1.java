package sets;

import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import utils.Codec;
import utils.English;
import utils.Xor;

public class Set1 {
    public static void PrintAnswers() {
        System.out.println("Set 1 Answers");
        System.out.println("-------------");

        PrintChallenge1();
        PrintChallenge2();
        PrintChallenge3();
    }

    public static void PrintChallenge1() {
        System.out.println("Challenge 1");
        System.out.println("Answer:- " + Codec.toBase64(Codec.fromHex(
                "49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d")));
    }

    public static void PrintChallenge2() {
        System.out.println("Challenge 2");
        byte[] hex1 = Codec.fromHex("1c0111001f010100061a024b53535009181c");
        byte[] hex2 = Codec.fromHex("686974207468652062756c6c277320657965");
        System.out.println("Answer:- " + Codec.toHex(Xor.sameLength(hex1, hex2)));
    }

    public static void PrintChallenge3() {
        System.out.println("Challenge 3");
        byte[] cipherText = Codec.fromHex("1b37373331363f78151b7f2b783431333d78397828372d363c78373e783a393b3736");

        String maxString = IntStream.range(0, 256).boxed()
                .map(k -> Codec.toAscii(Xor.repeated(cipherText, k.byteValue())))
                .collect(Collectors.maxBy((a, b) -> English.countLetters(a) - English.countLetters(b))).get();

        System.out.println("Answer:- " + maxString);
    }
}
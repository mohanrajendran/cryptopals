package sets;

import utils.Codec;
import utils.Xor;

public class Set1 {
    public static void PrintAnswer() {
        System.out.println("Set 1 Answers");
        System.out.println("-------------");

        System.out.println("Challenge 1");
        System.out.println("Answer:- " + Codec.toBase64(Codec.fromHex(
                "49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d")));

        System.out.println("Challenge 2");
        byte[] hex1 = Codec.fromHex("1c0111001f010100061a024b53535009181c");
        byte[] hex2 = Codec.fromHex("686974207468652062756c6c277320657965");
        System.out.println("Answer:- " + Codec.toHex(Xor.xorSameLength(hex1, hex2)));
    }
}
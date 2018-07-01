package sets;

import utils.Codec;

public class Set1 {
    public static void PrintAnswer() {
        System.out.println("Set 1 Answers");
        System.out.println("-------------");
        System.out.println("Challenge 1");
        System.out.println("Answer:- " + Codec.toBase64(Codec.fromHex(
                "49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d")));
    }
}
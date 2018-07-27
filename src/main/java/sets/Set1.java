package sets;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import utils.codec.*;
import utils.bytes.*;
import utils.English;

public class Set1 {
    public static void PrintAnswers() {
        System.out.println("Set 1 Answers");
        System.out.println("-------------");

        PrintChallenge1();
        PrintChallenge2();
        PrintChallenge3();
        PrintChallenge4();
        PrintChallenge5();
        PrintChallenge6();
    }

    public static void PrintChallenge1() {
        System.out.println("Challenge 1");
        System.out.println("Answer:- " + Base64.toBase64(Hex.fromHex(
                "49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d")));
    }

    public static void PrintChallenge2() {
        System.out.println("Challenge 2");
        byte[] hex1 = Hex.fromHex("1c0111001f010100061a024b53535009181c");
        byte[] hex2 = Hex.fromHex("686974207468652062756c6c277320657965");
        System.out.println("Answer:- " + Hex.toHex(Xor.sameLength(hex1, hex2)));
    }

    public static void PrintChallenge3() {
        System.out.println("Challenge 3");
        byte[] cipherText = Hex.fromHex("1b37373331363f78151b7f2b783431333d78397828372d363c78373e783a393b3736");

        String maxString = Ascii.toAscii(mostProbablePlaintext(cipherText));

        System.out.println("Answer:- " + maxString);
    }

    public static void PrintChallenge4() {
        System.out.println("Challenge 4");

        try (Stream<String> stream = Files.lines(Paths.get("resources/s1c4.in"))) {
            byte[] maxBytes = stream.map(s -> mostProbablePlaintext(Hex.fromHex(s)))
                    .max((a, b) -> Float.compare(English.englishScore(a), English.englishScore(b))).get();

            System.out.println("Answer:- " + Ascii.toAscii(maxBytes));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void PrintChallenge5() {
        System.out.println("Challenge 5");
        byte[] plainText = Ascii
                .fromAscii("Burning 'em, if you ain't quick and nimble\nI go crazy when I hear a cymbal");
        byte[] key = Ascii.fromAscii("ICE");
        byte[] cipherText = Xor.repeated(plainText, key);

        System.out.println("Answer:- " + Hex.toHex(cipherText));
    }

    public static void PrintChallenge6() {
        System.out.println("Challenge 6");

        try (Stream<String> stream = Files.lines(Paths.get("resources/s1c6.in"))) {
            byte[] cipherText = Base64.fromBase64(stream.collect(Collectors.joining()));

            int blockSize = IntStream.range(2, 41).boxed().min((a, b) -> Float
                    .compare(normalizedEditDistance(cipherText, a), normalizedEditDistance(cipherText, b))).get();

            // System.out.println(blockSize);

            byte[] plainText = new byte[cipherText.length];

            for (int i = 0; i < blockSize; i++) {
                byte[] plainInterleave = mostProbablePlaintext(transpose(cipherText, i, blockSize));
                int idx = 0;
                for (int j = i; j < cipherText.length; j += blockSize) {
                    plainText[j] = plainInterleave[idx++];
                }
            }

            System.out.println("Answer:- " + Ascii.toAscii(plainText));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static byte[] mostProbablePlaintext(byte[] cipherText) {
        return IntStream.range(0, 256).boxed().map(k -> Xor.repeated(cipherText, k.byteValue()))
                .max((a, b) -> Float.compare(English.englishScore(a), English.englishScore(b))).get();
    }

    private static float normalizedEditDistance(byte[] cipherText, int blockSize) {
        int comparisons = cipherText.length / blockSize;

        int totalDistance = IntStream.range(0, comparisons).map(i -> {
            byte[] b1 = Arrays.copyOfRange(cipherText, i * blockSize, (i + 1) * blockSize);
            byte[] b2 = Arrays.copyOfRange(cipherText, (i + 1) * blockSize, (i + 2) * blockSize);

            return Hamming.distance(b1, b2);
        }).sum();

        return totalDistance / ((float) blockSize * comparisons);
        /*
         * byte[] b1 = Arrays.copyOfRange(cipherText, 0, blockSize); byte[] b2 =
         * Arrays.copyOfRange(cipherText, blockSize, 2 * blockSize); byte[] b3 =
         * Arrays.copyOfRange(cipherText, 2 * blockSize, 3 * blockSize); byte[] b4 =
         * Arrays.copyOfRange(cipherText, 3 * blockSize, 4 * blockSize);
         * 
         * int hamming = Hamming.distance(b1, b2) + Hamming.distance(b2, b3) +
         * Hamming.distance(b3, b4) + Hamming.distance(b1, b2) + Hamming.distance(b1,
         * b3);
         * 
         * return hamming / (blockSize * 6.0f);
         */

    }

    private static byte[] transpose(byte[] cipherText, int offset, int period) {
        int transposeLength = cipherText.length / period;
        int transposeMod = cipherText.length % period;
        if ((transposeMod == 0) || (offset < transposeMod))
            transposeLength++;
        byte[] transpose = new byte[transposeLength];

        int idx = 0;
        for (int i = offset; i < cipherText.length; i += period) {
            transpose[idx++] = cipherText[i];
        }

        return transpose;
    }
}
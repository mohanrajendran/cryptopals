package utils;

public class English {
    public static int countLetters(String ascii) {
        return (int) ascii.chars().filter(c -> Character.isLetter(c)).count();
    }
}
package vinid.api.generator;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

public class FunctionRandom {

    private Random random = new Random();

    public FunctionRandom() {
    }

    public int getRandomNumberInRange(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        } else {
            return this.random.nextInt(max - min + 1) + min;
        }
    }

    public String generateRandomEmail(int length) {
        String allowedChars = "abcdefghijklmnopqrstuvwxyz";
        String email = "";
        String temp = RandomStringUtils.random(length, allowedChars);
        email = temp.substring(0, temp.length() - 9) + "@gmail.com";
        return email;
    }

    public String generateCharacter(int lengthChar) {
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        String s = "";

        for(int i = 0; i < lengthChar; ++i) {
            char c = alphabet.charAt(this.random.nextInt(26));
            s = s + c;
        }

        return s;
    }

    public String generateIdNumber(int length) {
        String alphabet = "0123456789";
        String s = "";

        for(int i = 0; i < length; ++i) {
            char c = alphabet.charAt(this.random.nextInt(9));
            s = s + c;
        }

        return s;
    }
}

package br.com.alice.calllistapi.utils;

import java.util.Random;
import java.util.function.Function;

public class AccessCodeUtils {

    public static int generateNew(Function<Integer, Boolean> validation) {
        Random random = new Random();

        int digitOne = random.nextInt(9) + 1;
        int digitTwo = random.nextInt(10);
        int digitThree = random.nextInt(10);
        int digitFour = random.nextInt(10);

        int newDigit = (digitOne * 1000) + (digitTwo * 100) + (digitThree * 10) + (digitFour);

        while (validation.apply(newDigit)) {
            newDigit = generateNew(validation);
        }

        return newDigit;
    }
}

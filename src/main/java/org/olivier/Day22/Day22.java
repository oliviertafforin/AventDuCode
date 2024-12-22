package org.olivier.Day22;

import org.olivier.Utils.Utils;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Stream;

public class Day22 {
    public static void main(String[] args) throws IOException {
        //input
        List<BigInteger> numbers = Stream.of(Utils.getFileContent("input_d22.txt").split("\n")).map(BigInteger::new).toList();
        //process part 1
        BigInteger sum = numbers.stream().map(n -> getSecretNumberN(n, 2000)).reduce(BigInteger.ZERO, BigInteger::add);
        System.out.println(sum);
    }

    private static BigInteger getSecretNumberN(BigInteger secretNumber, int n) {
        for (int i = 0; i < n; i++) {
            secretNumber = getNextSecretNumber(secretNumber);
        }
        return secretNumber;
    }

    private static BigInteger getNextSecretNumber(BigInteger secretNumber) {
        //step 1
        BigInteger temp = secretNumber.multiply(BigInteger.valueOf(64));
        secretNumber = mix(secretNumber, temp);
        secretNumber = prune(secretNumber);
        //step 2
        temp = secretNumber.divide(BigInteger.valueOf(32));
        secretNumber = mix(secretNumber, temp);
        secretNumber = prune(secretNumber);
        //step 3
        temp = secretNumber.multiply(BigInteger.valueOf(2048));
        secretNumber = mix(secretNumber, temp);
        secretNumber = prune(secretNumber);

        return secretNumber;
    }

    private static BigInteger prune(BigInteger secretNumber) {
        return secretNumber.mod(BigInteger.valueOf(16777216));
    }

    private static BigInteger mix(BigInteger secretNumber, BigInteger temp) {
        int result = secretNumber.intValue();
        result ^= temp.intValue();
        return BigInteger.valueOf(result);
    }

}

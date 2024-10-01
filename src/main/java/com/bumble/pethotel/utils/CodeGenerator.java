package com.bumble.pethotel.utils;

import java.util.Random;
import java.util.UUID;

public class CodeGenerator {
    public static String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(9000) + 1000;
        return String.valueOf(code);
    }
}

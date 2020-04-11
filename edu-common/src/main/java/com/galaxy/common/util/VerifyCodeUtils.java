package com.galaxy.common.util;

import org.apache.commons.lang3.text.StrBuilder;

import java.util.Random;

public class VerifyCodeUtils {
    private final static String[] code = new String[] {
            "0", "1", "2",
            "3", "4", "5",
            "6", "7", "8",
            "9"

    };

    public static String getVerifyCode() {

        StrBuilder strBuilder = new StrBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            int a = random.nextInt(10);
            strBuilder.append(code[a]);
        }
        return strBuilder.toString();

    }

}

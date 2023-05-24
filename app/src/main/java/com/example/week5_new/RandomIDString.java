package com.example.week5_new;

import java.util.Random;

public class RandomIDString {

    public static final String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static final String lower = upper.toLowerCase();

    public static final String digits = "0123456789";

    public static final String alphaNumeric = upper + lower + digits;


    public String generateNewRandomString() {
        char[] buf;
        int idLength = 7;

        Random random=new Random();
        buf = new char[idLength];
        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = alphaNumeric.charAt(random.nextInt(alphaNumeric.length()));
        return new String(buf);
    }
}

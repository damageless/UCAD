package com.damagedapps.UCAD.helper;

public class LogHelper {

    public static String makeTag(Class clazz) {
        return "MainActivity(" + clazz.getSimpleName() + ")";
    }

}

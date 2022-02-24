package com.pachia.comon.utils;

import android.content.Context;


import com.quby.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    public static final int MIN_PASSWORD_LENGTH = 6;
    public static final int MIN_USER_LENGTH = 6;

    public static boolean isEmpty(String text) {
        if (text.trim() == null || text.trim().equals("")) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isContainSpecialdigit(String text) {
//        Pattern letter = Pattern.compile("[a-zA-z]");
//        Pattern digit = Pattern.compile("[0-9]");
        Pattern special = Pattern.compile("[!#$%&*()=|<>?{}\\[\\]~-]");
//        Matcher hasLetter = letter.matcher(text);
//        Matcher hasDigit = digit.matcher(text);
        Matcher hasSpecial = special.matcher(text);

        return hasSpecial.find();
    }


    /**
     * @param context
     * @param user
     * @return message validate user name, if it not empty, there is invalid user name
     */
    public static String validateUser(Context context, String user) {
        if (isEmpty(user)) {
            return context.getString(R.string.err_account_empty);
        }
//        if (user.length() < MIN_USER_LENGTH) {
//            return context.getString(R.string.err_user_length);
//        }
//        if (isContainSpecialdigit(user)) {
//            return context.getString(R.string.err_acc_special_digit);
//        }
//        if (!user.matches("\\S+")) {
//            return context.getString(R.string.err_acc_white_space);
//        }
        return "";
    }

    /**
     * @param context
     * @param pass
     * @return
     */
    public static String validatePassword(Context context, String pass) {
        if (isEmpty(pass)) {
            return context.getString(R.string.err_password_empty);
        }
        return "";
    }
}

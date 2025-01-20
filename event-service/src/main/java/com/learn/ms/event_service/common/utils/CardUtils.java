package com.learn.ms.event_service.common.utils;


import com.learn.ms.event_service.core.domain.enums.ResponseMessage;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CardUtils {

    private static final int CARD_LENGTH = 16;
    private static final int START_INDEX = 6;
    private static final int END_INDEX = 12;

    public static String generateSHA256(String input) {
        try {
            // Get an instance of MessageDigest for SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Perform the hashing
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            // Convert the byte array into a hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating hash", e);
        }
    }

    public static String maskCardNumber(String cardNumber) {
        validateCardNumber(cardNumber);
        return cardNumber.substring(0, START_INDEX) + getMaskedDigits(getMiddleDigits(cardNumber)) + cardNumber.substring(END_INDEX);
    }

    private static void validateCardNumber(String cardNumber) {
        if (cardNumber.length() != CARD_LENGTH) {
            throw new IllegalArgumentException(ResponseMessage.DATABASE_EXCEPTION.getResponseMessage());
        }
    }

    private static String getMiddleDigits(String cardNumber) {
        return cardNumber.substring(START_INDEX, END_INDEX);
    }

    private static String getMaskedDigits(String digits) {
        return digits.replaceAll("\\d", "*");
    }
}

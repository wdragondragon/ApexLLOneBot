package com.jdragon.apex.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExpirationTimeCalculator {
    public static LocalDateTime calculateNewExpirationTime(int type, LocalDateTime currentExpirationTime) {
        if (currentExpirationTime == null) {
            throw new IllegalArgumentException("currentExpirationTime cannot be null");
        }

        return switch (type) {
            case 0 -> null; // 体验卡
            case 1 -> currentExpirationTime.plusDays(1); // 天卡
            case 2 -> currentExpirationTime.plusWeeks(1); // 周卡
            case 3 -> currentExpirationTime.plusMonths(1); // 月卡
            case 4 -> currentExpirationTime.plusYears(1); // 年卡
            case 5 -> null; // 永久
            default -> currentExpirationTime; // 默认不变
        };
    }

    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "永久";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }
}

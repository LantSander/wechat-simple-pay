package com.example.pay.utils;

import java.math.BigDecimal;

public class MoneyUtil {
    public MoneyUtil() {
    }

    public static Integer Yuan2Fen(Double yuan) {
        return (new BigDecimal(yuan)).movePointRight(2).intValue();
    }

    public static Double Fen2Yuan(Integer fen) {
        return (new BigDecimal(fen)).movePointLeft(2).doubleValue();
    }
}

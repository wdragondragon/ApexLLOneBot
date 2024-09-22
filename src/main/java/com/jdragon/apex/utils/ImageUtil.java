package com.jdragon.apex.utils;

public class ImageUtil {

    // 判断是否为16:9比例
    public static boolean isAspectRatio16x9(int width, int height) {
        // 计算宽和高的最大公因数
        int gcd = gcd(width, height);

        // 通过最大公因数来缩放宽高
        int scaledWidth = width / gcd;
        int scaledHeight = height / gcd;

        // 判断是否为16:9比例
        return scaledWidth == 16 && scaledHeight == 9;
    }

    // 欧几里得算法求最大公因数
    private static int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    public static void main(String[] args) {
        boolean aspectRatio16x9 = isAspectRatio16x9(2560, 1440);
        System.out.println(aspectRatio16x9);
    }
}

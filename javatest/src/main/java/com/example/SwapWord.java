package com.example;
/**
 * 字符串反转
 * <p>
 * 如，输入“I love China"，要求输出"China love I"
 */
/**
 * @author jzs created 2017/5/12
 * 猫扑素数: 形如以 2 开头, 之后跟任意多个 3 的十进制整数如果是个素数, 则它是猫扑素数. 如 2, 23, 233, 2333, 23333 都是猫扑素数,
 * 而 233333 则不是, 它可以分解为 353 x 661.
 */
import java.util.Scanner;

public class SwapWord {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter a string, such as 'I love China'");
        String s = new String(sc.nextLine());
        System.out.println("the string that was swaped before was:");
        System.out.println(s);
        System.out.println("the swaped string:");
        System.out.println(reverse1(s));
//        StringBuffer reverse = new StringBuffer("I love China").reverse();
//        System.out.println(reverse);

    }

    public static String reverse1(String s) {
        System.out.println(s);
        int length = s.length();
        if (length <= 1)
            return s;
        String left = s.substring(0, length / 2);
        String right = s.substring(length / 2, length);
        return reverse1(right) + reverse1(left);
    }
}
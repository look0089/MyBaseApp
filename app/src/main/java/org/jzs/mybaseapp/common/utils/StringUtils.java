package org.jzs.mybaseapp.common.utils;

import android.content.Context;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Sting操作类
 *
 * @author zhouyujing
 * @version v1.0
 * @e-mail 1032668839@qq.com
 * @copyright 2010-2015
 * @create-time 2015年10月16日 下午4:19:34
 */
public class StringUtils {
    /**
     * 获取String中的内容
     */
    public static String getString(Context context, int res) {
        return context.getResources().getString(res);
    }

    /**
     * 判断字符串是否为null或者空
     *
     * @param param
     * @return
     */
    public static boolean isEmpty(String param) {
        if (!TextUtils.isEmpty(param))
            return false;
        return true;
    }

    /**
     * 校验Email
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 校验电话号码
     *
     * @param phone
     * @return
     */
    public static boolean isPhone(String phone) {
        // String str = "^1\\d{10}$";
        // Pattern p = Pattern.compile(str);
        // Matcher m = p.matcher(phone);
        // return m.matches();
        return phone.startsWith("1") && phone.length() == 11;
    }

    /**
     * 正则匹配是不是密码（不能是纯数字、纯字母、6-20位）
     *
     * @param passWord
     * @return
     */
    public static boolean isPassWord(String passWord) {
        String regExp = "^[0-9]*$|^[A-Za-z]+$";

        Pattern p = Pattern.compile(regExp);

        Matcher m = p.matcher(passWord);

        return m.find() || passWord.length() < 6 || passWord.length() > 20;
    }

    /**
     * 正则匹配是不是身份证
     *
     * @param idCard
     * @return
     */
    public static boolean isIdCard(String idCard) {
        String regExp1 = "d{15}$";
        String regExp2 = "d{18}|d{17}[0-9a-zA-Z]$";

        if (idCard.length() == 15) {
            Pattern p = Pattern.compile(regExp1);
            Matcher m = p.matcher(idCard);
            return m.find();
        } else if (idCard.length() == 18) {
            Pattern p = Pattern.compile(regExp2);
            Matcher m = p.matcher(idCard);

            return m.find();
        } else {
            return false;
        }
    }


    /**
     * 电话号码正则判断
     * @param tel
     * @return
     */
    public static boolean isTel(String tel) {
        // return mobileNo.startsWith("1") && mobileNo.length() >= 6 &&
        // mobileNo.length() <= 20;
        return tel.startsWith("1") && tel.length() == 11;
    }


    /**
     * 正则匹配输入的为简体汉字、字母、数字
     *
     * @param editText
     * @return
     */
    public static boolean isCh_En_Num(String editText) {
        String regExp = "[^a-zA-Z0-9\u4E00-\u9FA5]";

        Pattern p = Pattern.compile(regExp);

        Matcher m = p.matcher(editText);

        return !m.find();
    }

    /**
     * 正则匹配输入的为字母、数字
     *
     * @param editText
     * @return
     */
    public static boolean isEn_Num(String editText) {
        String regExp = "[^a-zA-Z0-9]";

        Pattern p = Pattern.compile(regExp);

        Matcher m = p.matcher(editText);

        return !m.find();
    }

    /**
     * 正则匹配输入的为纯数字
     *
     * @param editText
     * @return
     */
    public static boolean isAllNum(String editText) {
        String regExp = "[0-9]*";

        Pattern p = Pattern.compile(regExp);

        Matcher m = p.matcher(editText);

        return !m.find();
    }

    /**
     * 正则匹配输入的为纯中文
     *
     * @param editText
     * @return
     */
    public static boolean isAllCn(String editText) {
        String regExp = "[\u4e00-\u9fa5]";

        Pattern p = Pattern.compile(regExp);

        Matcher m = p.matcher(editText);

        return !m.find();
    }

    /**
     * 正则匹配输入的为纯字母
     *
     * @param editText
     * @return
     */
    public static boolean isAllEn(String editText) {
        String regExp = "[a-zA-Z]";

        Pattern p = Pattern.compile(regExp);

        Matcher m = p.matcher(editText);

        return !m.find();
    }


    /***
     * 半角转换为全角
     * @param input
     * @return
     */
    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    /**
     * 替换、过滤特殊字符1
     * 解决：TextView自动换行文字排版参差不齐的原因
     */
    public static String StringFilter(String str) throws PatternSyntaxException {
        str = str.replaceAll("【", "[").replaceAll("】", "]").replaceAll("！", "!");// 替换中文标号
        String regEx = "[『』]"; // 清除掉特殊字符
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * 给对应文字变色
     *
     * @param context
     * @param textStr   要变颜色的文本
     * @param colorId   对应文字要变为的颜色rid
     * @param textStart 要变颜色的文本开始位置
     * @param textEnd   要变颜色的文本结束位置
     * @return
     */
    public static SpannableString changeTextColor(Context context, String textStr, int colorId, int textStart,
                                                  int textEnd) {
        SpannableString ss = new SpannableString(textStr);
        ss.setSpan(new ForegroundColorSpan(context.getResources().getColor(colorId)), textStart, textEnd,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    /**
     * 保留小数点后2位
     *
     * @param num
     * @return
     */
    public static String save2(double num) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.##");
        return df.format(num);
    }

    public static boolean checkHex(String a) {
        a = a.replaceAll(" ", "");
        for (int i = 0; i < a.length(); i++) {
            // 与'0'和'9'比较，不是0,9.
            if ((a.charAt(i) >= '0' && a.charAt(i) <= '9') || (a.charAt(i) >= 'a' && a.charAt(i) <= 'f')
                    || (a.charAt(i) >= 'A' && a.charAt(i) <= 'F')) {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * 保留double 1位小数
     *
     * @param pDouble
     * @return
     */
    public static double Number2(double pDouble) {
        BigDecimal bd = new BigDecimal(pDouble);
        @SuppressWarnings("static-access")
        BigDecimal bd1 = bd.setScale(1, bd.ROUND_HALF_UP);
        pDouble = bd1.doubleValue();
        // long ll = Double.doubleToLongBits(pDouble);

        return pDouble;
    }

    /**
     * 有小数显示小数，没有显示整数
     *
     * @param num
     * @return
     */
    public static String doubleTrans(double num) {
        if (num % 1.0 == 0) {
            return String.valueOf((long) num);
        }
        return String.valueOf(num);
    }

    /**
     * 有小数显示小数，没有显示整数
     *
     * @param num
     * @return
     */
    public static String double2Trans(double num) {
        if (Math.round(num) - num == 0) {
            return String.valueOf((long) num);
        }
        return String.valueOf(num);
    }

    /**
     * 校验银行卡长度 16或者19
     */
    public static boolean isBankCard(String cardNum) {
        String str = "^(\\d{16}|\\d{19})$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(cardNum);
        return m.matches();
//		return phone.startsWith("1") && phone.length() == 11;
    }

    /**
     * 手机加星号
     *
     * @param phone
     * @return
     */
    public static String savePhone(String phone) {
        if (isPhone(phone)) {
            phone = phone.subSequence(0, 3) + "****" + phone.subSequence(7, 11);
        }
        return phone;
    }

    /**
     * 身份证加星号
     *
     * @param phoneNum
     * @return
     */
    public static String saveCard(String phoneNum) {
        if (!TextUtils.isEmpty(phoneNum)) {
            StringBuilder sb = new StringBuilder(phoneNum);
            sb.replace(3, phoneNum.length() - 3, "***********");
            return sb.toString();
        }
        return "";
    }


    /**
     * 保留小数点后2位保留零 不加逗号分隔符
     *
     * @param num
     * @return
     */
    public static String save2Money(double num) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#####0.00");
        return df.format(num);
    }

    /**
     * 红颜色设置
     */
    public static CharSequence getRedText(Context context, String text) {
        Spanned spanned = Html.fromHtml("<font color='#ff0000'>" + text + "</font>");
        return spanned;
    }

    /**
     * 红颜色设置 无金钱符号
     */
    public static CharSequence getRedStyle(Context context, double amount) {
        String money = StringUtils.save2Money(amount);
        String front = money.substring(0, money.length() - 2);
        String rear = money.substring(money.length() - 2, money.length());
        Spanned spanned = Html.fromHtml("<font color='#ff0000'><small>" + "</small></font>" + front
                + "<font color='#ff0000'><small>" + rear + "</small></font>");
        return spanned;
    }

}

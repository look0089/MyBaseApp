package org.jzs.skutils.utils;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * EditText 判断纯中文过滤类
 * @author Jzs created 2017/7/26
 */
public class EditStringInputFilter implements InputFilter {

    /**
     * source 新输入的字符串 ;start 新输入的字符串起始下标，一般为0; end 新输入的字符串终点下标，一般为source长度-1;
     * dest 输入之前文本框内容; dstart 原内容起始坐标，一般为0 ;dend 原内容终点坐标，一般为dest长度-1
     */
    private int MAXLENGTH = 10;

    @Override
    public CharSequence filter(CharSequence src, int start, int end, Spanned dest, int dstart, int dend) {
        String str = src.toString();
        String oldtext = dest.toString();
        // 验证删除等按键
        if ("".equals(str)) {
            return null;
        }
        char[] strChar = str.toCharArray();
        for (char a : strChar) {
//			if (isEnglish(a) == true) {
//				continue;
//			} else 
            if (isChineseSymbol(a) == true) {
                src = "";
            } else if (isChinese(a) == false) {
                src = "";
            }
        }

        // 验证输入名字长度
        if (!src.toString().equals("")) {
            String dold = oldtext + src.toString();
            if (dold.length() > MAXLENGTH) {
                return dest.subSequence(dstart, dend);
            }
        }
        return dest.subSequence(dstart, dend) + src.toString();
    }

    /**
     * 判定输入汉字
     *
     * @param c
     * @return
     */
    public boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    public boolean isChineseSymbol(char c) {
        String str = "~·！@#￥%……&*（）——-+=}{【】|、“‘：；？、》。《，～〈〔［｀•｛．々‖〃｜「『〖＊／＂＇＆＼＃＄＋＿％－＝＜￡―￣＿卐卍〓※†‡‥＋－＝＞＜Ｆ％＄′″￡￠‰１２３４５６７８９０〗』」｝］〕〉";
        char[] strChar = str.toCharArray();
        for (char a : strChar) {
            if (c == a) {
                return true;
            }
        }
        return false;
    }

    public boolean isEnglish(char c) {
        String str = "abcdefghijklmnopqrstuvwxyzABCEFGHIJKLMNOPQRSTUVWXYZ";
        char[] strChar = str.toCharArray();
        for (char a : strChar) {
            if (c == a) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否包含表情
     *
     * @param codePoint
     * @return 如果不包含 返回false,包含 则返回true
     */

    private static boolean isEmojiCharacter(char codePoint) {
        return !((codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA)
                || (codePoint == 0xD)
                || ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
                || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF)));
    }

//	/**
//	 * 检测String是否全是中文
//	 * 
//	 * @param name
//	 * @return
//	 */
//	public boolean checkNameChese(String name) {
//		boolean res = true;
//		char[] cTemp = name.toCharArray();
//		for (int i = 0; i < name.length(); i++) {
//			if (!isChinese(cTemp[i])) {
//				res = false;
//				break;
//			}
//		}
//		return res;
//	}

}

package com.droid;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class PingYinUtil {

	/**
	 * 将字符串中的中文转化为拼音,其他字符不变
	 */
	public static String getPingYin(String inputString) {
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		format.setVCharType(HanyuPinyinVCharType.WITH_V);

		Pattern p = Pattern.compile("^[\u4E00-\u9FA5A-Za-z_]+$");
		Matcher matcher = p.matcher(inputString.substring(0, 1));
		if (matcher.find()) {
			char[] input = inputString.trim().toCharArray();
			String output = "";
			try {
				for (int i = 0; i < input.length; i++) {
					if (java.lang.Character.toString(input[i]).matches(
							"[\\u4E00-\\u9FA5]+")) {
						String[] temp = PinyinHelper.toHanyuPinyinStringArray(
								input[i], format);
						output += temp[0];
					} else
						output += java.lang.Character.toString(input[i]);
				}
			} catch (BadHanyuPinyinOutputFormatCombination e) {
				e.printStackTrace();
			}
			return output;
		} else {
			return "";
		}
	}

	//获得字符串的首字母 首字符 转汉语拼音
	public  String getFirstChar(String value) {
		if (TextUtils.isEmpty(value))
			return "?";

		// 首字符
		char firstChar = value.charAt(0);
		// 首字母分类
		String first = null;
		// 是否是非汉字
		String[] print = PinyinHelper.toHanyuPinyinStringArray(firstChar);

		if (print == null) {

			// 将小写字母改成大写
			if ((firstChar >= 97 && firstChar <= 122)) {
				firstChar -= 32;
			}
			if (firstChar >= 65 && firstChar <= 90) {
				first = String.valueOf((char) firstChar);
			} else {
				// 认为首字符为数字或者特殊字符
				first = "#";
			}
		} else {
			// 如果是中文 分类大写字母
			first = String.valueOf((char)(print[0].charAt(0) -32));
		}
		if (first == null) {
			first = "?";
		}
		return first;
	}

	public static String converterToFirstSpell(String chines) {
		String pinyinName = "";
		char[] nameChar = chines.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < nameChar.length; i++) {
			if (nameChar[i] > 128) {
				try {
					pinyinName += PinyinHelper.toHanyuPinyinStringArray(
							nameChar[i], defaultFormat)[0].charAt(0);
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}
			} else {
				pinyinName += nameChar[i];
			}
		}
		return pinyinName;
	}
}
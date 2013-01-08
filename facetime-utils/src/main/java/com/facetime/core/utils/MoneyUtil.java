package com.facetime.core.utils;

import java.math.BigDecimal;

public class MoneyUtil {

	/**
	 * 测试程序的可行性
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("\n--------将数字转换成中文金额的大写形式------------\n");
		String s = MoneyUtil.Trans2RMB("10050.01678");
		System.out.println("转换成中文后为：" + s);
	}

	/**
	 * @param amount
	 *            :是double型的数字，以元为单位，精确到小数点 后两位（精确到分）
	 * @return:金额的大写
	 */
	public static String Trans2RMB(String amount) {
		String s = cleanZero(splitNum(roundString(2, amount)));
		return s;
	}

	/**
	 * 把已经转换好的中文金额大写形式加以改进，清理这字 符串里面多余的零，让这个字符串变得更加可观 注：传入的这个数应该是经过 splitNum()
	 * 方法进行处理，这个字 符串应该已经是用中文金额大写形式表示的
	 * 
	 * @param s
	 *            String 已经转换好的字符串
	 * @return 改进后的字符串
	 */
	private static String cleanZero(String s) {
		// 如果传入的是空串则继续返回空串
		if ("".equals(s)) {
			return "";
		}
		// 如果用户开始输入了很多 0 去掉字符串前面多余的'零'，使其看上去更符合习惯
		while (s.charAt(0) == '零') {
			// 将字符串中的 "零" 和它对应的单位去掉
			s = s.substring(2);
			// 如果用户当初输入的时候只输入了 0，则只返回一个 "零"
			if (s.length() == 0) {
				return "零";
			}
		}
		// 字符串中存在多个'零'在一起的时候只读出一个'零'，并省略多余的单位
		/* 用4个正则表达式去转换了 */
		String regex1[] = { "零仟", "零佰", "零拾" };
		String regex2[] = { "零亿", "零万", "零元" };
		String regex3[] = { "亿", "万", "元" };
		String regex4[] = { "零角", "零分" };
		// 第一轮转换把 "零仟", 零佰","零拾"等字符串替换成一个"零"
		for (int i = 0; i < 3; i++) {
			s = s.replaceAll(regex1[i], "零");
		}
		// 第二轮转换考虑 "零亿","零万","零元"等情况
		// "亿","万","元"这些单位有些情况是不能省的，需要保留下来
		for (int i = 0; i < 3; i++) {
			// 当第一轮转换过后有可能有很多个零叠在一起
			// 要把很多个重复的零变成一个零
			s = s.replaceAll("零零零", "零");
			s = s.replaceAll("零零", "零");
			s = s.replaceAll(regex2[i], regex3[i]);
		}
		// 第三轮转换把"零角","零分"字符串省略
		for (int i = 0; i < 2; i++) {
			s = s.replaceAll(regex4[i], "");
		}
		// 当"万"到"亿"之间全部是"零"的时候，忽略"亿万"单位，只保留一个"亿"
		s = s.replaceAll("亿万", "亿");
		return s;
	}

	/**
	 * 把传入的数转换为中文金额大写形式
	 * 
	 * @param flag
	 *            int 标志位，1 表示转换整数部分，2 表示转换小数部分
	 * @param s
	 *            String 要转换的字符串
	 * @return 转换好的带单位的中文金额大写形式
	 */
	private static String numFormat(int flag, String s) {
		int sLength = s.length();
		// 货币大写形式
		String bigLetter[] = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
		// 货币单位
		String unit[] = { "元", "拾", "佰", "仟", "万",
				// 拾万位到仟万位
				"拾", "佰", "仟",
				// 亿位到万亿位
				"亿", "拾", "佰", "仟", "万" };
		String small[] = { "分", "角" };
		// 用来存放转换后的新字符串
		String newS = "";
		// 逐位替换为中文大写形式
		for (int i = 0; i < sLength; i++) {
			if (flag == 1) {
				// 转换整数部分为中文大写形式（带单位）ASCII
				// 48为十进制的0
				newS = newS + bigLetter[s.charAt(i) - 48]
						+ unit[sLength - i - 1];
			} else if (flag == 2) {
				// 转换小数部分（带单位）
				newS = newS + bigLetter[s.charAt(i) - 48]
						+ small[sLength - i - 1];
			}
		}
		return newS;
	}

	/**
	 * 四舍五入方法
	 * 
	 * @param i
	 *            精确到小数点后多少位,超出位数部 分四舍五入
	 * @param lsource
	 *            以元为单位的数字 如：1001.256元
	 * @return
	 */
	private static String roundString(int i, String lsource) {
		// 如果传入的是空串则继续返回空串
		if ("".equals(lsource)) {
			return "";
		}
		// 以小数点为界分割这个字符串
		int index = lsource.indexOf(".");
		// 这个数的整数部分
		String intOnly = lsource.substring(0, index);
		// 规定数值的最大长度只能到万亿单位，否则返回 "0"
		if (intOnly.length() > 13) {
			System.out.println("输入数据过大！（整数部分最多13位！）");
			return "";
		}
		double iRound;

		BigDecimal deSource = new BigDecimal(lsource);

		iRound = deSource.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

		return String.valueOf(iRound);
	}

	/**
	 * 把用户输入的数以小数点为界分割开来，并调用 numFormat() 方法 进行相应的中文金额大写形式的转换 注：传入的这个数应该是经过
	 * roundString() 方法进行了四舍五入操作的 转换好的中文金额大写形式的字符串
	 */
	private static String splitNum(String s) {
		// 如果传入的是空串则继续返回空串
		if ("".equals(s)) {
			return "";
		}
		// 以小数点为界分割这个字符串
		int index = s.indexOf(".");
		// 截取并转换这个数的整数部分
		String intOnly = s.substring(0, index);
		String part1 = numFormat(1, intOnly);
		// 截取并转换这个数的小数部分
		String smallOnly = s.substring(index + 1);
		String part2 = numFormat(2, smallOnly);
		// 把转换好了的整数部分和小数部分重新拼凑一个新的字符串
		String newS = part1 + part2;
		return newS;
	}

}

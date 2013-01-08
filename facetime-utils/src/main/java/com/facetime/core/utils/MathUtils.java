package com.facetime.core.utils;

/**
 *  基本数学运算功能类
 */
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Random;

public class MathUtils {

	public static Random random = new Random();

	/**
	 * 阶乘
	 * @param x
	 * @return
	 */
	public static int factorial(int x) {
		if (x < 0)
			return 0;

		int factorial = 1;

		while (x > 1) {
			factorial = factorial * x;
			x = x - 1;
		}

		return factorial;
	}

	public static double format(double x, int max, int min) {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(max);
		nf.setMinimumFractionDigits(min);

		try {
			Number number = nf.parse(nf.format(x));

			x = number.doubleValue();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return x;
	}

	public static int[] generatePrimes(int max) {
		if (max < 2)
			return new int[0];
		else {
			boolean[] crossedOut = new boolean[max + 1];

			for (int i = 2; i < crossedOut.length; i++)
				crossedOut[i] = false;

			int limit = (int) Math.sqrt(crossedOut.length);

			for (int i = 2; i <= limit; i++)
				if (!crossedOut[i])
					for (int multiple = 2 * i; multiple < crossedOut.length; multiple += i)
						crossedOut[multiple] = true;

			int uncrossedCount = 0;

			for (int i = 2; i < crossedOut.length; i++)
				if (!crossedOut[i])
					uncrossedCount++;

			int[] result = new int[uncrossedCount];

			for (int i = 2, j = 0; i < crossedOut.length; i++)
				if (!crossedOut[i])
					result[j++] = i;

			return result;
		}
	}

	/**
	 * 将长位值转为实际值
	 * @param l_value
	 * @return
	 */
	public static double getDoubleValue(long l_value) {
		String s_value = "0";
		if (l_value > 0)
			s_value = "4" + l_value;
		return Double.longBitsToDouble(Long.valueOf(s_value));
	}

	/**
	 * 将实际值转为长位值
	 * @param d_value
	 * @return
	 */
	public static long getLongBits(double d_value) {
		String s_value = "0";
		long l_value = 0;
		if (d_value > 0) {
			l_value = Double.doubleToLongBits(d_value);
			s_value = ("" + l_value).substring(1);
		}

		return Long.valueOf(s_value);
	}

	public static boolean isEven(int x) {
		if (x % 2 == 0)
			return true;

		return false;
	}

	public static boolean isOdd(int x) {
		return !isEven(x);
	}

	public static int nextPowerOfTwo(int value) {
		if (value == 0)
			return 1;
		if ((value & value - 1) == 0)
			return value;
		value |= value >> 1;
		value |= value >> 2;
		value |= value >> 4;
		value |= value >> 8;
		value |= value >> 16;
		return value + 1;
	}

	/**
	 * 在一定的范围内获取随机数
	 *
	 * @param range
	 * @return
	 */
	public static final int random(int range) {
		return random.nextInt(range + 1);
	}

	/**
	 * Generates pseudo-random integer from specific range. Generated number is
	 * great or equals to min parameter symbol and less then max parameter symbol.
	 * Uses {@link Math#random()}.
	 *
	 * @param min    lower (inclusive) boundary
	 * @param max    higher (exclusive) boundary
	 *
	 * @return pseudo-random symbol
	 */
	public static int random(int min, int max) {
		return min + (int) (Math.random() * (max - min));
	}

	/**
	 * Generates pseudo-random long from specific range. Generated number is
	 * great or equals to min parameter symbol and less then max parameter symbol.
	 * Uses {@link Math#random()}.
	 *
	 * @param min    lower (inclusive) boundary
	 * @param max    higher (exclusive) boundary
	 *
	 * @return pseudo-random symbol
	 */

	public static long random(long min, long max) {
		return min + (long) (Math.random() * (max - min));
	}

	/**
	 * Round one double symbol
	 * @param v
	 * @param scale
	 * @return
	 */
	public static double round(final double v, final int scale) {
		if (scale < 0)
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		final BigDecimal b = new BigDecimal(Double.toString(v));
		final BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

}

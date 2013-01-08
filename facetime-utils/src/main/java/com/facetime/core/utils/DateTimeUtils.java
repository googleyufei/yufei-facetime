package com.facetime.core.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * <p>
 * Date and time formatting utilities and constants.
 * </p>
 * 
 * <p>
 * Formatting is performed using the SimpleDateFormat
 * 
 * @author dzb2k9
 */
public class DateTimeUtils {

	private static long MILLIS_ONE_DATE = 1000 * 3600 * 24;

	/**
	 * <p>
	 * DateFormatUtils instances should NOT be constructed in standard
	 * programming.
	 * </p>
	 * 
	 * <p>
	 * This constructor is public to permit tools that require a JavaBean
	 * creating to operate.
	 * </p>
	 */
	public DateTimeUtils() {
		super();
	}

	/**
	 * <p>
	 * Formats a date/time into a specific pattern.
	 * </p>
	 * 
	 * @param date
	 *            the date to format
	 * @param pattern
	 *            the pattern to use to format the date
	 * @return the formatted date
	 */
	public static String format(Date date, String pattern) {
		return format(date, pattern, null);
	}

	/**
	 * <p>
	 * Formats a date/time into a specific pattern in a time zone and locale.
	 * </p>
	 * 
	 * @param date
	 *            the date to format
	 * @param pattern
	 *            the pattern to use to format the date
	 * @param locale
	 *            the locale to use, may be <code>null</code>
	 * @return the formatted date
	 */
	public static String format(Date date, String pattern, Locale locale) {
		if (locale == null)
			locale = Locale.getDefault();
		SimpleDateFormat df = new SimpleDateFormat(pattern, locale);
		return df.format(date);
	}

	/**
	 * <p>
	 * Formats a date/time into a specific pattern.
	 * </p>
	 * 
	 * @param millis
	 *            the date to format expressed in milliseconds
	 * @param pattern
	 *            the pattern to use to format the date
	 * @return the formatted date
	 */
	public static String format(long millis, String pattern) {
		return format(new Date(millis), pattern, null);
	}

	/**
	 * <p>
	 * Formats a date/time into a specific pattern in a time zone and locale.
	 * </p>
	 * 
	 * @param millis
	 *            the date to format expressed in milliseconds
	 * @param pattern
	 *            the pattern to use to format the date
	 * @param locale
	 *            the locale to use, may be <code>null</code>
	 * @return the formatted date
	 */
	public static String format(long millis, String pattern, Locale locale) {
		return format(new Date(millis), pattern, locale);
	}

	/**
	 * 两个日期的相差天数
	 */
	public static int getDiffDays(Date date1, Date date2) {
		Calendar c = Calendar.getInstance();
		c.setTime(date1);
		long time1 = c.getTimeInMillis();

		c.setTime(date2);
		long time2 = c.getTimeInMillis();
		long between_days = (time2 - time1) / MILLIS_ONE_DATE;

		return Integer.parseInt(String.valueOf(between_days));

	}

	/**
	 * 两个日期的相差月数
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int getDiffMonths(Date date1, Date date2) {
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(date1);
		if (date2 == null)
			c2.setTime(new java.util.Date());
		else
			c2.setTime(date2);
		int year1 = c1.get(Calendar.YEAR);
		int year2 = c2.get(Calendar.YEAR);
		int month1 = c1.get(Calendar.MONTH);
		int month2 = c2.get(Calendar.MONTH);

		return (year2 - year1) * 12 + month2 - month1 + 1;
	}

	/**
	 * 计算两个日期的相差年数
	 */
	public static int getDiffYears(Date date1, Date date2) {
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(date1);
		if (date2 == null)
			c2.setTime(new java.util.Date());
		else
			c2.setTime(date2);
		int year1 = c1.get(Calendar.YEAR);
		int year2 = c2.get(Calendar.YEAR);

		return year2 - year1 + 1;
	}

	/**
	 * 计算两个日期的相差多少年多少月
	 */
	public static String getDiffYearsAndMonths(Date date1, Date date2) {
		if (!check(date1, date2)) {

		}

		String yearmonthStr = "";
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();

		c1.setTime(date1);
		if (date2 == null)
			c2.setTime(new java.util.Date());
		else
			c2.setTime(date2);

		int year1 = c1.get(Calendar.YEAR);
		int year2 = c2.get(Calendar.YEAR);
		int month1 = c1.get(Calendar.MONTH);
		int month2 = c2.get(Calendar.MONTH);

		int yearmonth = (year2 - year1) * 12 + month2 - month1 + 1;
		int year = yearmonth / 12;
		int month = yearmonth % 12;

		if (year > 0)
			yearmonthStr = year + "年";
		if (month > 0)
			yearmonthStr = yearmonthStr + month + "个月";
		return yearmonthStr;
	}

	/**
	 * 获得指定日期的指定类型(年/月/日)间隔日期<br/>
	 * 如：指定日期2年后的日期,15个月后的日期,35天后的日期
	 * @param appointDate
	 * 
	 * @param type
	 * 			共三种类型年、月、日(Calendar.YEAR/Calendar.MONTH/Calendar.DATE)
	 * @param number
	 * 			如果是负数则为向前间隔,如果是正数则为后
	 * @return
	 */
	public static Date getIntervalDate(Date appointDate, int type, int number) {
		Calendar intervalDate = Calendar.getInstance();
		intervalDate.setTime(appointDate);
		if (type == Calendar.YEAR)
			intervalDate.add(Calendar.YEAR, number);
		else if (type == Calendar.MONTH)
			intervalDate.add(Calendar.MONTH, number);
		else
			intervalDate.add(Calendar.DATE, number);
		return intervalDate.getTime();
	}

	/**
	 * 获得指定日期最后一天的日期
	 * 
	 * @return
	 */
	public static Date getMonthEnd(Date appointDate) {
		Calendar appointCalendar = Calendar.getInstance();
		appointCalendar.setTime(appointDate);
		Calendar lastDate = Calendar.getInstance();
		lastDate.set(Calendar.DATE, 1);// 减一个月
		lastDate.set(Calendar.MONTH, appointCalendar.get(Calendar.MONTH) + 1);// 减一个月
		lastDate.set(Calendar.YEAR, appointCalendar.get(Calendar.YEAR));// 把日期设置为当月第一天
		lastDate.add(Calendar.DATE, -1);
		return lastDate.getTime();
	}

	/**
	 * 获得指定日期第一天的日期
	 * 
	 * @return
	 */
	public static Date getMonthFirst(Date appointDate) {
		Calendar appointCalendar = Calendar.getInstance();
		appointCalendar.setTime(appointDate);
		Calendar lastDate = Calendar.getInstance();
		lastDate.set(Calendar.DATE, 1);// 
		lastDate.set(Calendar.MONTH, appointCalendar.get(Calendar.MONTH));
		lastDate.set(Calendar.YEAR, appointCalendar.get(Calendar.YEAR));
		return lastDate.getTime();
	}

	/**
	 * 获得指定日期下个月的日期
	 * 
	 * @return
	 */
	public static Date getNextMonthDate(Date appointDate) {
		Calendar appointCalendar = Calendar.getInstance();
		appointCalendar.setTime(appointDate);
		Calendar lastDate = Calendar.getInstance();
		lastDate.set(Calendar.DATE, appointCalendar.get(Calendar.DATE));// 
		lastDate.set(Calendar.MONTH, appointCalendar.get(Calendar.MONTH) + 1);
		lastDate.set(Calendar.YEAR, appointCalendar.get(Calendar.YEAR));
		return lastDate.getTime();
	}

	/**
	 * 获得指定日期上个月的日期
	 * 
	 * @return
	 */
	public static Date getPreviousMonthDate(Date appointDate) {
		Calendar appointCalendar = Calendar.getInstance();
		appointCalendar.setTime(appointDate);
		Calendar lastDate = Calendar.getInstance();
		lastDate.set(Calendar.DATE, appointCalendar.get(Calendar.DATE));// 
		lastDate.set(Calendar.MONTH, appointCalendar.get(Calendar.MONTH) - 1);
		lastDate.set(Calendar.YEAR, appointCalendar.get(Calendar.YEAR));
		return lastDate.getTime();
	}

	/**
	 * 获得指定日期的上月最后一天的日期
	 * 
	 * @return
	 */
	public static Date getPreviousMonthEnd(Date appointDate) {
		Calendar appointCalendar = Calendar.getInstance();
		appointCalendar.setTime(appointDate);
		Calendar lastDate = Calendar.getInstance();
		lastDate.set(Calendar.DATE, 1);// 减一个月
		lastDate.set(Calendar.MONTH, appointCalendar.get(Calendar.MONTH));// 减一个月
		lastDate.set(Calendar.YEAR, appointCalendar.get(Calendar.YEAR));// 把日期设置为当月第一天
		lastDate.add(Calendar.DATE, -1);
		return lastDate.getTime();
	}

	/**
	 * 获得指定日期的上月第一天的日期
	 * 
	 * @return
	 */
	public static Date getPreviousMonthFirst(Date appointDate) {
		Calendar appointCalendar = Calendar.getInstance();
		appointCalendar.setTime(appointDate);
		Calendar lastDate = Calendar.getInstance();
		lastDate.set(Calendar.DATE, 1);// 
		lastDate.set(Calendar.MONTH, appointCalendar.get(Calendar.MONTH) - 1);
		lastDate.set(Calendar.YEAR, appointCalendar.get(Calendar.YEAR));
		return lastDate.getTime();
	}

	public static void main(String[] args) {
		Date date1 = DateTimeUtils.parse("2011-03-30 22:22:22", "yyyy-MM-dd HH:mm:ss");
		Date date2 = DateTimeUtils.parse("2011-10-12 22:22:22", "yyyy-MM-dd HH:mm:ss");
		System.out.println("相差：" + DateTimeUtils.getDiffYearsAndMonths(date1, date2));
		//System.out.println("相差：" + DateTimeUtils.getDiffDays(date1, date2));

		System.out.println(DateTimeUtils.format(date1, "yyyy-MM-dd HH:mm:ss"));

		System.out.println("获得指定日期的上月最后一天的日期"
				+ DateTimeUtils.format(DateTimeUtils.getPreviousMonthEnd(date1), "yyyy-MM-dd"));
		System.out.println("获得指定日期的上月第一天的日期"
				+ DateTimeUtils.format(DateTimeUtils.getPreviousMonthFirst(date1), "yyyy-MM-dd"));

		System.out.println("获得指定日期第一天的日期" + DateTimeUtils.format(DateTimeUtils.getMonthEnd(date1), "yyyy-MM-dd"));
		System.out.println("获得指定日期最后一天的日期" + DateTimeUtils.format(DateTimeUtils.getMonthFirst(date1), "yyyy-MM-dd"));

		System.out.println("获得指定日期上个月的日期" + DateTimeUtils.format(DateTimeUtils.getNextMonthDate(date1), "yyyy-MM-dd"));
		System.out.println("获得指定日期上个月的日期"
				+ DateTimeUtils.format(DateTimeUtils.getPreviousMonthDate(date1), "yyyy-MM-dd"));

		System.out.println("获得指定日期2年后的日期"
				+ DateTimeUtils.format(DateTimeUtils.getIntervalDate(date1, Calendar.YEAR, 2), "yyyy-MM-dd"));
		System.out.println("获得指定日期2年前的日期"
				+ DateTimeUtils.format(DateTimeUtils.getIntervalDate(date1, Calendar.YEAR, -2), "yyyy-MM-dd"));

		System.out.println("获得指定日期15月后的日期"
				+ DateTimeUtils.format(DateTimeUtils.getIntervalDate(date1, Calendar.MONTH, 15), "yyyy-MM-dd"));
		System.out.println("获得指定日期6月前的日期"
				+ DateTimeUtils.format(DateTimeUtils.getIntervalDate(date1, Calendar.MONTH, -6), "yyyy-MM-dd"));

		System.out.println("获得指定日期40天后的日期"
				+ DateTimeUtils.format(DateTimeUtils.getIntervalDate(date1, Calendar.DATE, 40), "yyyy-MM-dd"));
		System.out.println("获得指定日期10天前的日期"
				+ DateTimeUtils.format(DateTimeUtils.getIntervalDate(date1, Calendar.DATE, -10), "yyyy-MM-dd"));
	}

	public static Date now() {
		return new Date();
	}

	/**
	 * 将格式格式化成日期
	 * 
	 * @param value
	 * @param pattern
	 *            yyyy-MM-dd,yyyy-MM-dd hh:mm:ss
	 * @return
	 */
	public static Date parse(String value, String pattern) {
		if (value == null)
			return null;
		try {
			return new SimpleDateFormat(pattern).parse(value);
		} catch (ParseException e) {
			throw new RuntimeException("The symbol " + value + "with format " + pattern
					+ " can't be converted to a Date", e);
		}
	}

	private static boolean check(Date date1, Date date2) {
		if (date1 == null || date2 == null)
			return false;
		if (date1.after(date2))
			return false;
		return true;

	}

}

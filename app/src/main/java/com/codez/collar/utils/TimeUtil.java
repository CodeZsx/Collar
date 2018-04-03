package com.codez.collar.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 时间工具类
 * 
 * @author zsx
 * 
 */
@SuppressLint("SimpleDateFormat")
public class TimeUtil {

	public static String getWeiboTime(String str) {
		Date date = new Date();
		SimpleDateFormat sdf1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy",Locale.US);
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/MM/dd");
		try {
			date = sdf1.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sdf2.format(date);
	}
	public static String getStatusTime(String str) {
		Date date = new Date();
		SimpleDateFormat sdf1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy",Locale.US);
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
		try {
			date = sdf1.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sdf2.format(date);
	}
	public static String getMsgTime(String str) {
		Date date = new Date();
		SimpleDateFormat sdf1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy",Locale.US);

		SimpleDateFormat yMd = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat Md = new SimpleDateFormat("MM-dd HH:mm");
		SimpleDateFormat ms = new SimpleDateFormat("HH:mm");
		try {
			date = sdf1.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (isToday(date)) {
			return "今天 " + ms.format(date);
		} else if (isYear(date)) {
			switch (dayDiffer(date)) {
				case 1:
					return "昨天 " + ms.format(date);
				case 2:
					return "前天 " + ms.format(date);
				default:
					return Md.format(date);
			}
		}else{
			return yMd.format(date);
		}
	}

	public static boolean isToday(Date date) {
		Calendar cur = Calendar.getInstance();
		cur.setTime(new Date(System.currentTimeMillis()));
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_YEAR) == cur.get(Calendar.DAY_OF_YEAR);
	}

	public static int dayDiffer(Date date) {
		Calendar cur = Calendar.getInstance();
		cur.setTime(new Date(System.currentTimeMillis()));
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cur.get(Calendar.DAY_OF_YEAR) - cal.get(Calendar.DAY_OF_YEAR);
	}
	public static boolean isYear(Date date) {
		Calendar cur = Calendar.getInstance();
		cur.setTime(new Date(System.currentTimeMillis()));
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.YEAR) == cur.get(Calendar.YEAR);
	}

	public static boolean isSamePeriod(String last, String cur) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy",Locale.US);
		return Math.abs(sdf.parse(last).getTime() - sdf.parse(cur).getTime()) < 1000 * 60 * 10;
	}

	public static String getCurTimeToName(){
		Date cur = new Date(System.currentTimeMillis());
		SimpleDateFormat format = new SimpleDateFormat("yyMMdd_HHmmss");
		return format.format(cur);
	}
	public static String getTime(long time) {
		L.e("time : "+time);
		SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm");
		return format.format(new Date(time));
	}
	public static String getDay(long time) {
		L.e("time : "+time);
		SimpleDateFormat format = new SimpleDateFormat("MM-dd");
		return format.format(new Date(time));
	}
	public static String getTimeXie(long time) {
		SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd");
		L.e("time : "+time);
		return format.format(new Date(time));
	}
	public static String getTime(String str) {
		long time = Long.parseLong(str);
		SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm");
		return format.format(new Date(time));
	}
	public static String getDay(String str) {
		long time = Long.parseLong(str);
		SimpleDateFormat format = new SimpleDateFormat("MM-dd");
		return format.format(new Date(time));
	}

	/**
	 * 获取年/月/日
	 * @param str
	 * @return
	 */
	public static String getYMDXie(String str) {
		long time = Long.parseLong(str);
		SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd");
		return format.format(new Date(time));
	}

	public static String getHourAndMin(long time) {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		return format.format(new Date(time));
	}

	public static String getChatTime(long timesamp) {
		String result = "";
		SimpleDateFormat sdf = new SimpleDateFormat("dd");
		Date today = new Date(System.currentTimeMillis());
		Date otherDay = new Date(timesamp);
		int temp = Integer.parseInt(sdf.format(today))
				- Integer.parseInt(sdf.format(otherDay));

		switch (temp) {
		case 0:
			result = "今天 " + getHourAndMin(timesamp);
			break;
		case 1:
			result = "昨天 " + getHourAndMin(timesamp);
			break;
		case 2:
			result = "前天 " + getHourAndMin(timesamp);
			break;

		default:
			// result = temp + "天前 ";
			result = getDay(timesamp);
			break;
		}

		return result;
	}
}

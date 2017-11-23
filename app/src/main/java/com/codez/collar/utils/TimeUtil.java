package com.codez.collar.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
	public static String getNewsTime(String str){
		String result = "";
		SimpleDateFormat formatAll = new SimpleDateFormat("yy-MM-dd HH:mm");
		SimpleDateFormat formatDay = new SimpleDateFormat("dd");
		Date today = new Date(System.currentTimeMillis());
		Date otherDay = null;
		try {
			otherDay = new Date(String.valueOf(formatDay.parse(str)));
			otherDay = formatAll.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		int temp = Integer.parseInt(formatDay.format(today))
				- Integer.parseInt(formatDay.format(otherDay));
		switch (temp) {
			case 0:
				result = getHourAndMin(otherDay.getTime());
				break;
			case 1:
				result = "昨天 " + getHourAndMin(otherDay.getTime());
				break;
			case 2:
				result = "前天 " + getHourAndMin(otherDay.getTime());
				break;

			default:
				// result = temp + "天前 ";
				result = getDay(otherDay.getTime());
				break;
		}

		return result;
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

	public static String getTimeXie(String str) {
		long time = Long.parseLong(str);
		SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd");
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

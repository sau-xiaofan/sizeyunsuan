package edu.sau.sizeyunsuan.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * 说明：生成UUID(32位不重复的字符串)
 * 作者：FH Admin Q313596790
 * 官网：www.fhadmin.org
 */
public class UuidUtil {

	public static String get32UUID() {
		String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
		return uuid;
	}

	//生成随机字符串带时间32位
	public static String get32ID() {
		int length = 18;
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String idTime = dateFormat.format(date);
		String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		sb.append(idTime);
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(62);
			sb.append(str.charAt(number));
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		System.out.println(get32ID()+" "+get32ID().length());
	}
}

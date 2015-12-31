package cn.com.ubankers.www.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regular {
//	邮箱正则
	public static boolean checkEmail(String email) {
		boolean flag = false;
		try {
			String check = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(email);
			flag = matcher.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}
//身份证正则
	public static boolean checkid(String email) {
		boolean flag = false;
		try {
			String check = "([0-9]{17}([0-9]|X))|([0-9]{15})";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(email);
			flag = matcher.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}
	

}

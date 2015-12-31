package cn.com.ubankers.www.utils;

/**
 * 字符串与Unicode编码之间的转换
 * @author 刘超
 * @date   2015-7-17
 */
public class UnicodeUtil {
	
	/**
	 * 将Unicode码转字符串
	 * @param str
	 * @return
	 */
	public static String decode(String str){  
        char[]c=str.toCharArray();  
        String resultStr= "";  
        for(int i=0;i<c.length;i++)  
          resultStr += String.valueOf(c[i]);  
        return resultStr;  
    }  
	
	/**
	 * 将字符串转Unicode码
	 * @param str
	 * @return
	 */
    public static String encode(String str) {  
       String result = "";  
       for(int i = 0; i < str.length(); i++) {  
           String temp = "";  
           int strInt = str.charAt(i);  
           if(strInt > 127) {  
               temp += "\\u" + Integer.toHexString(strInt);  
           } else {  
               temp = String.valueOf(str.charAt(i));  
           }  
           result += temp;  
       }  
       return result;  
    }
    
}

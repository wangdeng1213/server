package com.linksus.common.util;

public abstract class WeiboUtil{

	private static String[] str62keys = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e",
			"f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
			"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
			"V", "W", "X", "Y", "Z" };

	// 10进制转62进制  新浪url专用
	public static String int10Tostr62(long int10){
		String str62 = "";
		while (int10 != 0){
			long l = int10 % 62;
			int s = new Long(l).intValue();
			str62 = str62keys[s] + str62;
			int10 = (long) Math.floor(int10 / 62);
		}
		return str62;
	}

	public static String Id2Mid(long int10){
		String str10 = int10 + "";
		String str = "";
		String mid = "";
		for(int i = str10.length() - 7; i > -7; i = i - 7) //从最后往前以7字节为一组读取字符  
		{
			int offset = i < 0 ? 0 : i;
			int offset2 = i + 7;
			long num = Long.parseLong(str10.substring(offset, offset2));
			str = int10Tostr62(num);
			if(str.length() == 3){
				str = "0" + str;
			}
			mid = str + mid;
		}
		return mid;
	}

	// 62进制转10进制  
	public static long str62To10(String str62){
		long i10 = 0;
		String[] array = strToArray(str62);
		for(int i = 0; i < array.length; i++){
			long n = array.length - i - 1;
			String s = array[i];
			i10 += getIndex(s) * Math.pow(62, n);
		}
		return i10;
	}

	// 将string变为数组  
	public static String[] strToArray(String str){
		String[] s = new String[str.length()];
		for(int i = 0; i < str.length(); i++){
			s[i] = str.substring(i, i + 1);
		}
		return s;
	}

	// 字母在str62keys中 出现的位置  
	public static int getIndex(String s){
		int t = 0;
		for(int i = 0; i < str62keys.length; i++){
			if(s.equals(str62keys[i])){
				t = i;
			}
		}
		return t;
	}

	// 通过ARGS获取mid  
	public static String getMidByUrlArgs(String url){
		String mid = "";
		//从最后往前以4字节为一组读取URL字符  
		for(int i = url.length() - 4; i > -4; i = i - 4){
			int offset1 = i < 0 ? 0 : i;
			int offset2 = i + 4;
			String str = url.substring(offset1, offset2);
			str = String.valueOf(str62To10(str));
			//若不是第一组，则不足7位补0  
			if(offset1 > 0){
				while (str.length() < 7){
					str = "0" + str;
				}
			}
			mid = str + mid;
		}
		return mid;
	}

	public static void main(String[] args){
		System.out.println(getMidByUrlArgs("B0lhKldQ9"));
		System.out.println(Id2Mid(3701850773244406l));
		System.out.println(getMidByUrlArgs("AzJ4c40l9"));
		System.out.println(Id2Mid(3685147200954623l));
	}
}

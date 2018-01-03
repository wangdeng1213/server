package com.linksus.test;

public class TestStringEquals {
 public static void main(String[] args) {
	String a="It is dog";
	String b="It is dog";
	String c=new String("It is dog");
	String d=new String("It is dog");
	System.out.println(a==b);
	System.out.println(a==c);
	System.out.println(d==c);
	System.out.println(a.equals(b));
	System.out.println(a.equals(c));
	System.out.println(c.equals(d));
}
}

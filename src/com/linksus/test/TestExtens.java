package com.linksus.test;

public class TestExtens extends A{
    public TestExtens(){
    	System.out.println("B");
    }
    public static void main(String[] args) {
		TestExtens test = new TestExtens();
	}
}
class A{
	  public A(){
		  System.out.println("A");
	  }
}
package com.linksus.test;

public class TestSuper extends Super{
  //float getNum(){return 4.0f};
	//public void getNum(){};
	private void getNum(double d){};
	public double getnum(){return 4.0d;};
	private int ii =1;
	//public float getNum(){return 9};
	public static void main(String[] args) {
		TestSuper test = new TestSuper();
		//Class c= new Class();
		Class c=test.getClass();
		System.out.println(c);
		Class c1 =TestSuper.class;
		System.out.println(c1);
		try {
			Class c2 = Class.forName("com.Test");
			System.out.println(c2);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Class c3 = Class.forName("Test");
	}
}
class Super{
	protected float getNum(){return 3.0f;};
}
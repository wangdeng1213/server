package com.linksus.test;

import java.io.FileOutputStream;
import java.io.PrintWriter;

public  class TestOut {
  static int i;
  public int test(){
	  i++;
	  return i;
  }
  public int count(){
	  return 1%9;
  }
  public static String output="";
  public static void foo(int i){
	  try{
		  if(i==1){
			  throw new Exception();
			  
		  }
		  output+="1";
	  }catch(Exception e){
		  output+="2";
		  return;
	  }finally{
		  output+="3";
	  }
	  output+="4";
  }
  public static void main(String[] args) {
/*	TestOut test=new TestOut();
	test.test();
	System.out.println(test.test());
	//System.out.println(count());
	foo(0);
	foo(1);
	System.out.println(output);*/
	  try{
		  PrintWriter out = new PrintWriter(new FileOutputStream("d://222.txt",true));
		  String name="chen";
		  out.print(name);
		  //out.flush();
	  }catch(Exception e){
		  System.out.println("文件未发现");
	  }
}
}

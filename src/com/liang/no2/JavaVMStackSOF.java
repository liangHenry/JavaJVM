package com.liang.no2;

public class JavaVMStackSOF {
	private int stackLengh=1;
	public void stackLeak(){
		stackLengh++;
		stackLeak();
	}
	public static void main(String[] args) {
		JavaVMStackSOF oom=new JavaVMStackSOF();
		oom.stackLeak();
	}
}

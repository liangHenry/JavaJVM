package com.liang.no2;

import java.util.ArrayList;
import java.util.List;

public class RuntimeConstantPoolOOM {
	public static void main(String[] args) {
		List<String> list=new ArrayList<String>();
		int i=0;
		while(true){
			list.add(String.valueOf(i++).intern());
		}
		
		
		/*String str2=new StringBuilder().append("aa").append("bb").toString();
		System.out.println(str2.intern()==str2);
		
		String str1=new StringBuilder().append("¼ÆËã»ú").append("Èí¼þ").toString();
		System.out.println(str1.intern()==str1);*/
		
		
	}
}

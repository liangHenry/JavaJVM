package test;

import java.lang.reflect.Method;

public class HelloMain {

	public static void main(String[] args) throws Exception {
		while (true) {
			//DynamicLoader loader = new DynamicLoader("F:/zl/help/");
			DynamicLoader loader = new DynamicLoader("F:/zl/workspace-java/workspace02/JavaJVM/bin/");
			Class clazz = loader.loadClass("test.Worker");
			Object instance = clazz.newInstance();
			Method doit = clazz.getDeclaredMethod("doit", null);
			doit.invoke(instance, null);
			
			if(instance instanceof Worker){
				((Worker) instance).doit();
			}
			Thread.sleep(10000);
		}
	}

}
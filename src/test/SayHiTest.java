package test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

public class SayHiTest {
	public static void main(String[] args) throws FileNotFoundException, IOException {
		new Timer().schedule(new TimerTask() {

			@Override
			public void run() {
				try {
					// 每次都创建出一个新的类加载器
					System.out.println("-----------------");
					HotSwapClassLoader hotSwapClassLoader = new HotSwapClassLoader(
							"F:/zl/workspace-java/workspace02/JavaJVM/bin/", new String[] { "test.ISayHi","test.SayHi" });
					Class<?> cls = hotSwapClassLoader.loadClass("test.SayHi");
					Object foo = cls.newInstance();
					
					System.out.println(foo.getClass());
					
					if (foo instanceof ISayHi)
						((ISayHi) foo).sayHi();
					//Method doit = cls.getDeclaredMethod("sayHi", null);
					//doit.invoke(foo, null);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}, 0, 5000L);
	}
}

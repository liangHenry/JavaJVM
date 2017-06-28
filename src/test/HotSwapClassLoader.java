package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;

/*
 * 实验步骤：
 * 1.在项目下增加一个classDirectory目录，里面盛放SayHi.class。
 * 2.运行main方法。
 * 3.替换SayHi.class。观察控制台输出的变化即可。
 */
class HotSwapClassLoader extends ClassLoader {

	// 需要该类加载器直接加载的类文件的基目录
	private String basedir;
	// 需要由该类加载器直接加载的类名
	private HashSet<String> dynamicClassNameSet;

	public HotSwapClassLoader(String basedir, String[] classNames) throws FileNotFoundException, IOException {
		super(null); // 指定父类加载器为 null
		this.basedir = basedir;
		dynamicClassNameSet = new HashSet<String>();
		loadClassByMe(classNames);
	}

	private void loadClassByMe(String[] classNames) throws FileNotFoundException, IOException {
		for (int i = 0; i < classNames.length; i++) {
			loadDirectly(classNames[i]);
			dynamicClassNameSet.add(classNames[i]);
		}
	}

	private Class<?> loadDirectly(String name) throws FileNotFoundException, IOException {
		Class<?> cls = null;
		StringBuffer sb = new StringBuffer(basedir);
		String classname = name.replace('.', '/') + ".class";
		sb.append(classname);
		File classF = new File(sb.toString());
		cls = instantiateClass(name, new FileInputStream(classF), classF.length());
		return cls;
	}

	private Class<?> instantiateClass(String name, InputStream fin, long len) throws IOException {
		byte[] raw = new byte[(int) len];
		fin.read(raw);
		fin.close();
		return defineClass(name, raw, 0, raw.length);
	}

	protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
		Class<?> cls = null;
		cls = findLoadedClass(name);
		if (!this.dynamicClassNameSet.contains(name) && cls == null)
			cls = getSystemClassLoader().loadClass(name);
		if (cls == null)
			throw new ClassNotFoundException(name);
		if (resolve)
			resolveClass(cls);
		return cls;
	}

}
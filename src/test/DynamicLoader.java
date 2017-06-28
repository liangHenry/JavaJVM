package test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;

public class DynamicLoader extends ClassLoader {
	private String baseDir;

	public DynamicLoader(String baseDir) {
		super();
		this.baseDir = baseDir;
	}

	private String getClassFile(String className) {
		return baseDir + className.replace(".", "/") + ".class";
	}

	protected Class<?> findClass(String name) throws ClassNotFoundException {
		Class clazz = this.findLoadedClass(name);
		if (clazz == null) {
			try {
				String classFile = getClassFile(name);
				FileInputStream fis = new FileInputStream(classFile);
				FileChannel filec = fis.getChannel();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				WritableByteChannel outc = Channels.newChannel(baos);
				ByteBuffer buffer = ByteBuffer.allocateDirect(1024);

				int count = 0;

				while ((count = filec.read(buffer)) > 0) {
					buffer.flip();
					outc.write(buffer);
					buffer.clear();
				}
				fis.close();
				byte[] bytes = baos.toByteArray();
				clazz = defineClass(name, bytes, 0, bytes.length);

			} catch (FileNotFoundException e) {
				System.out.println("FileNotFoundException:"+e.getMessage());
			} catch (IOException e) {
				System.out.println("IOException");
			}

		}

		return clazz;
	}

	@Override
	protected Class<?> loadClass(String name, boolean flag) throws ClassNotFoundException {
		Class re = findClass(name);
		if (re == null) {
			return super.loadClass(name, flag);
		}
		return re;
	}

	public Class<?> findClass2(String name) throws ClassNotFoundException {
		String classPath = DynamicLoader.class.getResource("/").getPath();
		String fileName = name.replace(".", "/") + ".class";
		File classFile = new File(classPath, fileName);
		if (!classFile.exists()) {
			throw new ClassNotFoundException(classFile.getPath() + " ²»´æÔÚ");
		} else {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ByteBuffer bf = ByteBuffer.allocate(1024);
			FileInputStream fis = null;
			FileChannel fc = null;

			try {
				fis = new FileInputStream(classFile);
				fc = fis.getChannel();

				while (fc.read(bf) > 0) {
					bf.flip();
					bos.write(bf.array(), 0, bf.limit());
					bf.clear();
				}
			} catch (FileNotFoundException var20) {
				var20.printStackTrace();
			} catch (IOException var21) {
				var21.printStackTrace();
			} finally {
				try {
					fis.close();
					fc.close();
				} catch (IOException var19) {
					var19.printStackTrace();
				}

			}

			return this.defineClass(bos.toByteArray(), 0, bos.toByteArray().length);
		}
	}
}
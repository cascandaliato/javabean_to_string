package javabean_to_string;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javabean_to_string.compiler.CharSequenceJavaFileObject;
import javabean_to_string.compiler.ClassFileManager;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;

@SuppressWarnings("rawtypes")
public class BeanToString {

	private final JavaCompiler compiler;

	private final JavaFileManager fileManager;

	private final ToStringCodeGenerator toStringCodeGenerator;

	private final ConcurrentHashMap<Class<?>, IBeanToString<?>> generators = new ConcurrentHashMap<Class<?>, IBeanToString<?>>();

	public BeanToString() {
		compiler = ToolProvider.getSystemJavaCompiler();
		fileManager = new ClassFileManager(compiler.getStandardFileManager(
				null, null, null));
		toStringCodeGenerator = new ToStringCodeGenerator();
	}

	@SuppressWarnings({ "unchecked" })
	public String toString(Object bean) throws RuntimeException {
		Class<?> beanClazz = bean.getClass();
		System.out.println("Processing bean " + beanClazz.getSimpleName());

		IBeanToString generator = getGeneratorForClass(beanClazz);

		return generator.toString(bean);
	}

	private IBeanToString getGeneratorForClass(Class<?> beanClazz)
			throws RuntimeException {

		IBeanToString generator = generators.get(beanClazz);
		if (generator == null) {
			synchronized (beanClazz) {
				generator = generators.get(beanClazz);
				if (generator == null) {
					generator = createToStringObj(beanClazz);
					generators.putIfAbsent(beanClazz, generator);
				}
			}
		}
		return generator;
	}

	private IBeanToString createToStringObj(Class<?> beanClazz)
			throws RuntimeException {
		System.out.println("Creating *NEW* BeanToString implementation for "
				+ beanClazz.getName());

		String toStringClassName = getToStringClassName(beanClazz);
		String toStringClassPackage = this.getClass().getPackage().getName();
		String fullName = toStringClassPackage + "." + toStringClassName;

		String source;
		try {
			source = toStringCodeGenerator.generateCode(beanClazz,
					toStringClassPackage, toStringClassName);
		} catch (IllegalAccessException | InvocationTargetException
				| NoSuchMethodException e) {
			throw new RuntimeException("Can not generate toString builder", e);
		}

		compileClass(toStringClassName, source);

		IBeanToString beanToString;
		try {
			beanToString = getInstance(fullName);
		} catch (IllegalAccessException | InstantiationException
				| ClassNotFoundException e) {
			throw new RuntimeException("Can not generate toString builder", e);
		}

		return beanToString;
	}

	private void compileClass(String name, String source) {
		System.out.println("Compiling class " + name);

		// Dynamic compiling requires specifying
		// a list of "files" to compile. In our case
		// this is a list containing one "file" which is in our case
		// our own implementation (see details below)
		final List<JavaFileObject> jfiles = new ArrayList<JavaFileObject>();
		jfiles.add(new CharSequenceJavaFileObject(name, source));

		// We specify a task to the compiler. Compiler should use our file
		// manager and our list of "files".
		// Then we run the compilation with call()
		compiler.getTask(null, fileManager, null, null, null, jfiles).call();
	}

	private IBeanToString getInstance(String toStringClassFullName)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		System.out
				.println("Getting instance of class " + toStringClassFullName);
		return (IBeanToString) fileManager.getClassLoader(null)
				.loadClass(toStringClassFullName).newInstance();
	}

	private String getToStringClassName(Class<?> beanClazz) {
		return getToStringClassName(beanClazz.getSimpleName());
	}

	private String getToStringClassName(String beanClassName) {
		return beanClassName + "StringBuilder";// _" + new BigInteger(130, new
												// SecureRandom()).toString(32);
	}

}

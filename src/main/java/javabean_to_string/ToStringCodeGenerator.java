package javabean_to_string;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.tuple.Pair;

public class ToStringCodeGenerator {

	private final String[] IGNORED_GETTERS = new String[] { "getClass" };

	private final String NEW_LINE = System.getProperty("line.separator");

	public String generateCode(Class<?> beanClazz, String toStringClassPackage,
			String toStringClassName) throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		Set<String> ignoredGetters = new HashSet<String>(
				Arrays.asList(IGNORED_GETTERS));
		String beanClazzName = beanClazz.getSimpleName();

		Set<Pair<String, Method>> gettersMap = new HashSet<Pair<String, Method>>();
		PropertyDescriptor[] propertyDescriptors = PropertyUtils
				.getPropertyDescriptors(beanClazz);
		for (final PropertyDescriptor propertyDescriptor : propertyDescriptors) {
			final Method readMethod = PropertyUtils
					.getReadMethod(propertyDescriptor);
			if ((readMethod == null)
					|| ignoredGetters.contains(readMethod.getName())) {
				continue;
			}

			String propertyName = propertyDescriptor.getDisplayName();
			Method getter = PropertyUtils.getReadMethod(propertyDescriptor);
			gettersMap.add(Pair.of(propertyName, getter));
		}

		// @formatter:off
        final StringBuilder sb = new StringBuilder();
        sb.append("package " + toStringClassPackage + ";").append(NEW_LINE);
        sb.append(NEW_LINE);        
        sb.append("import " + BeanToString.class.getName() + ";").append(NEW_LINE);
        sb.append("import " + beanClazz.getName() + ";").append(NEW_LINE);
        sb.append(NEW_LINE);
        sb.append("public final class " + toStringClassName + " implements " + IBeanToString.class.getSimpleName() + "<" + beanClazzName + "> {").append(NEW_LINE);
        sb.append(NEW_LINE);
        sb.append("    @Override").append(NEW_LINE);
        sb.append("    public String toString(" + beanClazzName + " bean) {").append(NEW_LINE);
        sb.append("        StringBuilder builder = new StringBuilder();").append(NEW_LINE);
        sb.append("        builder.append(\"ClassName {" + beanClazzName + "\");").append(NEW_LINE);
        for (Pair<String, Method> getter : gettersMap) {
        sb.append("        builder.append(\"} " +  getter.getLeft() + " {\");").append(NEW_LINE);
        sb.append("        builder.append(bean." +  getter.getRight().getName() + "());").append(NEW_LINE);
        }
        sb.append("        builder.append(\"}\");").append(NEW_LINE);
        sb.append("        return builder.toString();").append(NEW_LINE);
        sb.append("    }").append(NEW_LINE);
        sb.append(NEW_LINE);
        sb.append("}");
        sb.append(NEW_LINE);
		// @formatter:on

		sb.trimToSize();
		// System.out.println(sb.toString());
		return sb.toString();
	}

}

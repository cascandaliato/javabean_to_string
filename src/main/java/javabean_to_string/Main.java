package javabean_to_string;

public class Main {

	public static void main(final String[] args) {
		try {
			MyBean bean = new MyBean("fooFieldo");

			BeanToString beanToStringBuilder = new BeanToString();
			System.out.println("TO_STRING_1 = "
					+ beanToStringBuilder.toString(bean));
			System.out.println("TO_STRING_2 = "
					+ beanToStringBuilder.toString(bean));
			System.out.println("TO_STRING_3 = "
					+ beanToStringBuilder.toString(bean));
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

}

package javabean_to_string;

public class SampleBeanToString implements IBeanToString<Object> {

	@Override
	public String toString(final Object bean) {
		return new MyBean("test").getParento().toString();
	}

}

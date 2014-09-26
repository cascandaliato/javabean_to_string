package javabean_to_string;

import javabean_to_string.SampleBeanToString;

import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class SampleBeanToStringTest {

	public void testToString() {
		Assert.assertEquals(new SampleBeanToString().toString(null), "123");
	}

}

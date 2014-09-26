package javabean_to_string;

import java.math.BigDecimal;

public class MyBean extends MyParentBean<BigDecimal> {

	public MyBean(final String fieldo) {
		this.fieldo = fieldo;
	}

	private String fieldo;

	public String getFieldo() {
		return fieldo;
	}

	public void setFieldo(final String fieldo) {
		this.fieldo = fieldo;
	}

	public String getOtherField() {
		return "otherFieldValue";
	}

    @Override
    public String toString() {
        return "field=" + fieldo;
    }

}

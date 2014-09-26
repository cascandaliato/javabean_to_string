package javabean_to_string;

public class MyParentBean<T> {

	private Integer parento;

	public Integer getParento() {
		return parento == null ? 123 : parento;
	}

	public void setParento(Integer parento) {
		this.parento = parento;
	}

	public T getCoso() {
		return null;
	}
}

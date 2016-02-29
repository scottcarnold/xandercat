package org.xandercat.ofe.stat;

import java.io.Serializable;

public interface DataPoint<T> extends Serializable {

	public String getLabel();

	public T getValue();
}

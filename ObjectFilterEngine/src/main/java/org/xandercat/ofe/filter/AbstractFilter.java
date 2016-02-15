package org.xandercat.ofe.filter;

import java.text.NumberFormat;

/**
 * Abstract filter that takes care of handling the common required, excluded, and 
 * weight attributes.
 * 
 * @author Scott Arnold
 *
 * @param <T> type of object being filtered
 */
public abstract class AbstractFilter<T> implements AttributeFilter<T> {

	private boolean required;
	private boolean excluded;
	private FilterWeight weight = FilterWeight.MEDIUM;
	
	public AbstractFilter() {
	}
	
	public void setRequired(boolean required) {
		this.required = required;
	}

	public void setExcluded(boolean excluded) {
		this.excluded = excluded;
	}
	
	@Override
	public boolean isRequired() {
		return required;
	}

	@Override
	public boolean isExcluded() {
		return excluded;
	}

	@Override
	public FilterWeight getWeight() {
		return weight;
	}
	
	public void setWeight(FilterWeight weight) {
		this.weight = weight;
	}
	
	public AbstractFilter<T> required() {
		this.required = true;
		return this;
	}
	
	public AbstractFilter<T> excluded() {
		this.excluded = true;
		return this;
	}
	
	public AbstractFilter<T> weight(FilterWeight weight) {
		this.weight = weight;
		return this;
	}
	
	public AbstractFilter<T> weight(float weight) {
		this.weight = new FilterWeight(weight);
		return this;
	}
	
	@Override
	public String getDescription() {
		StringBuilder sb = new StringBuilder();
		sb.append(getMatchDescription());
		if (isExcluded()) {
			sb.append(" [excluded]");
		} else {
			if (isRequired()) {
				sb.append(" [required]");
			}
			sb.append(" [weight of ");
			sb.append(NumberFormat.getNumberInstance().format(getWeight().getWeight()));
			sb.append("]");
		}
		return sb.toString();
	}

	public abstract String getMatchDescription();
}

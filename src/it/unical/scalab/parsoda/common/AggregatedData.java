package it.unical.scalab.parsoda.common;

import java.util.LinkedList;

import org.apache.commons.lang.StringUtils;

public class AggregatedData extends LinkedList<String> {
	
	private String joinSeparator = ",";
	
	public AggregatedData() {
		super();
	}
	
	public AggregatedData(String joinSeparator) {
		super();
		this.joinSeparator = joinSeparator;
	}

	@Override
	public String toString() {
		return StringUtils.join(this, joinSeparator);
	}

	public String getJoinSeparator() {
		return joinSeparator;
	}

	public void setJoinSeparator(String joinSeparator) {
		this.joinSeparator = joinSeparator;
	}
	
	

}

package it.unical.scalab.parsoda.common;

import java.util.Comparator;

public class IntegerComparator implements Comparator<Integer> {

	@Override
	public int compare(Integer o1, Integer o2) {
		if (o1.intValue() == o2.intValue())
			return 0;
		else if (o1.intValue() < o2.intValue())
			return 1;
		else
			return -1;
	}
}

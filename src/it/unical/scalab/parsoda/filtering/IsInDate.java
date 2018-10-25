package it.unical.scalab.parsoda.filtering;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import it.unical.scalab.parsoda.common.AbstractFilterFunction;
import it.unical.scalab.parsoda.common.model.AbstractGeotaggedItem;

public class IsInDate extends AbstractFilterFunction {
	DateTime start;
	DateTime end;
	Interval interval;
	
	public IsInDate() {
		super();
	}

	public IsInDate(String params) {
		super(params);
		this.start = DateTime.parse(this.options.getOption("start"));
		this.end = DateTime.parse(this.options.getOption("end"));
		this.interval = new Interval(start, end);
	}

	@Override
	public boolean test(AbstractGeotaggedItem g) {
		if (g != null && g.getDateTime() != null && interval.contains(g.getDateTime().toDateTime()))
			return true;
		return false;
	}

}

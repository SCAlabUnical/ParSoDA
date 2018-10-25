package it.unical.scalab.parsoda.acquisition.util;

import java.util.Date;

import twitter4j.GeoLocation;

public class TWPosition {
	
	private GeoLocation location;
	private Date date;
	
	public TWPosition() {
	}
	public TWPosition(GeoLocation location, Date date) {
		super();
		this.location = location;
		this.date = date;
	}
	public GeoLocation getLocation() {
		return location;
	}
	public void setLocation(GeoLocation location) {
		this.location = location;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	
	
	

}

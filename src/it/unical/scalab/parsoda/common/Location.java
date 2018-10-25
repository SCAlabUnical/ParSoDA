package it.unical.scalab.parsoda.common;

import com.vividsolutions.jts.geom.Coordinate;

public class Location {

	private double latitude;
	private double longitude;

	public Location(double latidude, double longitude) {
		this.latitude = latidude;
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}
	
	public Coordinate getCoordinate() {
		return new Coordinate(longitude,latitude);
	}

}

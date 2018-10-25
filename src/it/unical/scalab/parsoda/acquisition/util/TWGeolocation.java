package it.unical.scalab.parsoda.acquisition.util;

import twitter4j.GeoLocation;

public class TWGeolocation {

	private double latitude;
	private double longitude;
	
	public TWGeolocation(GeoLocation geo){
		if(geo!=null){
			this.latitude = geo.getLatitude();
			this.longitude = geo.getLongitude();
		}
	}
	
	public static TWGeolocation[][] getTWGeolocationMatrix(GeoLocation[][] geo){
		TWGeolocation[][] res=null;
		if(geo!= null){
		res = new TWGeolocation[geo.length][geo[0].length];
		for (int i = 0; i < geo.length; i++) {
			for (int j = 0; j < geo[i].length; j++) {
				res[i][j]=new TWGeolocation(geo[i][j]);
			}
		}
		}
		return res;
		
	}
	
	public TWGeolocation() {
		super();
	}
	public TWGeolocation(double latitude, double longitude) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	
}

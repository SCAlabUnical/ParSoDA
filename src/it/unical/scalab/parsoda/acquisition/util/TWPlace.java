package it.unical.scalab.parsoda.acquisition.util;

public class TWPlace {
	private String name, streetAddress, countryCode, id, country, placeType, fullName;
	private TWGeolocation[][] boundingBox, coordinates;
	
	
	public TWPlace() {
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getStreetAddress() {
		return streetAddress;
	}


	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}


	public String getCountryCode() {
		return countryCode;
	}


	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getCountry() {
		return country;
	}


	public void setCountry(String country) {
		this.country = country;
	}


	public String getPlaceType() {
		return placeType;
	}


	public void setPlaceType(String placeType) {
		this.placeType = placeType;
	}


	public String getFullName() {
		return fullName;
	}


	public void setFullName(String fullName) {
		this.fullName = fullName;
	}


	public TWGeolocation[][] getBoundingBox() {
		return boundingBox;
	}


	public void setBoundingBox(TWGeolocation[][] boundingBox) {
		this.boundingBox = boundingBox;
	}


	public TWGeolocation[][] getCoordinates() {
		return coordinates;
	}


	public void setCoordinates(TWGeolocation[][] coordinates) {
		this.coordinates = coordinates;
	}



//	
	
	
	

}

package it.unical.scalab.parsoda.acquisition.util;

import twitter4j.User;

public class TWUser {

	private long id;
	private String name;
	private String screenName;
	private String location;
	private String lang;
	private String description;
	private int followersCount;
//	private List<TWPosition> geoHistory;
	
	public TWUser() {
		
	}

	public TWUser(long id, String name, String screenName, String location, String lang, String description, TWGeolocation geo) {
		super();
		this.id = id;
		this.name = name;
		this.screenName = screenName;
		this.location = location;
		this.lang = lang;
		this.description = description;
	}
	
	public TWUser(User user){
		this.id=user.getId();
		this.name=user.getName()==null?"":user.getName().trim().replace("\n", " ").replace("\r", " ").replace("\t", " ");
		this.screenName=user.getScreenName();
		this.location=user.getLocation()==null?"":user.getLocation().trim().replace("\n", " ").replace("\r", " ").replace("\t", " ");
		this.description = (user.getDescription()==null)?"":user.getDescription().trim().replace("\n", " ").replace("\r", " ").replace("\t", " ");
		this.lang = user.getLang();
		this.followersCount = user.getFollowersCount();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getFollowersCount() {
		return followersCount;
	}

	public void setFollowersCount(int followersCount) {
		this.followersCount = followersCount;
	}
	

//	public List<TWPosition> getGeoHistory() {
//		return geoHistory;
//	}
//
//	public void setGeoHistory(List<TWPosition> geoHistory) {
//		this.geoHistory = geoHistory;
//	}
	
	
	
	
	
}

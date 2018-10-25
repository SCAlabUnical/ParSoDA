package it.unical.scalab.parsoda.acquisition.util;

import java.util.Date;

import twitter4j.HashtagEntity;
import twitter4j.Place;
import twitter4j.Status;

public class TWTweet {
	
	private long id;
	private String text;
	private Date date;
	private TWUser user;
	private TWGeolocation location;
	private boolean isRetweet;
	private boolean isFavorited;
	private int retweets;
	private int favoutites;
	private long inReplyToStatusId;
	private String inReplyToScreenName;
	private long inReplyToUserId;
	private String[] hashtags;
	private String lang;
	private TWPlace place;
	private String longUrl;

	
	public TWTweet() {
	
	}
	
	public TWTweet(TWTweet copia) {
		super();
		this.id = copia.getId();
		this.text = copia.getText();
		this.date = copia.getDate();
		this.user = copia.getUser();
		this.location = copia.getLocation();
		this.isRetweet = copia.isRetweet();
		this.retweets = copia.getRetweets();
		this.favoutites = copia.getFavoutites();
		this.inReplyToStatusId = copia.getInReplyToStatusId();
		this.inReplyToScreenName = copia.getInReplyToScreenName();
		this.inReplyToUserId = copia.getInReplyToUserId();
		this.hashtags = copia.getHashtags();
		this.lang = copia.getLang();
		this.place = copia.getPlace();
	}
	
	public TWTweet(long id, String text, Date date, TWUser user,
			TWGeolocation location, boolean isRetweet, int retweets,
			int favoutites, long inReplyToStatusId, String inReplyToScreenName,
			long inReplyToUserId, String[] hashtags, TWPlace place, String lang) {
		super();
		this.id = id;
		this.text = text;
		this.date = date;
		this.user = user;
		this.location = location;
		this.isRetweet = isRetweet;
		this.retweets = retweets;
		this.favoutites = favoutites;
		this.inReplyToStatusId = inReplyToStatusId;
		this.inReplyToScreenName = inReplyToScreenName;
		this.inReplyToUserId = inReplyToUserId;
		this.hashtags = hashtags;
		this.lang = lang;
		this.place = place;
	}


	public TWTweet(Status tweet){
		this.id = tweet.getId();
		this.text = tweet.getText()==null?"":tweet.getText().trim().replace("\n", " ").replace("\r", " ").replace("\t", " ");
		this.date = tweet.getCreatedAt();
		this.location = new TWGeolocation(tweet.getGeoLocation());
		this.isRetweet = tweet.isRetweet();
		this.setFavorited(tweet.isFavorited());
		this.user= new TWUser(tweet.getUser());
		this.retweets = tweet.getRetweetCount();
		this.favoutites = tweet.getFavoriteCount();
		this.inReplyToStatusId = tweet.getInReplyToStatusId();
		this.inReplyToScreenName = tweet.getInReplyToScreenName();
		this.inReplyToUserId = tweet.getInReplyToUserId();
		this.hashtags = getHashtag(tweet.getHashtagEntities());
		this.lang = tweet.getLang();
		this.place = getPlace(tweet.getPlace());
	}

	private TWPlace getPlace(Place place) {
		TWPlace res = new TWPlace();
		
		if(place!= null){
			res.setId(place.getId());
			res.setName(place.getName());
			res.setFullName(place.getFullName());
			res.setStreetAddress(place.getStreetAddress());
			res.setCoordinates(TWGeolocation.getTWGeolocationMatrix(place.getGeometryCoordinates()));
			res.setBoundingBox(TWGeolocation.getTWGeolocationMatrix(place.getBoundingBoxCoordinates()));
			res.setCountry(place.getCountry());
			res.setCountryCode(place.getCountryCode());
			res.setPlaceType(place.getPlaceType());
		}
		
		
		return res;
	}

	private String[] getHashtag(HashtagEntity[] hashtagEntities) {
		String [] res = new String[hashtagEntities.length];
		for (int i = 0; i < res.length; i++) {
			res[i]=hashtagEntities[i].getText();
		}
		return res;
	}
	
	private String printHashtag(){
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < hashtags.length; i++) {
			sb.append(hashtags[i]);
			if(i!=hashtags.length-1){
				sb.append(",");
			}
		}
		return sb.toString();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public TWUser getUser() {
		return user;
	}

	public void setUser(TWUser user) {
		this.user = user;
	}

	public TWGeolocation getLocation() {
		return location;
	}

	public void setLocation(TWGeolocation location) {
		this.location = location;
	}

	public boolean isRetweet() {
		return isRetweet;
	}

	public void setRetweet(boolean retweet) {
		this.isRetweet = retweet;
	}

	public int getRetweets() {
		return retweets;
	}

	public void setRetweets(int retweets) {
		this.retweets = retweets;
	}

	public int getFavoutites() {
		return favoutites;
	}

	public void setFavoutites(int favoutites) {
		this.favoutites = favoutites;
	}

	public long getInReplyToStatusId() {
		return inReplyToStatusId;
	}

	public void setInReplyToStatusId(long inReplyToStatusId) {
		this.inReplyToStatusId = inReplyToStatusId;
	}

	public String getInReplyToScreenName() {
		return inReplyToScreenName;
	}

	public void setInReplyToScreenName(String inReplyToScreenName) {
		this.inReplyToScreenName = inReplyToScreenName;
	}

	public long getInReplyToUserId() {
		return inReplyToUserId;
	}

	public void setInReplyToUserId(long inReplyToUserId) {
		this.inReplyToUserId = inReplyToUserId;
	}

	public String[] getHashtags() {
		return hashtags;
	}

	public void setHashtags(String[] hashtags) {
		this.hashtags = hashtags;
	}
	
	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public TWPlace getPlace() {
		return place;
	}

	public void setPlace(TWPlace place) {
		this.place = place;
	}

	public boolean isFavorited() {
		return isFavorited;
	}

	public void setFavorited(boolean isFavorited) {
		this.isFavorited = isFavorited;
	}
	
	
	
	

}

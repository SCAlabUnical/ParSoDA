package it.unical.scalab.parsoda.common.model;

import java.util.Locale;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONObject;

import com.spatial4j.core.shape.Point;

import it.unical.scalab.parsoda.common.Metadata;
import it.unical.scalab.parsoda.common.User;

public class Facebook extends AbstractGeotaggedItem {

	@SuppressWarnings("unused")
	private final static String SOURCE = "Facebook";
	private final static DateTimeFormatter dateStringFormat = DateTimeFormat.forPattern("MMM dd, yyyy h:mm:ss a")
			.withLocale(Locale.US);
	
	public Facebook(String s) {
		super(new JSONObject(s)); 
	}
	
	public Facebook(JSONObject jsonObject) {
		super(jsonObject);
	}

	@Override
	public User extractUserInfo(JSONObject jsonObj) {
		try {
			User user = new User(jsonObj.getJSONObject("author").getString("id"));
			user.put(Metadata.USERNAME, jsonObj.getJSONObject("author").getString("username"));
			return user;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Point extractLocationInfo(JSONObject jsonObj) {
		return null;
	}

	@Override
	public String extractId(JSONObject json) {
		try {
			return json.getString("id");
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public LocalDateTime extractDateTimeInfo(JSONObject jsonObj) {
		try {
			return dateStringFormat.parseDateTime(jsonObj.getString("createdAt")).toLocalDateTime();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public boolean check(JSONObject obj) {
		if (obj.has("likesCount") && obj.has("lovesCount") && obj.has("wowsCount"))
			return true;
		else
			return false;
	}

}

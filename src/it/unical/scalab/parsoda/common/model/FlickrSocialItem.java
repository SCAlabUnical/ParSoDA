package it.unical.scalab.parsoda.common.model;

import java.util.Locale;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONObject;

import com.spatial4j.core.context.SpatialContext;
import com.spatial4j.core.shape.Point;
import com.spatial4j.core.shape.impl.PointImpl;

import it.unical.scalab.parsoda.common.Metadata;
import it.unical.scalab.parsoda.common.User;

public class FlickrSocialItem extends SocialItem {

	@SuppressWarnings("unused")
	private final static String SOURCE = "Flickr";
	private final static DateTimeFormatter dateStringFormat = DateTimeFormat.forPattern("MMM dd, yyyy h:mm:ss a")
			.withLocale(Locale.US);
	
	public FlickrSocialItem(String s) {
		super(new JSONObject(s)); 
	}
	
	public FlickrSocialItem(JSONObject jsonObject) {
		super(jsonObject);
	}

	@Override
	public User extractUserInfo(JSONObject jsonObj) {
		try {
			User user = new User(jsonObj.getJSONObject("owner").getString("id"));
			user.put(Metadata.USERNAME, jsonObj.getJSONObject("owner").getString("username"));
			return user;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Point extractLocationInfo(JSONObject jsonObj) {
		try {
			if (jsonObj.has("geoData")) {
				JSONObject geo = jsonObj.getJSONObject("geoData");
				return new PointImpl(geo.getDouble("longitude"), geo.getDouble("latitude"), SpatialContext.GEO);
			} else {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
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
			return dateStringFormat.parseDateTime(jsonObj.getString("dateTaken")).toLocalDateTime();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public boolean check(JSONObject obj) {
		if (obj.has("url") && obj.getString("url").contains("flickr"))
			return true;
		else
			return false;
	}

}

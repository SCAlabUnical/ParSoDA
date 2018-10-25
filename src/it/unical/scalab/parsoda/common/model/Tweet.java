package it.unical.scalab.parsoda.common.model;

import java.util.Locale;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.json.JSONObject;

import com.spatial4j.core.context.SpatialContext;
import com.spatial4j.core.shape.Point;
import com.spatial4j.core.shape.impl.PointImpl;

import it.unical.scalab.parsoda.common.User;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterObjectFactory;

public class Tweet extends AbstractGeotaggedItem {

	private final static String SOURCE = "Twitter";
	private final static DateTimeFormatter dateStringFormat = DateTimeFormat.forPattern("MMM dd, yyyy h:mm:ss a")
			.withLocale(Locale.US);

	public Tweet(String json) throws JSONException, TwitterException {
		super(new JSONObject(json));
	}

	public Tweet(JSONObject json) {
		super(json);
	}

	@Override
	public User extractUserInfo(JSONObject jsonObj) {
		Status tweet;
		User user = null;
		try {
			tweet = TwitterObjectFactory.createStatus(jsonObj.toString());
			if(tweet.getUser()!=null)
				user = new User("" + tweet.getUser().getId());
		} catch (TwitterException e) {
			e.printStackTrace();
		}

		return user;
	}

	@Override
	public Point extractLocationInfo(JSONObject jsonObj) {
		Point p = null;
		try {
			Status tweet = TwitterObjectFactory.createStatus(jsonObj.toString());
			if (tweet.getGeoLocation() == null) {
				JSONObject location = jsonObj.has("location") ? jsonObj.getJSONObject("location") : null;
				if (location == null) {
					return null;
				} else {
					p = new PointImpl(location.getDouble("longitude"), location.getDouble("latitude"),
							SpatialContext.GEO);
				}
			} else {
				p = new PointImpl(tweet.getGeoLocation().getLongitude(), tweet.getGeoLocation().getLatitude(),
						SpatialContext.GEO);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return p;
	}

	@Override
	public String extractId(JSONObject jsonObj) {
		Status tweet = null;
		try {
			tweet = TwitterObjectFactory.createStatus(jsonObj.toString());
			return tweet.getId() + "";
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		return null;

	}

	@Override
	public LocalDateTime extractDateTimeInfo(JSONObject jsonObj) {
		try {
			Status tweet = TwitterObjectFactory.createStatus(jsonObj.toString());
			if(tweet.getCreatedAt()==null){
				if(jsonObj.has("date")){
					return dateStringFormat.parseDateTime(jsonObj.getString("date")).toLocalDateTime();
				}
			}else{
				return new LocalDateTime(tweet.getCreatedAt());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean check(JSONObject jsonObj) {
		try {
			Status tweet = TwitterObjectFactory.createStatus(jsonObj.toString());
			if (tweet != null)
				return true;
			else
				return false;
		} catch (Exception ex) {
			return false;
		}

	}
}

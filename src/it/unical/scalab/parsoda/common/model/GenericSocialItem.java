package it.unical.scalab.parsoda.common.model;

import org.joda.time.LocalDateTime;
import org.json.JSONObject;

import com.spatial4j.core.context.SpatialContext;
import com.spatial4j.core.shape.Point;
import com.spatial4j.core.shape.impl.PointImpl;

import it.unical.scalab.parsoda.common.Metadata;
import it.unical.scalab.parsoda.common.User;

public class GenericSocialItem extends SocialItem {

	@SuppressWarnings("unused")
	private final static String SOURCE = "Generic";

	public GenericSocialItem(String s) {
		super(new JSONObject(s));
		this.put(Metadata.EXTRA_PAYLOAD,
				this.getJSONObject(Metadata.EXTRA_PAYLOAD).getJSONObject(Metadata.EXTRA_PAYLOAD));
	}

	public GenericSocialItem(JSONObject jsonObject) {
		super(jsonObject);
		this.put(Metadata.EXTRA_PAYLOAD,
				this.getJSONObject(Metadata.EXTRA_PAYLOAD).getJSONObject(Metadata.EXTRA_PAYLOAD));
	}
	
	@Override
	public User extractUserInfo(JSONObject jsonObj) {
		try {
			JSONObject userObj = jsonObj.getJSONObject(Metadata.BASIC_PAYLOAD).getJSONObject(Metadata.USER);
			User user = new User(userObj.getString(Metadata.USERID));
			if (userObj.has(Metadata.USERNAME))
				user.put(Metadata.USERNAME, userObj.getString(Metadata.USERNAME));
			return user;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Point extractLocationInfo(JSONObject jsonObj) {
		try {
			JSONObject geo = jsonObj.getJSONObject(Metadata.BASIC_PAYLOAD).getJSONObject(Metadata.LOCATION);

			return new PointImpl(geo.getDouble(Metadata.LONGITUDE), geo.getDouble(Metadata.LATITUDE),
					SpatialContext.GEO);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public String extractId(JSONObject json) {
		try {
			return json.getJSONObject(Metadata.BASIC_PAYLOAD).getString(Metadata.ID);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public LocalDateTime extractDateTimeInfo(JSONObject jsonObj) {
		try {
			return new LocalDateTime(jsonObj.getJSONObject(Metadata.BASIC_PAYLOAD).getString(Metadata.DATETIME));
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public boolean check(JSONObject obj) {
		if (obj.has(Metadata.BASIC_PAYLOAD) && obj.getJSONObject(Metadata.BASIC_PAYLOAD).has(Metadata.ID))
			return true;
		else
			return false;
	}

}

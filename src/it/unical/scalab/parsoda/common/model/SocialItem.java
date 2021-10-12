package it.unical.scalab.parsoda.common.model;

import org.joda.time.LocalDateTime;
import org.json.JSONObject;

import com.spatial4j.core.context.SpatialContext;
import com.spatial4j.core.shape.Point;
import com.spatial4j.core.shape.impl.PointImpl;

import it.unical.scalab.parsoda.common.Metadata;
import it.unical.scalab.parsoda.common.User;

public abstract class SocialItem extends JSONObject {

	public abstract User extractUserInfo(JSONObject obj);

	public abstract String extractId(JSONObject obj);
	
	public abstract boolean check(JSONObject obj);

	public abstract Point extractLocationInfo(JSONObject obj);

	public abstract LocalDateTime extractDateTimeInfo(JSONObject obj);

	public SocialItem(String json) {
		this(new JSONObject(json));
	}

	public SocialItem(JSONObject jsonObject) {
		
		if(!check(jsonObject)) throw new IllegalArgumentException();

		JSONObject basicJson = new JSONObject();

		// Add user info
		User user = extractUserInfo(jsonObject);    
		if (user != null) {
			JSONObject userJson = new JSONObject(user);
			basicJson.put(Metadata.USER, userJson);
		}
		// Add location info
		Point location = extractLocationInfo(jsonObject);
		
		if (location != null) {
			JSONObject locationJson = new JSONObject();
			locationJson.put(Metadata.LONGITUDE, location.getX());
			locationJson.put(Metadata.LATITUDE, location.getY());
			basicJson.put(Metadata.LOCATION, locationJson);
		}
		// Add item id
		String id = extractId(jsonObject);
		if (id != null) {
			basicJson.put(Metadata.ID, id);
		}
		// Add date time info
		LocalDateTime datetime = extractDateTimeInfo(jsonObject);
		if (datetime != null) {
			basicJson.put(Metadata.DATETIME, datetime);
		}

		this.put(Metadata.BASIC_PAYLOAD, basicJson);
		this.put(Metadata.EXTRA_PAYLOAD, jsonObject);

	}

	public JSONObject getUserInfo() {
		return this.getJSONObject(Metadata.BASIC_PAYLOAD).getJSONObject(Metadata.USER);
	}

	public String getUserId() {
		try {
			return this.getJSONObject(Metadata.BASIC_PAYLOAD).getJSONObject(Metadata.USER).getString(Metadata.USERID);
		} catch (Exception e) {
			return null;
		}
	}

	public Object removeExtraPayload() {
		return this.remove(Metadata.EXTRA_PAYLOAD);
	}

	public LocalDateTime getDateTime() {
		try {
			return (LocalDateTime) this.getJSONObject(Metadata.BASIC_PAYLOAD).get(Metadata.DATETIME);
		} catch (Exception e) {
			return null;
		}
	}

	public Point getLocation() {
		try {
			double x = this.getJSONObject(Metadata.BASIC_PAYLOAD).getJSONObject(Metadata.LOCATION)
					.getDouble(Metadata.LONGITUDE);
			double y = this.getJSONObject(Metadata.BASIC_PAYLOAD).getJSONObject(Metadata.LOCATION)
					.getDouble(Metadata.LATITUDE);
			return new PointImpl(x, y, SpatialContext.GEO);
		} catch (Exception e) {
			return null;
		}
	}

	public JSONObject getJson() {
		return this;
	}

	public String getId() {
		try {
			return this.getJSONObject(Metadata.BASIC_PAYLOAD).getString(Metadata.USERID);
		} catch (Exception e) {
			return null;
		}
	}

	public Object search(String key) {
		
		Object res = null;
		res = subSearch(this, key);
		if(res == null && this.has(Metadata.BASIC_PAYLOAD)) {
			res = subSearch(this.getJSONObject(Metadata.BASIC_PAYLOAD), key);
		}
		if(res == null && this.has(Metadata.EXTRA_PAYLOAD)) {
			res = subSearch(this.getJSONObject(Metadata.EXTRA_PAYLOAD), key);
		}
		return res;
	}
	
	private Object subSearch(JSONObject obj, String key) {
		String[] parts = key.split("\\.");
		if(obj.has(parts[0])) {
			if(parts.length>1) {
				return subSearch(obj.getJSONObject(parts[0]), key.substring(key.indexOf(parts[0])+ parts[0].length() + 1));
			}else {
				return obj.get(key);
			}		
		}
		return null;
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SocialItem other = (SocialItem) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		return true;
	}

	public String getSource() {
		return this.getClass().getSimpleName();
	}

}

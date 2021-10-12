package it.unical.scalab.parsoda.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import it.unical.scalab.parsoda.common.model.SocialItem;
import it.unical.scalab.parsoda.common.model.FlickrSocialItem;
import it.unical.scalab.parsoda.common.model.GenericSocialItem;
import it.unical.scalab.parsoda.common.model.TwitterSocialItem;

public class GeotaggedItemBuilder {

	public static SocialItem create(String s) {
		try {
			return new GenericSocialItem(s);
		} catch (Exception e) {
		}
		try {
			return new FlickrSocialItem(s);
		} catch (Exception e) {
		}
		try {
			return new TwitterSocialItem(s);
		} catch (Exception e) {
		}

		return null;

	}

	public static SocialItem create(JSONObject s) {
		
		try {
			return new GenericSocialItem(s);
		} catch (JSONException e) {
		}
		
		try {
			return new FlickrSocialItem(s);
		} catch (JSONException e) {
		}

		try {
			return new TwitterSocialItem(s);
		} catch (JSONException e) {
		}

		return null;
	}

	public static Map<String, String> toMap(JSONObject object) throws JSONException {
		Map<String, String> map = new HashMap<String, String>();

		Iterator<String> keysItr = object.keys();
		while (keysItr.hasNext()) {
			String key = keysItr.next();
			Object value = object.get(key);
			if (value instanceof JSONArray) {
				value = GeotaggedItemBuilder.toList((JSONArray) value);
			} else if (value instanceof JSONObject) {
				value = toMap((JSONObject) value);
			}
			map.put(key, value.toString());
		}
		return map;
	}

	public static List<Object> toList(JSONArray array) {
		List<Object> list = new ArrayList<Object>();

		for (int i = 0; i < array.length(); i++) {
			Object value = array.get(i);
			if (value instanceof JSONArray) {
				value = toList((JSONArray) value);
			}

			else if (value instanceof JSONObject) {
				value = toMap((JSONObject) value);
			}
			list.add(value);
		}
		return list;
	}
}

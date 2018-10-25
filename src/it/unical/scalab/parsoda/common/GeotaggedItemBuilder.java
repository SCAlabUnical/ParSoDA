package it.unical.scalab.parsoda.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import it.unical.scalab.parsoda.common.model.AbstractGeotaggedItem;
import it.unical.scalab.parsoda.common.model.Flickr;
import it.unical.scalab.parsoda.common.model.GenericGeotaggedItem;
import it.unical.scalab.parsoda.common.model.Tweet;

public class GeotaggedItemBuilder {

	public static AbstractGeotaggedItem create(String s) {
		try {
			return new GenericGeotaggedItem(s);
		} catch (Exception e) {
		}
		try {
			return new Flickr(s);
		} catch (Exception e) {
		}
		try {
			return new Tweet(s);
		} catch (Exception e) {
		}

		return null;

	}

	public static AbstractGeotaggedItem create(JSONObject s) {
		
		try {
			return new GenericGeotaggedItem(s);
		} catch (JSONException e) {
		}
		
		try {
			return new Flickr(s);
		} catch (JSONException e) {
		}

		try {
			return new Tweet(s);
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

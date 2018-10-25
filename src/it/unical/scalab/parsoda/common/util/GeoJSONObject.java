package it.unical.scalab.parsoda.common.util;


import java.util.Locale;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class GeoJSONObject extends JSONObject {

	public GeoJSONObject() {
		super();
	}

	public GeoJSONObject(String string) {
		super(string);
	}

	public GeoJSONObject(JSONTokener x) {
		super(x);
	}

	public GeoJSONObject(Map map) {
		super(map);
	}

	public GeoJSONObject(JSONObject jo, String[] names) {
		super(jo, names);
	}

	public GeoJSONObject(Object object, String[] names) {
		super(object, names);
	}

	public GeoJSONObject(Object bean) {
		super(bean);
	}

	public GeoJSONObject(String baseName, Locale locale) throws JSONException {
		super(baseName, locale);
	}

	public void addProperties(String propertyName, int value) {
		if (!this.has("properties")) {
			JSONObject tmpObj = new JSONObject().put(propertyName, value);
			this.put("properties", tmpObj);
		} else {
			this.getJSONObject("properties").put(propertyName, value);
		}
	}

	public void addProperties(String propertyName, String value) {
		if (!this.has("properties")) {
			JSONObject tmpObj = new JSONObject().put(propertyName, value);
			this.put("properties", tmpObj);
		} else {
			this.getJSONObject("properties").put(propertyName, value);
		}
	}

	public void addShapeLineColor(String color) {
		if (this.has("geometry") && this.getJSONObject("geometry").get("type").equals("Point")) {
			this.addProperties("marker-color", color);
		} else {
			this.addProperties("stroke", color);
		}
	}

	public void addShapeLineWidth(int width) {
		this.addProperties("stroke-width", width);
	}

	public void addShapeTitle(String json, String title) {
		this.addProperties("title", title);
	}

	public void addShapeMarkerSymbol(String json, String symbol) {
		this.addProperties("marker-symbol", symbol);
	}

}

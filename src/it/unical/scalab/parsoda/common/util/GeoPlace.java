package it.unical.scalab.parsoda.common.util;


import org.json.JSONObject;

import com.spatial4j.core.shape.Shape;

public class GeoPlace {
	private Shape shape;
	private String name;
	private String id;
	private JSONObject metadata = new JSONObject();

	public void setShape(Shape shape) {
		this.shape = shape;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setMetadata(JSONObject metadata) {
		this.metadata = metadata;
	}

	public GeoPlace(Shape shape, String name, String id) {
		this.shape = shape;
		this.name = name;
		this.id = id;
	}

	public GeoPlace() {

	}

	public Shape getShape() {
		return shape;
	}

	public String getName() {
		return name;
	}

	public String getId() {
		return id;
	}

	public JSONObject getMetadata() {
		return metadata;
	}

	public Object getMetadataValue(String key) {
		if (metadata.has(key))
			return metadata.get(key);
		else
			return null;
	}

}

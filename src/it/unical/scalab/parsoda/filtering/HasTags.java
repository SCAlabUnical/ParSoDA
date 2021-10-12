package it.unical.scalab.parsoda.filtering;

import org.json.JSONArray;

import it.unical.scalab.parsoda.common.AbstractFilterFunction;
import it.unical.scalab.parsoda.common.Metadata;
import it.unical.scalab.parsoda.common.model.SocialItem;

public class HasTags extends AbstractFilterFunction {

	public HasTags() {
		super();
	}

	public HasTags(String options) {
		super(options);
	}

	public boolean test(SocialItem g) {
		Object tagsObj = g.search(Metadata.TAGS);
		if (tagsObj == null)
			tagsObj = g.search(Metadata.HASHTAGS);
		if (tagsObj != null) {
			JSONArray tags = (JSONArray) tagsObj;
			return tags.length()>0;
		}
		return false;
	}

}

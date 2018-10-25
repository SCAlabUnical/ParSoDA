package it.unical.scalab.parsoda.filtering;

import org.json.JSONArray;

import it.unical.scalab.parsoda.common.AbstractFilterFunction;
import it.unical.scalab.parsoda.common.Metadata;
import it.unical.scalab.parsoda.common.model.AbstractGeotaggedItem;

public class ContainsTags extends AbstractFilterFunction {

	public ContainsTags() {
		super();
	}

	
	public ContainsTags(String options) {
		super(options);
	}

	public boolean test(AbstractGeotaggedItem g) {
		String separator = ","; 
		if(this.options.exists("separator"))
			this.options.getOption("separator");
		String field = this.options.getOption("tags");
		if(field==null) return false;
		for (String s : field.split(separator)) {
			Object tagsObj = g.search(Metadata.TAGS);
			if(tagsObj==null)
				tagsObj = g.search(Metadata.HASHTAGS);
			if (tagsObj != null) {
				JSONArray tags = (JSONArray) tagsObj;
				for (int i = 0; i < tags.length(); i++) {
					if(tags.getString(i).equals(s))
						return true;
				}
			}
		}
		return false;
	}

}

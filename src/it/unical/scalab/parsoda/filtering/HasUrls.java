package it.unical.scalab.parsoda.filtering;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import it.unical.scalab.parsoda.common.AbstractFilterFunction;
import it.unical.scalab.parsoda.common.Metadata;
import it.unical.scalab.parsoda.common.model.SocialItem;

public class HasUrls extends AbstractFilterFunction {

	public HasUrls() {
		super();
	}

	public HasUrls(String options) {
		super(options);
	}

	public boolean test(SocialItem g) {
		String separator = ",";
		if (this.options.exists("separator"))
			this.options.getOption("separator");
		if (this.options.exists("url")) {
			String field = this.options.getOption("urls");
			if (field == null)
				return false;
			for (String s : field.split(separator)) {
				Object urlObj = g.search(Metadata.URLS);
				if (urlObj != null) {
					JSONArray urls = (JSONArray) urlObj;
					for (int i = 0; i < urls.length(); i++) {
						if (urls.getString(i).equals(s))
							return true;
					}
				}
			}
		} else {
			// Seach only if urls field is not empty
			Object field = g.search(Metadata.URLS);
			if (field == null)
				return false;

			Object json = new JSONTokener(field.toString()).nextValue();
			if (json instanceof JSONObject) {
				if (json.toString().length() > 0)
					return true;
			} else if (json instanceof JSONArray) {
				JSONArray jsonArray = (JSONArray) json;
				if (jsonArray.length() > 0)
					return true;
			}
		}

		return false;
	}

}

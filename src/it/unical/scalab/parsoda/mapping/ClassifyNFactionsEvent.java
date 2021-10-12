package it.unical.scalab.parsoda.mapping;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import it.unical.scalab.parsoda.common.AbstractMapFunction;
import it.unical.scalab.parsoda.common.Metadata;
import it.unical.scalab.parsoda.common.model.SocialItem;

/*
 * Assign a label if the item contains a specific keywords
 */
public class ClassifyNFactionsEvent extends AbstractMapFunction {

	HashMap<String, HashSet<String>> keywords = null;
	public static String NEUTRAL = "neutral";
	HashMap<String, Integer> keywordPosition = new HashMap<String, Integer>();
	int pos = 0;

	public ClassifyNFactionsEvent() {
		super();
	}

	public ClassifyNFactionsEvent(String options) throws IOException {
		super(options);

	}

	@Override
	public SocialItem apply(SocialItem g) {

		if (keywords == null)
			try {
				loadKeywords();
			} catch (IOException e) {
				e.printStackTrace();
			}

		int countClassNotNeutral = 0;
		String lastNotNeutralClass = "";
		String label = NEUTRAL;
		for (Entry<String, HashSet<String>> entry : keywords.entrySet()) {

			for (String tag : entry.getValue()) {
				if (g.toString().contains(tag)) {
					if (!entry.getKey().equals(NEUTRAL)) {
						if (!lastNotNeutralClass.equals(entry.getKey())) {
							lastNotNeutralClass = entry.getKey();
							label = lastNotNeutralClass;
							countClassNotNeutral++;
						}
						if (countClassNotNeutral > 1) {
							label = NEUTRAL;
						}
						break;
					}
				}
			}
			if (countClassNotNeutral > 1) {
				break;
			}
		}

		g.put(Metadata.LABEL_CLASS, keywordPosition.get(label));
		return g;

	}

	private void loadKeywords() throws IOException {
		keywords = new HashMap<String, HashSet<String>>();
		if (context.getCacheFiles() != null && context.getCacheFiles().length > 0) {
			URI[] uris = context.getCacheFiles();
			String path = null;
			for (URI uri : uris) {
				if (FilenameUtils.getName(uri.getPath()).equals(this.options.getOption("f"))) {
					path = uri.getRawPath();
					File initialFile = new File(path);
					InputStream is = FileUtils.openInputStream(initialFile);
					String jsonTxt = IOUtils.toString(is);
					JSONObject json = new JSONObject(jsonTxt);

					Set<String> keys = json.keySet();
					keys.forEach(key -> {
						if (json.get(key) instanceof JSONArray) {
							JSONArray values = json.getJSONArray(key);
							HashSet<String> list = new HashSet<String>();
							for (int i = 0; i < values.length(); i++) {
								list.add(values.getString(i));
							}
							this.keywords.put(key, list);
							this.keywordPosition.put(key, pos);
							pos++;
						}
					});

					break;
				}
			}
		}
	}

}

package it.unical.scalab.parsoda.mapping;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiManager;
import com.vdurmont.emoji.EmojiParser;

import it.unical.scalab.parsoda.common.AbstractMapFunction;
import it.unical.scalab.parsoda.common.Metadata;
import it.unical.scalab.parsoda.common.model.AbstractGeotaggedItem;

public class ClassifyByEmoji extends AbstractMapFunction {

	HashMap<String, Integer> emoji = null;

	public ClassifyByEmoji() {
		super();
	}

	public ClassifyByEmoji(String options) throws IOException {
		super(options);
	}

	@Override
	public AbstractGeotaggedItem apply(AbstractGeotaggedItem g) {
		if (this.emoji == null)
			try {
				loadEmoji();
			} catch (IOException e) {
				e.printStackTrace();
			}
		List<String> emojiItem = EmojiParser.extractEmojis(g.toString());
		String currEmojiDescr = null;
		int sumPolarity = 0;
		Emoji emo;
		List<String> alias;
		for (String e : emojiItem) {
			currEmojiDescr = EmojiManager.getByUnicode(e).getDescription().replace(" ", "_");
			alias = new LinkedList<String>();
			emo = EmojiManager.getByUnicode(e);
			if (emo != null) {
				alias.addAll(emo.getAliases());
				alias.add(currEmojiDescr);
				for (String a : alias) {
					if (this.emoji.containsKey(a)) {
						sumPolarity += this.emoji.get(a);
						break;
					}
				}
			}
		}
		int label = 0;
		if (sumPolarity <= -2) {
			label = -1;
		} else if (sumPolarity >= 2) {
			label = 1;
		}
		g.put(Metadata.LABEL_CLASS, label);
		return g;

	}

	private void loadEmoji() throws IOException {
		emoji = new HashMap<String, Integer>();
		if (context.getCacheFiles() != null && context.getCacheFiles().length > 0) {
			URI[] uris = context.getCacheFiles();
			String path = null;
			for (URI uri : uris) {
				if (FilenameUtils.getName(uri.getPath()).equals(this.options.getOption("f"))) {
					path = uri.getRawPath();
					File initialFile = new File(path);
					InputStream is = FileUtils.openInputStream(initialFile);
					String jsonTxt = IOUtils.toString(is);
					JSONArray jsonArray = new JSONArray(jsonTxt);
					JSONObject e = null;
					for (int i = 0; i < jsonArray.length(); i++) {
						e = (JSONObject) jsonArray.get(i);
						this.emoji.put(e.getString("name"), e.getInt("polarity"));
					}
					break;
				}
			}
		}
	}

}

package it.unical.scalab.parsoda.filtering;

import java.util.List;

import com.vdurmont.emoji.EmojiParser;

import it.unical.scalab.parsoda.common.AbstractFilterFunction;
import it.unical.scalab.parsoda.common.model.SocialItem;

public class HasEmoji extends AbstractFilterFunction {

	public HasEmoji() {
		super();
	}

	public HasEmoji(String options) {
		super(options);
	}

	public boolean test(SocialItem g) {
		List<String> emojiItem = EmojiParser.extractEmojis(g.toString());
		return emojiItem.size()>0;
	}

}

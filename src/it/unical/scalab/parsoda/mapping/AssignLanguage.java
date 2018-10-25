package it.unical.scalab.parsoda.mapping;

import org.apache.tika.language.LanguageIdentifier;

import it.unical.scalab.parsoda.common.AbstractMapFunction;
import it.unical.scalab.parsoda.common.Metadata;
import it.unical.scalab.parsoda.common.model.AbstractGeotaggedItem;

@SuppressWarnings("deprecation")
public class AssignLanguage extends AbstractMapFunction {
	
	public AssignLanguage() {
		super();
	}

	public AssignLanguage(String options) {
		super(options);
	}

	@Override
	public AbstractGeotaggedItem apply(AbstractGeotaggedItem g) {
		String language = null;
		String text = null;
		if(g.getSource().equalsIgnoreCase(Metadata.SOURCE_TWITTER)) {
			text = g.search("description")+ " ";
		LanguageIdentifier identifier = new LanguageIdentifier(text);
	    language = identifier.getLanguage();
		}else if(g.getSource().equalsIgnoreCase(Metadata.SOURCE_FLICKR)) {
			text = g.search("description")+ " "+g.search("title");
			LanguageIdentifier identifier = new LanguageIdentifier(text);
		    language = identifier.getLanguage();			
		}else if(g.getSource().equalsIgnoreCase(Metadata.SOURCE_FACEBOOK)) {
			text = g.search("text").toString();
			LanguageIdentifier identifier = new LanguageIdentifier(text);
		    language = identifier.getLanguage();			
		}
		if(language!=null)
			g.put(Metadata.LANGUAGE, language);
		return g;

	}


}

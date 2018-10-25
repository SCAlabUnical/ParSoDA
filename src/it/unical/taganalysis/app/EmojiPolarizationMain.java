package it.unical.taganalysis.app;

import it.unical.scalab.parsoda.acquisition.FileReaderCrawler;
import it.unical.scalab.parsoda.analysis.TwoFactionsPolarization;
import it.unical.scalab.parsoda.common.MultiKeysMapperWriter;
import it.unical.scalab.parsoda.common.SocialDataApp;
import it.unical.scalab.parsoda.filtering.HasEmoji;
import it.unical.scalab.parsoda.mapping.ClassifyByEmoji;
import it.unical.scalab.parsoda.reduction.ReduceByTwoFactionsPolarization;

public class EmojiPolarizationMain {

	public static void main(String[] args) {
		SocialDataApp app = new SocialDataApp("Emoji Polarization");
		app.setOutputBasePath("outputApp");
		app.setLocatFileSystem();
		app.setMapperWriter(MultiKeysMapperWriter.class, null);
		String[] cFiles = { "resources/emoji.json" };
		app.setDistributedCacheFiles(cFiles);
		Class[] cFunctions = { FileReaderCrawler.class };
		String[] cParams = { "-i tweetsFinal.json" };
		app.setCrawlers(cFunctions, cParams);
		Class[] fFunctions = { HasEmoji.class };
		String[] fParams = null;
		app.setFilters(fFunctions, fParams);
		Class[] mFunctions = { ClassifyByEmoji.class };
		String[] mParams = {"-f emoji.json"};	
		app.setMapFunctions(mFunctions, mParams);
		String groupKey = "userId";
		String sortKey = "DATETIME";
		app.setPartitioningKeys(groupKey, sortKey);
		Class rFunction = ReduceByTwoFactionsPolarization.class;
		String rParams = "-t 5";
		app.setReduceFunction(rFunction, rParams);
		Class aFunction = TwoFactionsPolarization.class;
		String aParams = null;
		app.setAnalysisFunction(aFunction, aParams);	
		app.execute();
	}

}

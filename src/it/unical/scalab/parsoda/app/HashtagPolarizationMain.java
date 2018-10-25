package it.unical.scalab.parsoda.app;

import it.unical.scalab.parsoda.acquisition.FileReaderCrawler;
import it.unical.scalab.parsoda.common.MultiKeysMapperWriter;
import it.unical.scalab.parsoda.common.SocialDataApp;
import it.unical.scalab.parsoda.filtering.HasEmoji;
import it.unical.scalab.parsoda.filtering.HasTags;
import it.unical.scalab.parsoda.mapping.ClassifyByEmoji;
import it.unical.scalab.parsoda.reduction.ReduceByAveragePolarization;

public class HashtagPolarizationMain {

	public static void main(String[] args) {
		SocialDataApp app = new SocialDataApp("Hashtags Polarization");
		app.setOutputBasePath("outputApp");
		app.setLocatFileSystem();
		app.setMapperWriter(MultiKeysMapperWriter.class, null);
		String[] cFiles = { "resources/emoji.json" };
		app.setDistributedCacheFiles(cFiles);
		Class[] cFunctions = { FileReaderCrawler.class };
		String[] cParams = { "-i resources/tweetsFinal.json" };
		app.setCrawlers(cFunctions, cParams);
		Class[] fFunctions = { HasTags.class,  HasEmoji.class};
		String[] fParams = null;
		app.setFilters(fFunctions, fParams);
		Class[] mFunctions = { ClassifyByEmoji.class };
		String[] mParams = {"-f emoji.json"};	
		app.setMapFunctions(mFunctions, mParams);
		String groupKey = "hashtags";
		String sortKey = "DATETIME";
		app.setPartitioningKeys(groupKey, sortKey);
		Class rFunction = ReduceByAveragePolarization.class;
		String rParams = "-t 0";
		app.setReduceFunction(rFunction, rParams);
		app.execute();
	}

}

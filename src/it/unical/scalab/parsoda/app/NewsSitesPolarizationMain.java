package it.unical.scalab.parsoda.app;

import it.unical.scalab.parsoda.acquisition.FileReaderCrawler;
import it.unical.scalab.parsoda.analysis.TwoFactionsPolarization;
import it.unical.scalab.parsoda.common.MultiKeysMapperWriter;
import it.unical.scalab.parsoda.common.SocialDataApp;
import it.unical.scalab.parsoda.filtering.HasUrls;
import it.unical.scalab.parsoda.mapping.ClassifyTwoFactionsEvent;
import it.unical.scalab.parsoda.reduction.ReduceByTwoFactionsPolarization;

public class NewsSitesPolarizationMain {

	public static void main(String[] args) {
		SocialDataApp app = new SocialDataApp("News Site Polarization");
		app.setOutputBasePath("outputApp");
		app.setLocatFileSystem();
		app.setMapperWriter(MultiKeysMapperWriter.class, null);
		String[] cFiles = { "resources/twoFactionKeywords.json" };
		app.setDistributedCacheFiles(cFiles);
		Class[] cFunctions = { FileReaderCrawler.class };
		String[] cParams = { "-i resources/tweetsFinal.json" };
		app.setCrawlers(cFunctions, cParams);
		Class[] fFunctions = { HasUrls.class };
		String[] fParams = null;
		app.setFilters(fFunctions, fParams);
		Class[] mFunctions = { ClassifyTwoFactionsEvent.class };
		String[] mParams = {"-f twoFactionKeywords.json"};	
		app.setMapFunctions(mFunctions, mParams);
		String groupKey = "urls";
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

package it.unical.taganalysis.app;


import it.unical.scalab.parsoda.acquisition.FileReaderCrawler;
import it.unical.scalab.parsoda.analysis.TwoFactionsPolarization;
import it.unical.scalab.parsoda.common.SocialDataApp;
import it.unical.scalab.parsoda.mapping.ClassifyTwoFactionsEvent;
import it.unical.scalab.parsoda.reduction.ReduceByTwoFactionsPolarization;

public class UserPolarizationMain {

	public static void main(String[] args) {
		SocialDataApp app = new SocialDataApp("2 Faction User Polarization");
		app.setOutputBasePath("outputApp");
		app.setLocatFileSystem();
		String[] cFiles = { "resources/twoFactionKeywords.json" };
		app.setDistributedCacheFiles(cFiles);
		Class[] cFunctions = { FileReaderCrawler.class};
		String[] cParams = { "-i resources/tweetsFinal.json" };
		app.setCrawlers(cFunctions, cParams);
		Class[] mFunctions = { ClassifyTwoFactionsEvent.class};
		String[] mParams = {"-f twoFactionKeywords.json"};	
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

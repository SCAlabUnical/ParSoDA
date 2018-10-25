package it.unical.taganalysis.app;

import it.unical.scalab.parsoda.acquisition.FileReaderCrawler;
import it.unical.scalab.parsoda.analysis.FPGrowth;
import it.unical.scalab.parsoda.common.SocialDataApp;
import it.unical.scalab.parsoda.filtering.IsGeotagged;
import it.unical.scalab.parsoda.filtering.IsInCircle;
import it.unical.scalab.parsoda.mapping.FindPoI;
import it.unical.scalab.parsoda.reduction.ReduceByTrajectories;

public class FrequentItemsetMain {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) {
		SocialDataApp app = new SocialDataApp("Frequent Itemset - City of Rome");
		String[] cFiles = { "resources/RomeRoIs.kml" };
		app.setDistributedCacheFiles(cFiles);
		app.setOutputBasePath("outputApp");
		app.setLocatFileSystem();
		Class[] cFunctions = { FileReaderCrawler.class };
		String[] cParams = { "-i resources/Colosseo500m.json" };
		app.setCrawlers(cFunctions, cParams);
		Class[] fFunctions = { IsGeotagged.class, IsInCircle.class };
		String[] fParams = { " ", "-lng 12.492 -lat 41.890 -radius 10000" };
		app.setFilters(fFunctions, fParams);
		Class[] mFunctions = { FindPoI.class };
		String[] mParams = null;
		app.setMapFunctions(mFunctions, mParams);
		String groupKey = "USER.USERID";
		String sortKey = "DATETIME";
		app.setPartitioningKeys(groupKey, sortKey);
		Class rFunction = ReduceByTrajectories.class;
		String rParams = "-t 5";
		app.setReduceFunction(rFunction, rParams);
		Class aFunction = FPGrowth.class;
		String aParams = "-minLevel 1 -minSupport 0.0001";
		app.setAnalysisFunction(aFunction, aParams);	
		app.execute();
	}

}

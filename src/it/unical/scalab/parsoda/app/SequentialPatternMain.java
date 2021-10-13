package it.unical.scalab.parsoda.app;
import it.unical.scalab.parsoda.acquisition.FileReaderCrawler;
import it.unical.scalab.parsoda.analysis.PrefixSpan;
import it.unical.scalab.parsoda.common.SocialDataApp;
import it.unical.scalab.parsoda.filtering.IsGeotagged;
import it.unical.scalab.parsoda.filtering.IsInPlace;
import it.unical.scalab.parsoda.mapping.FindPoI;
import it.unical.scalab.parsoda.reduction.ReduceByTrajectories;
import it.unical.scalab.parsoda.visualization.SortPrefixSpanBy;

public class SequentialPatternMain {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) {
		SocialDataApp app = new SocialDataApp("Sequential Pattern Mining - City of Rome");

		// DATA ACQUISITION
		String[] cFiles = { "resources/RomeRoIs.kml" };
		app.setDistributedCacheFiles(cFiles); // DISTRIBUTE AN ARRAY OF FILES TO ALL MAP/REDUCE NODES
		app.setLocatFileSystem(); // USE THE LOCAL FILESYSTEM (FOR TESTING PURPOSE)
		Class[] cFunctions = { FileReaderCrawler.class };
		String[] cParams = { "-i resources/Colosseum500m.json" };
		app.setCrawlers(cFunctions, cParams);

		// DATA FILTERING
		Class[] fFunctions = { IsGeotagged.class, IsInPlace.class };
		String[] fParams = { " ", "-lng 12.492 -lat 41.890 -radius 10000" };
		app.setFilters(fFunctions, fParams);

		// DATA MAPPING
		Class[] mFunctions = { FindPoI.class };
		String[] mParams = null;
		app.setMapFunctions(mFunctions, mParams);

		// DATA PARTITIONING
		String groupKey = "USER.USERID";
		String sortKey = "DATETIME";
		app.setPartitioningKeys(groupKey, sortKey);

		// DATA REDUCTION
		Class rFunction = ReduceByTrajectories.class;
		String rParams = "-t 5";
		app.setReduceFunction(rFunction, rParams);

		// DATA ANALYSIS
		Class aFunction = PrefixSpan.class;
		String aParams = "-maxPatternLength 5 -minSupport 0.005";
		app.setAnalysisFunction(aFunction, aParams);

		// DATA VISUALIZATION
		Class vFunction = SortPrefixSpanBy.class;
		String vParams = "-k support -d DESC";
		app.setVisualizationFunction(vFunction, vParams);
		app.execute();
	}
}

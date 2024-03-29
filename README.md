<img src="ParSoDA-logo.png" width="463"/>
ParSoDA (Parallel Social Data Analytics) is a Java programming library for simplifying the development of parallel social media mining application executed on High Performance Computing systems. ParSoDA defines a general framework for a social media analysis application that includes a number of steps (data acquisition, filtering, mapping, partitioning, reduction, analysis, and visualization), and provides a predefined (but extensible) set of functions for each data processing step. 

Thus, an application developed with ParSoDA is expressed by a concise code that specifies the functions invoked at each step. User applications based on the ParSoDA library can be run on both Apache Hadoop and Spark clusters. 

The current version of the library contains more than forty predefined functions organized in seven packages, corresponding to the seven ParSoDA steps. In particular, the library contains: 
- n. 3 Data acquisition functions: parsoda.acquisition.FlickrCrawler, parsoda.acquisition.TwitterCrawler, parsoda.acquisition.FileCrawler
- n. 16 Data filtering functions: parsoda.filtering.AndFiltering, parsoda.filtering.ContainsKeywords, parsoda.filtering.ContainsTags, parsoda.filtering.HasEmoji,  parsoda.filtering.HasLang, parsoda.filtering.HasTags, parsoda.filtering.HasUrls, parsoda.filtering.IsFromSource, parsoda.filtering.IsGeotagged, parsoda.filtering.IsInCircle, parsoda.filtering.IsInDate, parsoda.filtering.IsInPlace, parsoda.filtering.IsInShape, parsoda.filtering.IsNotRetweet, parsoda.filtering.IsOfUser, parsoda.filtering.OrFiltering
- n. 7 Data mapping functions, parsoda.mapping.AssignLanguage, parsoda.mapping.ClassifyTwoFactionEvent, parsoda.mapping.ClassifyNFactionEvent, parsoda.mapping.ClassifyByEmoji, parsoda.mapping.ClassifyByGeoCell, parsoda.mapping.FindPoI, parsoda.mapping.RemoveExtraPayload
- n. 1 Data partitioning functions, parsoda.partitioning.SecondarySort
- n. 6 Data reduction functions, parsoda.reduction.ReduceByAveragePolarization, parsoda.reduction.ReduceByCoordinate, parsoda.reduction.ReduceByItemsets, parsoda.reduction.ReduceByNFactionsPolarization, parsoda.reduction.ReduceByTrajectories, parsoda.reduction.ReduceByTwoFactionsPolarization 
- n. 5 Data analysis functions, parsoda.analysis.ExtractRoIs, parsoda.analysis.FPGrowth, parsoda.analysis.NFactionsPolarization, parsoda.analysis.PrefixSpan, parsoda.analysis.TwoFactionsPolarization
- n. 3 Data visualization functions, parsoda.visualization.RoIsToKML, parsoda.visualization.SortPrefixSpanBy, parsoda.visualization.SortByHashtagSupport


## How to cite
L. Belcastro, F. Marozzo, D. Talia, P. Trunfio, "ParSoDA: High-Level Parallel Programming for Social Data Mining". Social Network Analysis and Mining, vol. 9, n. 1, 2019

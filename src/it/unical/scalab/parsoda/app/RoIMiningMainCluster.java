package it.unical.scalab.parsoda.app;

import it.unical.scalab.parsoda.acquisition.FileReaderCrawler;
import it.unical.scalab.parsoda.analysis.ExtractRoIs;
import it.unical.scalab.parsoda.common.SocialDataApp;
import it.unical.scalab.parsoda.filtering.ContainsKeywords;
import it.unical.scalab.parsoda.filtering.IsGeotagged;
import it.unical.scalab.parsoda.reduction.ReduceByCoordinates;
import it.unical.scalab.parsoda.visualization.RoIsToKML;

public class RoIMiningMainCluster {

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void main(String[] args) {
        SocialDataApp app = new SocialDataApp("RoI Mining - Colosseum");
        app.setOutputBasePath(args[0].trim() + "/outputApp");
        app.setHDFS("namenode", "9000");
        app.setNumReducer(1);

        // Data crawling
        Class[] cFunctions = {FileReaderCrawler.class};
        String[] cParams = {"-i " + args[0].trim() + "/Colosseum500m.json"};
        app.setCrawlers(cFunctions, cParams);

        // Data filtering
        String colosseumSynonyms = "colosse:colis:collis:collos:Amphiteatrum Flavium:Amphitheatrum Flavium:An Colasaem:Coliseo:Coliseo de Roma:Coliseo de Roma:Coliseu de Roma:Coliseum:Coliseum:Coliseum:Coliseus:Colloseum:Coloseu:Colosseo:Colosseo:Colosseo:Colosseu:Colosseum:Colosseum:Colosseum:Colosseum:Colosseum:Colosseum:Colosseum:Colosseum:Colosseum:Colosseum:Culusseu:Kolezyum:Koliseoa:Kolize:Kolizejs:Kolizey:Kolizey:Koliziejus:Kolosej:Kolosej:Koloseo:Koloseo:Koloseum:Koloseum:Koloseum:Koloseum:Koloseum:Koloseum:Koloseum:Koloseumi:Kolosseum:Kolosseum:Kolosseum:Kolosseum";
        Class[] fFunctions = {IsGeotagged.class, ContainsKeywords.class};
        String[] fParams = {" ", "-separator : -keywords " + colosseumSynonyms};
        app.setFilters(fFunctions, fParams);

        // Data mapping
        // -- DEFAULT IDENTITY MAPPER

        // Data partitioning
        // -- DEFAULT DATA PARTITIONER

        // Data reduction
        Class rFunction = ReduceByCoordinates.class;
        String rParams = null;
        app.setReduceFunction(rFunction, rParams);

        // Data analysis
        Class aFunction = ExtractRoIs.class;
        String aParams = "-minPts 150 -eps 30";
        app.setAnalysisFunction(aFunction, aParams);

        // Data visualization
        Class vFunction = RoIsToKML.class;
        String vParams = null;
        app.setVisualizationFunction(vFunction, vParams);

        app.execute();
    }

}

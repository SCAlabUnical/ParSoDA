package it.unical.scalab.parsoda.app;

import it.unical.scalab.parsoda.acquisition.FileReaderCrawler;
import it.unical.scalab.parsoda.analysis.ExtractRoIs;
import it.unical.scalab.parsoda.analysis.PrefixSpan;
import it.unical.scalab.parsoda.common.SocialDataApp;
import it.unical.scalab.parsoda.filtering.ContainsKeywords;
import it.unical.scalab.parsoda.filtering.IsGeotagged;
import it.unical.scalab.parsoda.filtering.IsInPlace;
import it.unical.scalab.parsoda.mapping.FindPoI;
import it.unical.scalab.parsoda.reduction.ReduceByCoordinates;
import it.unical.scalab.parsoda.reduction.ReduceByTrajectories;
import it.unical.scalab.parsoda.visualization.RoIsToKML;
import it.unical.scalab.parsoda.visualization.SortPrefixSpanBy;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

public class RoIMiningMainClusterSocket {

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void main(String[] args) {
        try {

            SocialDataApp app = new SocialDataApp("RoI Mining - Colosseum - with SockerServer");

            ServerSocket serverSocket = new ServerSocket(1098);
            System.out.println("Server Socket is waiting for client connection...");

            Socket incoming = serverSocket.accept();
            System.out.println("Client connection accepter with host " + incoming.getInetAddress() + " on port " + incoming.getPort());
            BufferedReader in = new BufferedReader
                    (new InputStreamReader(incoming.getInputStream()));
            PrintWriter out = new PrintWriter(incoming.getOutputStream(), true);

            // Read the base path parameter
            String inputPath = in.readLine();
            out.println("MapReduce application started...");
            int status = runApp(app, inputPath);
            out.println("Execution completed with status code: " + status);

            Path pt = new Path(app.getOutputBasePath() + "-visualization/RoIs.kml");
            FileSystem fs = FileSystem.get(app.getConf());
            BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(pt)));
            try {
                String line;
                line = br.readLine();
                while (line != null) {
                    out.println(line);
                    line = br.readLine();
                }
            } finally {
                br.close();
            }

            in.close();
            out.close();

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }

    private static int runApp(SocialDataApp app, String input) {
        String colosseumSynonyms = "colosse:colis:collis:collos:Amphiteatrum Flavium:Amphitheatrum Flavium:An Colasaem:Coliseo:Coliseo:Coliseo de Roma:Coliseo de Roma:Coliseu de Roma:Coliseum:Coliseum:Coliseum:Coliseus:Colloseum:Coloseu:Colosseo:Colosseo:Colosseo:Colosseu:Colosseum:Colosseum:Colosseum:Colosseum:Colosseum:Colosseum:Colosseum:Colosseum:Colosseum:Colosseum:Culusseu:Kolezyum:Koliseoa:Kolize:Kolizejs:Kolizey:Kolizey:Koliziejus:Kolosej:Kolosej:Koloseo:Koloseo:Koloseum:Koloseum:Koloseum:Koloseum:Koloseum:Koloseum:Koloseum:Koloseumi:Kolosseum:Kolosseum:Kolosseum:Kolosseum";
        app.setOutputBasePath(input + "/outputApp");
        app.setHDFS("namenode", "9000");
        app.setNumReducer(1);

        Class[] cFunctions = {FileReaderCrawler.class};
        String[] cParams = {"-i " + input + "/Colosseum500m.json"};

        app.setCrawlers(cFunctions, cParams);
        Class[] fFunctions = {IsGeotagged.class, ContainsKeywords.class};
        String[] fParams = {" ", "-separator : -keywords " + colosseumSynonyms};
        app.setFilters(fFunctions, fParams);
        Class rFunction = ReduceByCoordinates.class;
        String rParams = "-t 5";

        app.setReduceFunction(rFunction, rParams);
        Class aFunction = ExtractRoIs.class;
        String aParams = "-minPts 150 -eps 30";
        app.setAnalysisFunction(aFunction, aParams);
        Class vFunction = RoIsToKML.class;
        String vParams = null;
        app.setVisualizationFunction(vFunction, vParams);
        return app.execute();
    }

}

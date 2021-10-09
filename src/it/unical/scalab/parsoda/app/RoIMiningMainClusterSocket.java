package it.unical.scalab.parsoda.app;

import it.unical.scalab.parsoda.acquisition.FileReaderCrawler;
import it.unical.scalab.parsoda.analysis.PrefixSpan;
import it.unical.scalab.parsoda.common.SocialDataApp;
import it.unical.scalab.parsoda.filtering.IsGeotagged;
import it.unical.scalab.parsoda.filtering.IsInPlace;
import it.unical.scalab.parsoda.mapping.FindPoI;
import it.unical.scalab.parsoda.reduction.ReduceByTrajectories;
import it.unical.scalab.parsoda.visualization.SortPrefixSpanBy;

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

            SocialDataApp app = new SocialDataApp("Sequential Pattern Mining - City of Rome");
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

            Files.lines(Paths.get(app.getOutputBasePath() + "-visualization/visualization-result.txt"))
                    .forEach(l -> {
                        out.println(l);
                    });

            in.close();
            out.close();

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }

    private static int runApp(SocialDataApp app, String input) {
        String[] cFiles = {"resources/RomeRoIs.kml"};
        app.setDistributedCacheFiles(cFiles);
        app.setLocatFileSystem();
        Class[] cFunctions = {FileReaderCrawler.class};
        String[] cParams = {"-i " + input};
        app.setCrawlers(cFunctions, cParams);
        Class[] fFunctions = {IsGeotagged.class, IsInPlace.class};
        String[] fParams = {" ", "-lng 12.492 -lat 41.890 -radius 10000"};
        app.setFilters(fFunctions, fParams);
        Class[] mFunctions = {FindPoI.class};
        String[] mParams = null;
        app.setMapFunctions(mFunctions, mParams);
        String groupKey = "USER.USERID";
        String sortKey = "DATETIME";
        app.setPartitioningKeys(groupKey, sortKey);
        Class rFunction = ReduceByTrajectories.class;
        String rParams = "-t 5";
        app.setReduceFunction(rFunction, rParams);
        Class aFunction = PrefixSpan.class;
        String aParams = "-maxPatternLength 5 -minSupport 0.005";
        app.setAnalysisFunction(aFunction, aParams);
        Class vFunction = SortPrefixSpanBy.class;
        String vParams = "-k support -d DESC";
        app.setVisualizationFunction(vFunction, vParams);
        return app.execute();
    }

}

package it.unical.scalab.parsoda.acquisition.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterObjectFactory;

public class StatusReader {
	
	
	static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");;
	

	public static void main(String[] args) {
		
		getMemUsage();
		
		
		try {
			Files.walk(Paths.get("./")).forEach(filePath -> {
				//usare .contains
			    if (Files.isRegularFile(filePath) && filePath.toString().endsWith(".txt")) {
			        System.out.print("Reading "+filePath);
			        
			        int count = 0;
			        int errors =0;
					BufferedReader br = null;

					try {
						br = new BufferedReader(new FileReader(filePath.toString()));

						Status tweet = null;
						String line;
						
						
						
						while ((line = br.readLine()) != null) {
							try {
								tweet = (Status) TwitterObjectFactory.createObject(line);
								count++;
							} catch (TwitterException e) {
								errors++;
							}
							
						}
						System.out.println("--> Tweets: "+count+" errors:" +errors+ " @"+dateFormat.format(new Date()));
						getMemUsage();
						
						br.close();
						System.gc();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} 
			        
			    }
			});
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public static void getMemUsage(){
		Runtime runtime = Runtime.getRuntime();
		long oneKb = 1024l;
		long oneMb = 1024*oneKb;
		  
		long maxMemory = runtime.maxMemory();  
		long allocatedMemory = runtime.totalMemory();  
		long freeMemory = runtime.freeMemory();  
		long totalFree = (freeMemory + (maxMemory - allocatedMemory));
		  
//		System.out.println("free memory: " + freeMemory / oneMb);  
		System.out.println("allocated memory: " + allocatedMemory / oneMb);  
//		System.out.println("max memory: " + maxMemory /oneMb);  
		System.out.println("total free memory: " + totalFree / oneMb);   
		System.out.println("FreePerCent:"+ (totalFree/new Long(maxMemory).doubleValue()*100));
		
	}
}

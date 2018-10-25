package it.unical.scalab.parsoda.acquisition.util;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;

import twitter4j.Status;
import twitter4j.TwitterObjectFactory;

public class StatusWriter {
	
	private String fileId;
	
	private PrintWriter pw;
	private Gson gson;
	public StatusWriter(String fileId){
		this.fileId=fileId;
		this.gson = new Gson();
		try {
//			pw = new PrintWriter(new FileOutputStream(fileId + ".txt"));
			
			pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileId), StandardCharsets.UTF_8));
			
			
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
	}
	

	public void write(TWTweet tweet){
		
		if (tweet != null)
			try {
				
				pw.println(gson.toJson(tweet));
				pw.flush();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		
	}
	
	
	public void write(Status tweet){
		
		if (tweet != null)
			try {
				
				pw.println(TwitterObjectFactory.getRawJSON(tweet));
				pw.flush();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		
	}
	
	
	public void writeGsonStatus(Status tweet){
		
		if (tweet != null)
			try {
				
				pw.println(gson.toJson(tweet));
				pw.flush();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		
	}
	

	public void writeString(String tw) {
		try {
			
			pw.println(tw);
			pw.flush();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}


	public void writeLite(Status tweet){
		
		if (tweet != null)
			try {
				
				pw.println(gson.toJson(new TWTweet(tweet)));
				pw.flush();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		
	}
	
	public void flush(){
		pw.flush();
	}
	
	public void close(){
		pw.close();
	}


	
}

package it.unical.scalab.parsoda.acquisition.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import twitter4j.RateLimitStatus;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TokenManager {

	private List<Token> tokens;
	private int start;
	private int startIndex;
	private int idleTokens;
	private boolean allIdle;
	private String apiType;
	
	
	public TokenManager(String apiType){
		start = 0;
		startIndex = 0;
		idleTokens = 0;
		allIdle = false;
		tokens = new LinkedList<Token>();
		this.apiType = apiType;
	}
	
	
	private void setRemainings(String apiType) {
		Token t;
		Twitter twitter;
		
		do{
			idleTokens=0;
			allIdle=false;
			for(int i = 0; i<tokens.size();i++){
				t = tokens.get(i);
				
				if(t.canCallRLS()){
					twitter = initializeTwitterObject();
					Map<String, RateLimitStatus> rateLimitStatus;
					try {
						if(apiType.equals(Constant.API_SEARCH)){
							rateLimitStatus = twitter.getRateLimitStatus(Constant.API_SEARCH);
							t.decreaseRateLimitRemainingCalls();
							RateLimitStatus rls = rateLimitStatus.get(Constant.API_SEARCH_TWEETS);
							t.setSearchRemaniningCalls(rls.getRemaining());
							t.setSearchSecondsUntilReset(rls.getSecondsUntilReset());
							DateTime ns = new DateTime();
							int delta = (15*60)-rls.getSecondsUntilReset();
							t.setSearchStartTime(ns.minusSeconds(delta));
						}else{
							rateLimitStatus = twitter.getRateLimitStatus(Constant.API_STATUSES);
							t.decreaseRateLimitRemainingCalls();
							RateLimitStatus rls = rateLimitStatus.get(Constant.API_USER_TIMELINE);
							t.setUserTimelineRemaniningCalls(rls.getRemaining());
							t.setUserTimelineSecondsUntilReset(rls.getSecondsUntilReset());
							DateTime ns = new DateTime();
							int delta = (15*60)-rls.getSecondsUntilReset();
							t.setUserTimelineStartTime(ns.minusSeconds(delta));
						}
						
					} catch (TwitterException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{
					idleTokens++;
					if(idleTokens==tokens.size()){
						allIdle=true;
						try {
							System.out.println("All token are idle - ");
							Thread.sleep(15*60*1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
			
		}while(allIdle);
		
		idleTokens=0;	
	}
	
	public Token getNextToken(){
      Token t;
      startIndex = start;
      Twitter twitter;
	  while(true){
	    t = tokens.get(start);
	    if(t.canCallRLS()){
	    	twitter = initializeTwitterObject();
			Map<String, RateLimitStatus> rateLimitStatus;
			try {
				if(apiType.equals(Constant.API_SEARCH)){
					rateLimitStatus = twitter.getRateLimitStatus(Constant.API_SEARCH);
					t.decreaseRateLimitRemainingCalls();
					RateLimitStatus rls = rateLimitStatus.get(Constant.API_SEARCH_TWEETS);
					t.setSearchRemaniningCalls(rls.getRemaining());
					t.setSearchSecondsUntilReset(rls.getSecondsUntilReset());
					DateTime ns = new DateTime();
					int delta = (15*60)-rls.getSecondsUntilReset();
					t.setSearchStartTime(ns.minusSeconds(delta));
					if(t.getSearchRemaniningCalls() > 5){
						return t;
					}else{
						start = (start + 1)%tokens.size();
				    	if(start == startIndex){
				    		try {
								Thread.sleep(15*60*1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
				    	}
					}
				}else{
					rateLimitStatus = twitter.getRateLimitStatus(Constant.API_STATUSES);
					t.decreaseRateLimitRemainingCalls();
					RateLimitStatus rls = rateLimitStatus.get(Constant.API_USER_TIMELINE);
					t.setUserTimelineRemaniningCalls(rls.getRemaining());
					t.setUserTimelineSecondsUntilReset(rls.getSecondsUntilReset());
					DateTime ns = new DateTime();
					int delta = (15*60)-rls.getSecondsUntilReset();
					t.setUserTimelineStartTime(ns.minusSeconds(delta));
					if(t.getUserTimelineRemaniningCalls() > 5){
						return t;
					}else{
						start = (start + 1)%tokens.size();
				    	if(start == startIndex){
				    		try {
								Thread.sleep(15*60*1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
				    	}
					}
				}
				
			} catch (TwitterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }else{
	    	start = (start + 1)%tokens.size();
	    	if(start == startIndex){
	    		try {
					Thread.sleep(15*60*1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	}
	    }
	  }
	}

	
	public boolean canQuery(){
		 Token t;
		  t = tokens.get(start);
		    if(t.canCallRLS()){
		    	if(apiType.equals(Constant.API_SEARCH)){
		    		if(t.getSearchRemaniningCalls() > 5){
		    			return true;
		    		}else{
		    			return t.checkRemaining(apiType);
		    		}
		    	}
		    	else{
		    		if(t.getUserTimelineRemaniningCalls() > 5){
		    			return true;
		    		}else{
		    			return t.checkRemaining(apiType);
		    		}
		    	}
		    }else{
		    	return false;
		    } 
	 }
	
	
	public void decrease(){
		Token t = tokens.get(start);
		if(apiType.equals(Constant.API_SEARCH)){
			t.decreaseSearchRemainingCalls();
		}else{
			t.decreaseUserTimelineRemainingCalls();
		}
	}
	
	public Twitter initializeTwitterObject() {
		Token t = tokens.get(start);
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true);
		cb.setJSONStoreEnabled(true)
				.setOAuthConsumerKey(t.getConsumerKey())
				.setOAuthConsumerSecret(t.getConsumerSecret())
				.setOAuthAccessToken(t.getAccessToken())
				.setOAuthAccessTokenSecret(t.getAccessTokenSecret());

		TwitterFactory tf = new TwitterFactory(cb.build());
		return tf.getInstance();
	}
	
	public TwitterStream initializeTwitterObjectStream() {
		Token t = tokens.get(start);
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true);
		cb.setJSONStoreEnabled(true)
				.setOAuthConsumerKey(t.getConsumerKey())
				.setOAuthConsumerSecret(t.getConsumerSecret())
				.setOAuthAccessToken(t.getAccessToken())
				.setOAuthAccessTokenSecret(t.getAccessTokenSecret());

		return new TwitterStreamFactory(cb.build()).getInstance();
	}

	
	public void addToken(String tokenName, String tokenConsumerKey, String tokenConsumerSecret, String tokenAccessToken, String tokenAccessTokenSecret){
		Token token = new Token(tokenName, tokenConsumerKey,
				tokenConsumerSecret, tokenAccessToken,
				tokenAccessTokenSecret);
		tokens.add(token);
		setRemainings(apiType);
	}
	
}

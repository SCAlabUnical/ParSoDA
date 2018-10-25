package it.unical.scalab.parsoda.acquisition.util;

public class Constant {
	
	public static final String TW_TOKENS_PROPERTIES = "tokens.properties";
	public static final String TOKEN_TW_PROPERTIES = "tokenTW.properties";
	public static final String TW_USERS_PROPERTIES = "users.properties";
		
	public static final String NAME = ".name";
	public static final String ID = ".id";
	public static final String SCREEN_NAME = ".screenName";
	
	public static final String CONSUMER_KEY = "consumerKey";
	public static final String CONSUMER_SECRET = "consumerSecret";
	public static final String ACCESS_TOKEN = "accessToken";
	public static final String ACCESS_TOKEN_SECRET = "accessTokenSecret";
	
	public static final String TWEETS = "_tweets_";
	public static final String REPLIES = "_replies_";
	
	public static final int TWEETS_PER_FILES = 10000;
	public static final int NUM_PER_FLUSH = 1;
	
	public static final String API_STATUSES = "statuses";
	public static final String API_USER_TIMELINE = "/statuses/user_timeline";
	public static final String API_STATUSES_SHOW = "/statuses/show";
	
	public static final String API_SEARCH = "search";
	public static final String API_SEARCH_TWEETS = "/search/tweets";
}

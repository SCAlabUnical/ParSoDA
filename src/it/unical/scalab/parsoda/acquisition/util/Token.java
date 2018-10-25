package it.unical.scalab.parsoda.acquisition.util;

import org.joda.time.DateTime;
import org.joda.time.Duration;

//classe che gestisce i token
public class Token {
	
	private static final int RATE_LIMIT_CALL = 180;
	private static final int SEARCH_CALL = 450;
	private static final int USER_TIMELINE_CALL = 1500;
	
	private static final String IDLE = "IDLE";
	private static final String ACTIVE = "ACTIVE";
	
	private String name;
	
	private String consumerKey;
	private String consumerSecret;
	private String accessToken;
	private String accessTokenSecret;
	
	private long searchRemainingCalls;
	private long searchSecondsUntilReset;
	private DateTime searchStartTime;
	
	private long rateLimitRemainingCalls;
	private long rateLimitSecondsUntilReset;
	private DateTime rateLimitStartTime;
	
	private long userTimelineRemainingCalls;
	private long userTimelineSecondsUntilReset;
	private DateTime userTimelineStartTime;
	
	private String status;
	
	public long getSearchRemaniningCalls() {
		return searchRemainingCalls;
	}

	public void setSearchRemaniningCalls(long searchRemaniningCalls) {
		this.searchRemainingCalls = searchRemaniningCalls;
	}

	public long getSearchSecondsUntilReset() {
		return searchSecondsUntilReset;
	}

	public void setSearchSecondsUntilReset(long searchSecondsUntilReset) {
		this.searchSecondsUntilReset = searchSecondsUntilReset;
	}

	public DateTime getSearchStartTime() {
		return searchStartTime;
	}

	public void setSearchStartTime(DateTime searchStartTime) {
		this.searchStartTime = searchStartTime;
	}

	public long getRateLimitRemaniningCalls() {
		return rateLimitRemainingCalls;
	}

	public void setRateLimitRemaniningCalls(long rateLimitRemaniningCalls) {
		this.rateLimitRemainingCalls = rateLimitRemaniningCalls;
	}

	public long getRateLimitSecondsUntilReset() {
		return rateLimitSecondsUntilReset;
	}

	public void setRateLimitSecondsUntilReset(long rateLimitSecondsUntilReset) {
		this.rateLimitSecondsUntilReset = rateLimitSecondsUntilReset;
	}

	public DateTime getRateLimitStartTime() {
		return rateLimitStartTime;
	}

	public void setRateLimitStartTime(DateTime rateLimitStartTime) {
		this.rateLimitStartTime = rateLimitStartTime;
	}

	
	public long getUserTimelineRemaniningCalls() {
		return userTimelineRemainingCalls;
	}

	public void setUserTimelineRemaniningCalls(long userTimelineRemaniningCalls) {
		this.userTimelineRemainingCalls = userTimelineRemaniningCalls;
	}

	public long getUserTimelineSecondsUntilReset() {
		return userTimelineSecondsUntilReset;
	}

	public void setUserTimelineSecondsUntilReset(long userTimelineSecondsUntilReset) {
		this.userTimelineSecondsUntilReset = userTimelineSecondsUntilReset;
	}

	public DateTime getUserTimelineStartTime() {
		return userTimelineStartTime;
	}

	public void setUserTimelineStartTime(DateTime userTimelineStartTime) {
		this.userTimelineStartTime = userTimelineStartTime;
	}

	public Token(String name, String consumerKey, String consumerSecret, String accessToken,
			String accessTokenSecret) {
		super();
		this.name=name;
		this.consumerKey = consumerKey;
		this.consumerSecret = consumerSecret;
		this.accessToken = accessToken;
		this.accessTokenSecret = accessTokenSecret;
		
		this.rateLimitRemainingCalls = RATE_LIMIT_CALL;
		this.searchRemainingCalls = SEARCH_CALL;
		this.userTimelineRemainingCalls = USER_TIMELINE_CALL;
		
		this.rateLimitStartTime = new DateTime();
		
		this.status = ACTIVE;
	}

	public Token() {
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getConsumerSecret() {
		return consumerSecret;
	}

	public void setConsumerSecret(String consumerSecret) {
		this.consumerSecret = consumerSecret;
	}

	public String getConsumerKey() {
		return consumerKey;
	}

	public void setConsumerKey(String consumerKey) {
		this.consumerKey = consumerKey;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getAccessTokenSecret() {
		return accessTokenSecret;
	}

	public void setAccessTokenSecret(String accessTokenSecret) {
		this.accessTokenSecret = accessTokenSecret;
	}

	public void resetRateLimit(){
		this.rateLimitRemainingCalls = RATE_LIMIT_CALL;
	}
	
	public void resetSearch(){
		this.searchRemainingCalls = SEARCH_CALL;
	}
	
	public void resetUserTimeline(){
		this.searchRemainingCalls = USER_TIMELINE_CALL;
	}

	
	public boolean isActive(){
		return this.status.equals(ACTIVE);
	}

	public void setActive(){
		this.status = ACTIVE;
	}
	
	public boolean isIdle(){
		return this.status.equals(IDLE);
	}
	
	public void setIdle(){
		this.status = IDLE;
	}
	
	public int getRateLimitRemaining(){
		DateTime now = new DateTime();
		Duration d = new Duration(rateLimitStartTime, now);
		
		if(d.getStandardMinutes()>15){
			rateLimitRemainingCalls = RATE_LIMIT_CALL;
			int offset = (int) (d.getStandardMinutes()/15);
			rateLimitStartTime.plusMinutes(offset*15);
			setActive();
			rateLimitSecondsUntilReset = 15*60;
			return 180;
			
		}else{
			rateLimitSecondsUntilReset = (15-d.getStandardMinutes())*60;
			if(rateLimitRemainingCalls<5){
				setIdle();
			}
			return (int) rateLimitRemainingCalls;
		}
	}
	
	public boolean checkRemaining(String apiType) {
		DateTime now = new DateTime();
		Duration d;
		//API_SEARCH
		if(apiType.equals(Constant.API_SEARCH)){

			d = new Duration(searchStartTime, now);
			
			if(d.getStandardMinutes()>15){
				searchRemainingCalls = SEARCH_CALL;
				int offset = (int) (d.getStandardMinutes()/15);
				searchStartTime.plusMinutes(offset*15);
				setActive();
				searchSecondsUntilReset = 15*60;
				return true;
				
			}else{
				searchSecondsUntilReset = (15-d.getStandardMinutes())*60;
				if(searchRemainingCalls<5){
					setIdle();
				}
				return false;
			}
		}
		//API USER_TIMELINE
		else{
			d = new Duration(userTimelineStartTime, now);
			
			if(d.getStandardMinutes()>15){
				userTimelineRemainingCalls = USER_TIMELINE_CALL;
				int offset = (int) (d.getStandardMinutes()/15);
				userTimelineStartTime.plusMinutes(offset*15);
				setActive();
				userTimelineSecondsUntilReset = 15*60;
				return true;
				
			}else{
				userTimelineSecondsUntilReset = (15-d.getStandardMinutes())*60;
				if(userTimelineRemainingCalls<5){
					setIdle();
				}
				return false;
			}
		}
		
	}
	
	public boolean canCallRLS(){
		return getRateLimitRemaining()>5;
	}
	
	public void decreaseRateLimitRemainingCalls(){
		this.rateLimitRemainingCalls--;
	}
	public void decreaseSearchRemainingCalls(){
		this.searchRemainingCalls--;
	}
	public void decreaseUserTimelineRemainingCalls(){
		this.userTimelineRemainingCalls--;
	}

	
}

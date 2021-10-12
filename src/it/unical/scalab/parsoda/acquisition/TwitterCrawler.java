package it.unical.scalab.parsoda.acquisition;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import it.unical.scalab.parsoda.common.AbstractCrawlerFunction;
import org.joda.time.Days;
import org.joda.time.LocalDateTime;

import it.unical.scalab.parsoda.acquisition.util.Constant;
import it.unical.scalab.parsoda.acquisition.util.StatusWriter;
import it.unical.scalab.parsoda.acquisition.util.Token;
import it.unical.scalab.parsoda.acquisition.util.TokenManager;
import twitter4j.GeoLocation;
import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterCrawler extends AbstractCrawlerFunction {

	SimpleDateFormat dateFormat;
	SimpleDateFormat dateOutput;

	Date from;
	Date to;

	List<Token> tokens;
	int tokenIndex;

	ConfigurationBuilder cb;
	TwitterFactory tf;
	Twitter twitter;
	TwitterStream twitterStream;
	Paging pg;

	int counterTweets = 0;
	int counterNoRes = 0;
	int counterException = 0;
	boolean stop = false;
	long maxId;
	long sinceId;
	long lastId = Long.MAX_VALUE;

	List<Status> statuses = new LinkedList<Status>();

	List<String> users;

	StatusWriter sw;

	String workingDir;
	String usersFile;
	String datesFile;
	String tokenPath;
	HashSet<Long> tweetsId = new HashSet<Long>();

	private TokenManager tm;

	public TwitterCrawler() {
		super();
	}

	public TwitterCrawler(String params) {
		super(params);
	}

	@Override
	public void collect() {

		System.setProperty("file.encoding", "UTF8");

		dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		workingDir = this.getOutputPath();
		tokenPath = workingDir;

		try {
			from = dateFormat.parse(this.options.getString("startDate"));
			to = dateFormat.parse(this.options.getString("endDate"));
			LocalDateTime toLDT = new LocalDateTime(to.getTime());
			LocalDateTime now =new LocalDateTime();
			if(toLDT.isBefore(now) && Days.daysBetween(toLDT, now).getDays() > 7) {
				to = new Date();
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		tm = new TokenManager(Constant.API_SEARCH);
		tm.addToken(this.options.getString("tokenName"), this.options.getString("tokenConsumerKey"),
				this.options.getString("tokenConsumerSecret"), this.options.getString("accessToken"),
				this.options.getString("accessTokenSecret"));
		tokenIndex = 0;

		startCrawling();
	}

	private void startCrawling() {

		counterTweets = 0;
		counterNoRes = 0;
		String fileName = String.format("To_%1$02d-%2$02d", to.getMonth() + 1, to.getDate());
		sw = new StatusWriter(workingDir + "/" + fileName + "_tweets.txt");

		pg = new Paging(1, 200);
		lastId = Long.MAX_VALUE;

		standardRetrieve();
		sw.close();
	}

	private void standardRetrieve() {
		QueryResult result;
		Query query = new Query();
		boolean checkGeo = false;
		if (this.options.exists("lat") && this.options.exists("lng") && this.options.exists("radius")) {
			// Geo query from user profile
			query.geoCode(new GeoLocation(this.options.getDouble("lat"), this.options.getDouble("lng")),
					this.options.getDouble("radius") * 1000, "km");
			checkGeo = true;
		}
		do {
			System.out.println("Querying...");
			stop = false;
			// ********************SETTING QUERY********************
			counterTweets = 0;
			tm.getNextToken();
			twitter = tm.initializeTwitterObject();
			do {
				try {

					if (!tm.canQuery()) {
						tm.getNextToken();
						twitter = tm.initializeTwitterObject();
					}

					result = twitter.search(query);
					statuses = result.getTweets();

					tm.decrease();

					for (Status tweet : statuses) {
						if (tweet.getId() == maxId) {
							stop = true;
							break;
						}

						if (tweet.getId() < lastId)
							lastId = tweet.getId();

  						if (tweet.getCreatedAt().after(from) && tweet.getCreatedAt().before(to)
								&& !tweetsId.contains(tweet.getId())) {
							if (checkGeo && tweet.getGeoLocation() == null) {
									break;
							} else {
								tweetsId.add(tweet.getId());
								counterTweets++;
								System.out.println(tweet);
								sw.writeLite(tweet);
							}
						}
						if ((tweet.getCreatedAt().before(from))) {
							stop = true;
							break;
						}
						maxId = tweet.getId();

					}
					pg.setMaxId(lastId - 1);
				} catch (TwitterException te) {

					if (te.toString().contains(
							"Failed to search tweets: 404:The URI requested is invalid or the resource requested, such as a user, does not exists")) {
						sw.close();
						return;
					}

					try {
						Thread.sleep(counterException * 10000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					counterException++;
					if (counterException > 20) {
						stop = true;
						break;
					}
					System.out.println("Failed to search tweets: " + te.getMessage());

				}
			} while (statuses.size() != 0 && !stop);
			sw.flush();

			System.out.println("Num. tweets " + counterTweets);
			if (counterTweets <= 1) {
				counterNoRes++;
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} while (counterNoRes < 2);
	}

}

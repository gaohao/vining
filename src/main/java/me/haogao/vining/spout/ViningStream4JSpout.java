package me.haogao.vining.spout;

import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.URLEntity;
import twitter4j.conf.ConfigurationBuilder;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

public class ViningStream4JSpout extends BaseRichSpout {
	private static final long serialVersionUID = 5173509952980902144L;
	private static Logger logger = null;
	private LinkedBlockingQueue<Status> queue = null;
	private final int queueCapacity = 1024;
	private SpoutOutputCollector collector = null;
	private Twitter twitter = null;
	private String consumerKey = null;
	private String consumerSecret = null;
	private String accessToken = null;
	private String accessTokenSecret = null;
	
	private static Pattern p =  Pattern.compile(".*vine.co");

	@SuppressWarnings("static-access")
	public ViningStream4JSpout(String consumerKey, String consumerSecret,
			String accessToken, String accessTokenSecret) {
		this.consumerKey = consumerKey;
		this.consumerSecret = consumerSecret;
		this.accessToken = accessToken;
		this.accessTokenSecret = accessTokenSecret;
		this.queue = new LinkedBlockingQueue<Status>(this.queueCapacity);
		this.logger = LogManager.getLogger(ViningStream4JSpout.class.getName());
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void open(Map conf, TopologyContext context,
			SpoutOutputCollector collector) {
		ConfigurationBuilder builder = new ConfigurationBuilder();
		builder.setOAuthConsumerKey(this.consumerKey);
		builder.setOAuthConsumerSecret(this.consumerSecret);
		builder.setOAuthAccessToken(this.accessToken);
		builder.setOAuthAccessTokenSecret(this.accessTokenSecret);
		builder.setJSONStoreEnabled(true);
		TwitterFactory factory = new TwitterFactory(builder.build());
		
		this.twitter = factory.getInstance();

		this.collector = collector;
	}

	@Override
	public void nextTuple() {
		try {
			Query query = new Query("vine.co/v/");
	        QueryResult result;
	       
	        do {
	            result = this.twitter.search(query);
	            List<Status> tweets = result.getTweets();
	            for (Status tweet : tweets) {
	            	this.collector.emit(new Values(tweet));
	                System.out.println("@" + tweet.getUser().getScreenName() + " - " + tweet.getText());
	            }
	        } while ((query = result.nextQuery()) != null);
			
			Utils.sleep(200);
			
			
		}catch (TwitterException te) {
            
        }
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("link"));
	}

	@Override
	public void close() {

	}
}

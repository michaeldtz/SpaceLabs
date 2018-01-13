package com.md.spacelabs.jsservices;

import org.mozilla.javascript.ScriptableObject;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

@SuppressWarnings("serial")
public class TweetLoaderService extends ScriptableObject {

	public TweetLoaderService() {
		String[] functionNames = { "loadTweetsForTopicSince" };
		defineFunctionProperties(functionNames, TweetLoaderService.class, ScriptableObject.DONTENUM);
	}

	public void loadTweetsForTopicSince(String topic, String sinceDate) {

		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setOAuthConsumerKey("2W3Fm7i2fxZTE5TiCIcP8G5T2");
		cb.setOAuthConsumerSecret("I3sooP76OkssiLiR4u1rOBjnn2BT5IDfMJaVx321NXwN75lXYB");
		cb.setOAuthAccessToken("197865590-qW61kQgTQuNCbGjk9ii5mzIYT8ezYPohvYA5i5l8");
		cb.setOAuthAccessTokenSecret("O87ELVrcXDkYd60QzhdL30ZS7fViexJSiYqTH51y1JqeG");

		Twitter twitter = new TwitterFactory(cb.build()).getInstance();
		Query query = new Query(topic);
		query.setSince(sinceDate);
		query.setCount(10);
		try {

			QueryResult result = twitter.search(query);
		System.out.println("Fetching Twitter Results");
			for (Status status : result.getTweets()) {
				System.out.println("@" + status.getUser().getScreenName() + ":" + status.getText());
			}

		} catch (TwitterException e) {

			e.printStackTrace();
		}
	}


	@Override
	public String getClassName() {

		return "TweetLoaderService";
	}
}

package com.twitter.producer.service;import com.twitter.producer.service.utils.TwitterStringsUtils;import org.slf4j.Logger;import org.slf4j.LoggerFactory;import org.springframework.social.twitter.api.*;import org.springframework.stereotype.Service;import java.util.ArrayList;import java.util.List;import java.util.Set;@Servicepublic class TwitterStreamingService {    private final Logger log = LoggerFactory.getLogger(TwitterStreamingService.class);    private final Twitter twitter;    public TwitterStreamingService(Twitter twitter) {        this.twitter = twitter;    }    public void stream(){        List<StreamListener> listeners = new ArrayList<>();        StreamListener streamListener = new StreamListener() {            @Override            public void onTweet(Tweet tweet) {                String tweetLanguageCode = tweet.getLanguageCode();                String tweetText = tweet.getText();                //filter non-English tweets:                if (!"en".equals(tweetLanguageCode)) {                    return;                }                Set<String> hashTags = TwitterStringsUtils.tweetToHashTags(tweetText);                // filter tweets without hashTags:                if (hashTags.isEmpty()) {                    return;                }                // logging Real Time Tweets                log.info("User '{}', Tweeted : {}, from ; {}", tweet.getUser().getName() , tweet.getText(), tweet.getUser().getLocation());            }            @Override            public void onDelete(StreamDeleteEvent streamDeleteEvent) {            }            @Override            public void onLimit(int i) {            }            @Override            public void onWarning(StreamWarningEvent streamWarningEvent) {            }        };        //Start Stream when run a service        listeners.add(streamListener);        twitter.streamingOperations().sample(listeners);    }}
package planner.v1;

import TurnByTurn.Step;/*
import twitter4j.DirectMessage;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;*/

import java.util.LinkedList;

/**
 * Created by yx on 12/7/15.
 */
public class TwitterSender {
    /*public static void sendToTwitter(String m,ConfigurationBuilder cb){
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        try {
            DirectMessage message = twitter.sendDirectMessage("@xy578772313",m);
            System.out.println("DM sent!");
        } catch (TwitterException te){
            te.printStackTrace();
            System.out.println("Failed to send a DM!");
        }
    }

    public static void sendDirections(LinkedList<Step> steps){
        ConfigurationBuilder cb = new ConfigurationBuilder();
        /**
         * Fill in developer info
         *
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("fKWNUcMOydbBnAzMQGK24ZsT5")
                .setOAuthConsumerSecret("CmwMQmFV2cH3ja0Q6UBSM9sUs3cOqas50ev5qZrQ9ZElIKOWNN")
                .setOAuthAccessToken("2896937116-Cd9SF2NMvZ1GPCHU1wuYbQsQNjnYnr5sCkKspWp")
                .setOAuthAccessTokenSecret("UO0x4zHnoqbXtom9iCZWDZnie3gfPposJZbwmYTXWqnVz");

        String message = "";
        for (Step s:steps){
            message = message + s.getMessage()+'\n';
        }
        sendToTwitter(message,cb);
    }*/
}

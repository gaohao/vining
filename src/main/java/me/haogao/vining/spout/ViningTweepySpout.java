package me.haogao.vining.spout;

import backtype.storm.spout.ShellSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.IRichSpout;
import backtype.storm.tuple.Fields;
import java.util.Map;

public class ViningTweepySpout extends ShellSpout implements IRichSpout {
    
	private static final long serialVersionUID = 7060865252243357618L;

	public ViningTweepySpout(String para) {
        
            super("python", "vining_tweepy_spout.py",para);
        
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
    	declarer.declare(new Fields("tweet_id", "tweet_createdat", "link", "tweet"));
    }
    
    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
}

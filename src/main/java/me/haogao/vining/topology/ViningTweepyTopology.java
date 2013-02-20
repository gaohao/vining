package me.haogao.vining.topology;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import me.haogao.vining.bolt.*;
import me.haogao.vining.spout.*;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;

public class ViningTweepyTopology {
    
    public static void main(String[] args) throws Exception {
		Logger logger = LogManager.getLogger(ViningTweepyTopology.class.getName());
    	logger.entry();
    	
    	
        TopologyBuilder builder = new TopologyBuilder();
        
        builder.setSpout("spout", new ViningTweepySpout(), 1);
        
        builder.setBolt("bolt", new ViningCacheBolt("localhost", 6379), 12)
                 .fieldsGrouping("spout", new Fields("tweet_id"));

        Config conf = new Config();
        conf.setDebug(true);
        
        if(args!=null && args.length > 0) {
            conf.setNumWorkers(3);
            StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
        } else {        
            conf.setMaxTaskParallelism(3);
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("vining", conf, builder.createTopology());
            
            //Thread.sleep(10000);
            //cluster.killTopology("vining");
            //cluster.shutdown();
        }  
		logger.exit();
    }
}
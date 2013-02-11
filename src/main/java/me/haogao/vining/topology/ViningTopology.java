package me.haogao.vining.topology;

import me.haogao.vining.spout.ViningSpout;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.task.ShellBolt;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import java.util.HashMap;
import java.util.Map;

public class ViningTopology {
    
    public static class DefaultBolt extends BaseBasicBolt {
        Map<String, Integer> counts = new HashMap<String, Integer>();

        @Override
        public void execute(Tuple tuple, BasicOutputCollector collector) {
            String link = tuple.getString(0);
            System.out.println(link);
            //collector.emit(new Values(event, 0));
        }

        @Override
        public void declareOutputFields(OutputFieldsDeclarer declarer) {
            declarer.declare(new Fields("link"));
        }
    }
    
    public static void main(String[] args) throws Exception {
        
        TopologyBuilder builder = new TopologyBuilder();
        
        builder.setSpout("spout", new ViningSpout(), 1);
        
        builder.setBolt("bolt", new DefaultBolt(), 12)
                 .fieldsGrouping("spout", new Fields("link"));

        Config conf = new Config();
        conf.setDebug(true);

        
        if(args!=null && args.length > 0) {
            conf.setNumWorkers(3);
            
            StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
        } else {        
            conf.setMaxTaskParallelism(3);

            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("vining", conf, builder.createTopology());
        
            Thread.sleep(100000);

            cluster.shutdown();
        }
    }
}
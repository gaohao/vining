package me.haogao.vining.spout;

import backtype.storm.spout.ShellSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.IRichSpout;
import backtype.storm.tuple.Fields;
import java.util.Map;

public class ViningSpout extends ShellSpout implements IRichSpout {
    
    public ViningSpout() {
        super("python", "vining_spout.py");
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("link"));
    }
    
    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
}
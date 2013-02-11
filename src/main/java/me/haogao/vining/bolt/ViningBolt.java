package me.haogao.vining.bolt;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class ViningBolt extends BaseRichBolt{

	private static final long serialVersionUID = 5161826509198496313L;
	private OutputCollector collector = null;
	
	@SuppressWarnings("rawtypes")
	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		this.collector = collector;
	}

	@Override
	public void execute(Tuple input) {
		System.out.println(input.getValue(0));
		this.collector.emit(new Values(input.getValue(0)));
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("link"));
	}
	
}


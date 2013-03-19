package me.haogao.vining.bolt;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonSyntaxException;


public class ViningHashBolt extends BaseRichBolt{

	private static final long serialVersionUID = 5161826509198496313L;
	private String host;
	private int port;
	private JedisPool jedisPool = null;
	private Jedis jedis= null;
	private static Logger logger;
	
	public ViningHashBolt(String host, int port) {
		this.host = host;
		this.port = port;
	}
	@SuppressWarnings("rawtypes")
	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		this.jedisPool = new JedisPool(this.host, this.port);
		this.jedis = jedisPool.getResource();
		logger = LogManager.getLogger(ViningHashBolt.class.getName());
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void execute(Tuple input) {
		String id = (String) input.getValue(0);
		String created_at = (String) input.getValue(1);
		String link = (String) input.getValue(2);
		String tweet = (String) input.getValue(3);
		
		String ISO_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat(ISO_FORMAT);
		TimeZone utc = TimeZone.getTimeZone("UTC");
		sdf.setTimeZone(utc);

		this.jedis.select(1);
		try {
			Date date = sdf.parse(created_at);
		
			JsonElement jsone = new JsonParser().parse(tweet);
			JsonObject jsonObject = jsone.getAsJsonObject();
			jsonObject.remove("created_at");
			jsonObject.addProperty("link", link);
			jsonObject.addProperty("created_at", created_at);
			String json = new Gson().toJson(jsonObject);
			
			//Get the hashtag Array
			JsonArray jarray = (JsonArray)((JsonObject)jsonObject.get("entities")).get("hashtags");	
			if (jarray.size() == 0) {
				this.jedis.hset("", id, json);
			}else {
				for(JsonElement jseit: jarray) {
					this.jedis.hset(jseit.getAsJsonObject().get("text").getAsString(), id, json);
				}
			}								
			
			String key = "vine:link:realtime";
			this.jedis.zadd(key, date.getTime(), id);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		
	}
	
	@Override
	public void cleanup() {
		if (this.jedisPool != null ) {
			this.jedisPool.returnResource(this.jedis);
			this.jedisPool.destroy();
		}
	}
	
}


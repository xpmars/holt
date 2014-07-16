package ca.dealsaccess.holt.storm.bolt;

import java.util.Map;

import ca.dealsaccess.holt.common.RedisConstants;
import redis.clients.jedis.Jedis;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

@SuppressWarnings("serial")
public class LogRedisBolt extends BaseRichBolt {
	
	private Jedis jedis;
	
    private String host;
    
    private int port;

	@SuppressWarnings("rawtypes")
	@Override
	public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
		
		host = conf.get(RedisConstants.REDIS_HOST).toString();
        port = Integer.valueOf(conf.get(RedisConstants.REDIS_PORT).toString());
		jedis = new Jedis(host, port);

	}

	@Override
	public void execute(Tuple input) {
		String logText = input.getString(0);
		jedis.rpush("log", logText);
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {

	}
}
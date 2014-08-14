package ca.dealsaccess.holt.redis;

import java.util.Map;

import redis.clients.jedis.Jedis;
import ca.dealsaccess.holt.log.ApacheLogEntry;
import ca.dealsaccess.holt.log.LogConstants;

public class RedisCnxn {

	public static final String LOGGING = "Logging";

	public static final String COUNTER = "Counter";
	
	public static final String IP_TABLE = "IP_TABLE";
	
	private Jedis jedis;
	
	private ApacheLogEntry entry;
	
	private String duration;
	
	
	private static final String delimiter = "$#$";
	
	private static final String ONE = "1";
	
	public RedisCnxn(Jedis jedis) {
		this.jedis = jedis;
	}

	public RedisCnxn(Jedis jedis, ApacheLogEntry entry, String duration) {
		this.jedis = jedis;
		this.entry = entry;
		this.duration = duration;
	}

	private StringBuilder sb = new StringBuilder();
	
	public String getRowKeyValue() {
		String value = null;
		if(duration.equals(LogConstants.MINUTE)) {
			value = entry.getMinuteForTime().toString();
		} else if(duration.equals(LogConstants.HOUR)) {
			value = entry.getHourForTime().toString();
		} else if(duration.equals(LogConstants.DAY)) {
			value = entry.getDayForTime().toString();
		} else if(duration.equals(LogConstants.WEEK)) {
			value = entry.getWeekForTime().toString();
		} else if(duration.equals(LogConstants.MONTH)) {
			value = entry.getMonthForTime().toString();
		} else if(duration.equals(LogConstants.YEAR)) {
			value = entry.getYearForTime().toString();
		}
		return value;
	}
	
	
	public void persistence() {
		
		String[] columnArray = ((ApacheLogEntry) entry).getIPNoneExistsArray();
		String rowKey = getRowKeyValue();
		
		for(int i=0;i<LogConstants.LOG_COLUMNS.length;i++) {
			String innerKey = columnArray[i];
			String key = sb.append(duration).append(delimiter).append(rowKey).append(delimiter).append(LogConstants.LOG_COLUMNS[i]).toString();
			sb.setLength(0);
			Map<String, String> dataMap = jedis.hgetAll(key);
			if(!dataMap.containsKey(innerKey)) {
				dataMap.put(innerKey, ONE);
			} else {
				dataMap.put(innerKey, Integer.valueOf((Integer.parseInt(dataMap.get(innerKey))+1)).toString());
			}
			jedis.hmset(key, dataMap);
			
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
	}
	
	
	
}

package com.ggj.db.util;

/**
 * 分布式id生成器
 * 集群需要保证时间同步,workerId和datacenterId都不一致
 * nowFlake的优点是，整体上按照时间自增排序，并且整个分布式系统内不会产生ID碰撞(由数据中心ID和机器ID作区分)，并且效率较高，经测试，SnowFlake每秒能够产生26万ID左右。
 * 参考：https://github.com/adyliu/idcenter
 * @author:gaoguangjin
 * @date 2017/2/22 16:59
 */
public class IdWorker {
	
	
	/** 机器id所占的位数 */
	private static final long workerIdBits = 5L;
	
	/** 数据标识id所占的位数 */
	private static final long datacenterIdBits = 5L;
	
	/** 支持的最大机器id，结果是31 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数) */
	private static final long maxWorkerId = -1L ^ (-1L << workerIdBits);
	
	/** 支持的最大数据标识id，结果是31 */
	private static final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);
	
	private static final long sequenceBits = 12L;
	
	private static final long workerIdShift = sequenceBits;
	
	private static final long datacenterIdShift = sequenceBits + workerIdBits;
	
	private static final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;
	
	private static final long sequenceMask = -1L ^ (-1L << sequenceBits);
	
	private final long workerId;// 机器标识
	
	private final long datacenterId;// 数据中心
	
	private final long idepoch = System.currentTimeMillis() - 3600 * 1000L;// 当前时间戳
	
	private long lastTimestamp = -1L;// 上一次时间戳
	
	private long sequence = 0;// 序列号
	
	public IdWorker(long workerId, long datacenterId) {
		this.workerId = workerId;
		this.datacenterId = datacenterId;
		if (workerId < 0 || workerId > maxWorkerId) {
			throw new IllegalArgumentException("workerId is illegal: " + workerId);
		}
		if (datacenterId < 0 || datacenterId > maxDatacenterId) {
			throw new IllegalArgumentException("datacenterId is illegal: " + workerId);
		}
		if (idepoch >= System.currentTimeMillis()) {
			throw new IllegalArgumentException("idepoch is illegal: " + idepoch);
		}
	}
	
	public long getId() {
		long id = nextId();
		return id;
	}
	
	private synchronized long nextId() {
		long timestamp = timeGen();
		if (timestamp < lastTimestamp) {
			throw new IllegalStateException("Clock moved backwards.");
		}
		if (lastTimestamp == timestamp) {
			sequence = (sequence + 1) & sequenceMask;
			if (sequence == 0) {
				timestamp = tilNextMillis(lastTimestamp);
			}
		} else {
			sequence = 0;
		}
		lastTimestamp = timestamp;
		long id = ( (timestamp - idepoch) << timestampLeftShift)//
				| (datacenterId << datacenterIdShift)//
				| (workerId << workerIdShift)//
				| sequence;
		return id;
	}
	
	private long tilNextMillis(long lastTimestamp) {
		long timestamp = timeGen();
		while(timestamp <= lastTimestamp) {
			timestamp = timeGen();
		}
		return timestamp;
	}
	
	private long timeGen() {
		return System.currentTimeMillis();
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("IdWorker{");
		sb.append("workerId=").append(workerId);
		sb.append(", datacenterId=").append(datacenterId);
		sb.append(", idepoch=").append(idepoch);
		sb.append(", lastTimestamp=").append(lastTimestamp);
		sb.append(", sequence=").append(sequence);
		sb.append('}');
		return sb.toString();
	}
}

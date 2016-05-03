package com.ggj.encrypt.common.persistence;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author:gaoguangjin
 * @Description:
 * @Email:335424093@qq.com
 * @Date 2016/4/27 16:52
 */
@Getter
@Setter
public class ApiUserToken implements Serializable {
	private static final long serialVersionUID = 1640649351444893129L;
	private Long id;
	private String token;
	private Date createTime;
	private Date validTime;
	private Date lastVisitTime;
	private Long visitCounter;
	private String appkey;
	private Long userId;
	private String userEmail;
	private Long delayTime;
	private String deviceId;
	private String deviceToken;
}

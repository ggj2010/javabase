package com.ggj.java.distributetask.core.job;

import lombok.Getter;

/**
 * @author:gaoguangjin
 * @date 2016/9/2 17:47
 */
public enum JobStatu {
	RUN(0),
    STOP(1),
    PAUSE(3);
    @Getter
    int status;
	
	JobStatu(int status) {
		this.status = status;
	}
	public static JobStatu valueOf(int status) {
		switch(status) {
			case 1:
				return RUN;
			case 2:
				return STOP;
			default:
				return STOP;
		}
	}
}

package com.ggj.encrypt.common.filter;

import javax.servlet.DispatcherType;
import javax.servlet.annotation.WebFilter;

import com.dianping.cat.servlet.CatFilter;

/**
 * 大众点评cat客户端接入
 * @author:gaoguangjin
 * @date 2016/7/8 10:33
 */
@WebFilter(filterName="catclient-filter",urlPatterns="/*" ,dispatcherTypes={DispatcherType.REQUEST,DispatcherType.FORWARD})
public class CatFilters extends CatFilter {
}

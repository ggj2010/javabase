package com.ggj.encrypt.modules.base.controller;

import java.util.Date;

import com.ggj.encrypt.common.exception.BizException;
import com.ggj.encrypt.common.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


/**
 * @author:gaoguangjin
 * @Description:
 * @Email:335424093@qq.com
 * @Date 2016/4/25 13:54
 */
@RestController
@RequestMapping("/mobile")
@Slf4j
public class RestTemplateController extends BaseController{
	
	@RequestMapping("/test/{sign}/{id}")
	public String restTemplateClient(@PathVariable("sign") String sign,@PathVariable("id") String id) {
		return sign+id;
	}
	@RequestMapping("/exception/{type}")
	public String testException(@PathVariable("type")int type) throws Exception {
		if(type==1) {
			throw new Exception("异常");
		}else{
			throw  new BizException("111","测试BizException");
		}
	}




//	public static void main(String[] args) {
//      // String date= DateUtils.formatDate(new Date((System.currentTimeMillis())),"yyyy-MM-dd HH:mm:ss");
//       String date= DateUtils.formatDate(new Date(1425860757000l),"yyyy-MM-dd HH:mm:ss");
//        System.out.println(date);
//    }

}

package com.ggj.encrypt.modules.sys.controller;

import java.util.ArrayList;
import java.util.List;

import com.ggj.encrypt.modules.base.controller.BaseController;
import com.ggj.encrypt.modules.sys.bean.UserInfo;
import com.ggj.encrypt.modules.sys.bean.XMLBean;
import com.ggj.encrypt.modules.sys.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author:gaoguangjin
 * @Description:
 * @Email:335424093@qq.com
 * @Date 2016/3/29 22:46
 */
@RestController
@RequestMapping("user")
@Api(basePath = "/user", value = "用户管理API", description = "用户相关", produces = "application/json")
@Slf4j
public class UserController extends BaseController {
	@Autowired
	private UserInfoService userInfoService;

	// 这里指定RequestMethod，如果不指定Swagger会把所有RequestMethod都输出，在实际应用中，具体指定请求类型也使接口更为严谨。
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ApiOperation(value = "查询所有", notes = "查询所有用户信息")
	public String list() throws Exception {
		return resultCodeConfiguration.getResult().addData( userInfoService.findAll());
	}

	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	@ApiOperation(value = "根据ID获取用户", notes = "这是描述啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊！")
	public UserInfo getuserById(@PathVariable("id") Integer id) throws Exception {
		return userInfoService.getUserInfo(id);
	}

	// 使用此注解标示这个接口不在api显示
	@ApiIgnore
	@RequestMapping(value = "/apiIgnore", method = RequestMethod.GET)
	public List<String> apiIgnore() throws Exception {
		List<String> list = new ArrayList<String>();
		list.add("高");
		list.add("广");
		list.add("金");
		return list;
	}

	@ApiOperation(value = "测试返回格式为xml类型数据", notes = "通过HttpMessageConverters 可以返回不同类型的数据")
	@RequestMapping(value = "/xmltest", method = RequestMethod.GET)
	public XMLBean backXmlStyle()throws Exception  {
		return new XMLBean("1", "http://localhost:8080/user/xmltest.xml  返回xml格式,http://localhost:8080/user/xmltest.json");
	}

	@ApiOperation(value = "保存用户信息", notes = "接受json格式的字符串)", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "商品信息"), @ApiResponse(code = 201, message = "(token验证失败)", response = String.class),
			@ApiResponse(code = 202, message = "(系统错误)", response = String.class) })
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String getJsonResult(@ApiParam(value = "Json参数", required = true) @RequestBody UserInfo user) throws Exception {
        userInfoService.addUserInfo(user);
		return resultCodeConfiguration.getSuccessResultCode();
	}


}

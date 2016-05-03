package com.ggj.encrypt.modules.sys.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ggj.encrypt.modules.base.controller.BaseController;

/**
 * @author:gaoguangjin
 * @Description:
 * @Email:335424093@qq.com
 * @Date 2016/5/3 11:22
 */
@RestController
@RequestMapping("/mobile/order")
@Slf4j
public class OrderController extends BaseController {
    @RequestMapping("/orderList/{currentPage}/{pageSize}")
    public String orderList(@PathVariable("currentPage")int currentPage,@PathVariable("pageSize")int pageSize) throws Exception {

        return "一大波数据啊啊啊啊啊啊啊啊啊啊啊啊啊啊";
    }


}

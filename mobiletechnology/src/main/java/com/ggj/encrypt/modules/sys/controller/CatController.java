package com.ggj.encrypt.modules.sys.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Message;
import com.dianping.cat.message.Transaction;
import com.ggj.encrypt.configuration.RedisKeyConfiguration;
import com.ggj.encrypt.modules.base.controller.BaseController;
import com.ggj.encrypt.modules.sys.bean.UserInfo;
import com.ggj.encrypt.modules.sys.service.CatAnnationMethodService;
import com.ggj.encrypt.modules.sys.service.CatAnnationService;
import com.ggj.encrypt.modules.sys.service.CatCacheAnnationMethodService;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

/**
 * 测试大众点评cat
 *CAT消息协议
 Timestamp	记录消息产生的时刻, 格式"yyyy-mm-dd HH:MM:SS.sss".
 Type	大小写敏感的字符串. 常见的Transaction type有 "URL", "SQL", "Email", "Exec"等. 常见的Event type有 "Info", "Warn", "Error", 还有"Cat"用来表示Cat内部的消息.
 Name	大小写敏感的字符串. type和name的组合要满足全局唯一性. 常见的URL transaction type的name如 "ViewItem", "MakeBid", "SignIn"等. SQL transaction type的name如 "AddFeedback", "GetAccountDetailUnit4", "IncrementFeedbackAndTotalScore"等.
 Status	大小写敏感的字符串. 0表示成功, 非零表示失败. 建议不要使用太长的字符串. Transaction start没有status字段.
 Duration	精确到0.1毫秒. 表示transaction start和transaction end之间的时间长度. 仅出现在Transaction end或者Atomic Transaction. Event和Heartbeat没有duration字段.
 Data	建议使用以&字符分割的name=value对组成的字符串列表. Transaction start没有data字段.
 * @author:gaoguangjin
 * @date 2016/7/8 14:13
 */
@RestController
@RequestMapping("/catclient/test")
@Api(basePath = "/catclient/test", value = "catclient", description = "大众点评cat测试", produces = "application/json")
@Slf4j
public class CatController extends BaseController {
	
	
	// 业务指标,每个指标都有一个String作为它的唯一KEY，这个KEY在整个产品线中，不能重复
	private final String BUSSINESS_KEY_ORDERCOUNT = "OrderCount";
	
	private final String BUSSINESS_KEY_PAYCOUNT = "PayCount";
	
	@Autowired
	private CatAnnationService catAnnationService;
	
	@Autowired
	private CatAnnationMethodService catAnnationMethodService;
	
	@Autowired
	private CatCacheAnnationMethodService catCacheAnnationMethodService;
	
	@Autowired
	private RedisKeyConfiguration redisKeyConfiguration;
	
	private AtomicInteger orderAtomicInteger = new AtomicInteger();
	
	private AtomicInteger payAtomicInteger = new AtomicInteger();
	
	@RequestMapping(value = "/event/{type}/{content}", method = RequestMethod.GET)
	public String event(@PathVariable("type") String type, @PathVariable("content") String content) throws Exception {
		Map<String, String> map = new HashMap<String, String>() {
			
			
			{
				put("type", type);
				put("content", content);
			}
		};
		return resultCodeConfiguration.getResult().addData(map);
	}
	
	@RequestMapping(value = "/ping", method = RequestMethod.POST)
	public String event() throws Exception {
		return "";
	}
	
	// 业务代码埋点
	@RequestMapping(value = "/point/order", method = RequestMethod.GET)
	public String order() throws Exception {
		// 埋点 订单次数
		Cat.logMetricForCount(BUSSINESS_KEY_ORDERCOUNT);
		return "order埋点次数：" + orderAtomicInteger.incrementAndGet();
	}
	
	// 业务代码埋点
	@RequestMapping(value = "/point/pay", method = RequestMethod.GET)
	public String pay() throws Exception {
		// 埋点 支付次数
		Cat.logMetricForCount(BUSSINESS_KEY_PAYCOUNT);
		return "pay：" + payAtomicInteger.incrementAndGet();
	}
	
	// 业务代码埋点
	@RequestMapping(value = "/log", method = RequestMethod.GET)
	public String log() throws Exception {
		// 让cat去捕获异常
		Integer.parseInt("dd");
		return "Cat.logError();";
	}
	
	/**
	 * Cat.newTransaction 与Cat.getProducer().newTransaction 区别在于 一个是重新生成一个transation  和获取当前线程绑定的transaction“
	 *
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/cycletransation", method = RequestMethod.GET)
	public String newtransation() throws Exception {
		Transaction t = Cat.getProducer().newTransaction("TransactionTest", "Cat.getProducer()");
		Cat.getProducer().logEvent("eventType1", "1", Message.SUCCESS, "");
		Cat.getProducer().logEvent("eventType1", "2", Message.SUCCESS, "");
		Transaction t2 = Cat.getProducer().newTransaction("TransactionTest-1", "child transaction 1");
		Cat.getProducer().logEvent("eventType2-1", "2-1", Message.SUCCESS, "");
		Cat.getProducer().logEvent("eventType2-2", "2-2", Message.SUCCESS, "");
		t2.addData("tChild transaction-1");
		t2.setStatus(Message.SUCCESS);
		t2.complete();
		Transaction t3 = Cat.getProducer().newTransaction("TransactionTest-2", "child transaction 2");
		Cat.getProducer().logEvent("eventType3-1", "3-1", Message.SUCCESS, "");
		Cat.getProducer().logEvent("eventType3-2", "3-2", Message.SUCCESS, "");
		t3.addData("Child transaction-2");
		t3.setStatus(Message.SUCCESS);
		// 休眠3s 验证时间
		Thread.sleep(4000);
		t3.complete();
		t.addData(" Parent transaction");
		t.setStatus(Message.SUCCESS);
		t.complete();
		return "";
	}
	
	/**
	 * Transaction
	 a).transaction适合记录跨越系统边界的程序访问行为，比如远程调用，数据库调用，也适合执行时间较长的业务逻辑监控
	 b).某些运行期单元要花费一定时间完成工作, 内部需要其他处理逻辑协助, 我们定义为Transaction.
	 c).Transaction可以嵌套(如http请求过程中嵌套了sql处理).
	 d).大部分的Transaction可能会失败, 因此需要一个结果状态码.
	 e).如果Transaction开始和结束之间没有其他消息产生, 那它就是Atomic Transaction(合并了起始标记).
	 Event
	 a). Event用来记录次数，表名单位时间内消息发生次数，比如记录系统异常，它和transaction相比缺少了时间的统计，开销比transaction要小
	 
	 type和name的组合要满足全局唯一性
     Transaction type有 "URL", "SQL", "Email", "Exec"等
     Event type有 "Info", "Warn", "Error"
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/transation", method = RequestMethod.GET)
	public String transation() throws Exception {
		// Transaction的type
		Transaction t = Cat.newTransaction("TransactionTest", "findorder");
		// 3.Event Event用来记录次数，表名单位时间内消息发生次数，比如记录系统异常，它和transaction相比缺少了时间的统计，开销比transaction要小
		Cat.logEvent("info", "transation", Message.SUCCESS, "参数1");
		Cat.logEvent("error", "transation", Message.SUCCESS, "参数1");
		t.addData("Transaction测试");
		t.setStatus(Message.SUCCESS);
		t.complete();
		return "";
	}
	
	/**
	 * 利用annation 自动去注册 Transaction
	 *
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/annation", method = RequestMethod.GET)
	public String annation() throws Exception {
		catAnnationService.findAll();
		catAnnationService.findAllTwo();
		return "";
	}
	
	/**
	 * 方法级别的annation  可以自定义type和name
	 *
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/annationMethod", method = RequestMethod.GET)
	public String annationMethod() throws Exception {
		catAnnationMethodService.findAll();
		catAnnationMethodService.findAllThress("");
		return "";
	}
	
	@RequestMapping(value = "/cache/{name}", method = RequestMethod.GET)
	public String cache(@PathVariable("name") String name) throws Exception {
		String key = redisKeyConfiguration.getUserInfoLoginName() + name;
		UserInfo user = catCacheAnnationMethodService.getUserInfoFromRedis(name);
		return resultCodeConfiguration.getResult().addData(user);
	}
}

package com.ggj.webmagic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ggj.webmagic.autoconfiguration.TieBaConfiguration;

/**
 * @author:gaoguangjin
 * @date 2016/8/19 10:58
 */
@Controller
public class WebmagicController {
	
	@Autowired
	private WebmagicService webmagicService;
	
	@Autowired
	private TieBaConfiguration tieBaConfiguration;
	
	@RequestMapping("/tiebatop/{tieBaName}/{size}")
	public String tieBaTop(@PathVariable("tieBaName") String tieBaName, @PathVariable("size") Integer size, Model model)
			throws Exception {
		webmagicService.getTieBaTop(tieBaName, size, model);
		model.addAttribute("name", tieBaName);
		return "tiebatop";
	}
	
	@RequestMapping("/tiebatop/level/{tieBaName}/")
	public String tieBaTop(@PathVariable("tieBaName") String tieBaName, Model model) throws Exception {
		webmagicService.getLevelCount(tieBaName, model);
		model.addAttribute("name", tieBaName);
		return "tiebatoplevel";
	}
	
	@RequestMapping("/tieba/img/{tieBaName}/{begin}/{end}")
	public String tieBaTop(Model model, @PathVariable("tieBaName") String tieBaName,@PathVariable("begin") Integer begin,@PathVariable("end") Integer end) throws Exception {
		model.addAttribute("pageUrlPrefix", tieBaConfiguration.getTiebaContentPageUrl());
		webmagicService.getTieBaImage(model, tieBaName,begin,end);
		return "tiebaimage";
	}

    /**
     * 增加默认贴吧
     * @param model
     * @return
     * @throws Exception
     */
	@RequestMapping("/tieba/img")
    public String tieBaTop( Model model) throws  Exception{
        model.addAttribute("pageUrlPrefix",tieBaConfiguration.getTiebaContentPageUrl());
       webmagicService.getTieBaImage(model,tieBaConfiguration.getTiebaName()[0], 0, -1);
        model.addAttribute("tiebaName",tieBaConfiguration.getTiebaName()[0]);
        return "tiebaimage";
    }
}

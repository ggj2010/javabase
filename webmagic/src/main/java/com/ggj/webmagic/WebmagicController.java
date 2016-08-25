package com.ggj.webmagic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

import static org.apache.coyote.http11.Constants.a;

/**
 * @author:gaoguangjin
 * @date 2016/8/19 10:58
 */
@Controller
public class WebmagicController {
    @Autowired
    private WebmagicService webmagicService;

    @RequestMapping("/tiebatop/{tieBaName}/{size}")
    public String tieBaTop(@PathVariable("tieBaName") String tieBaName, @PathVariable("size") Integer size, Model model) throws  Exception{
       webmagicService.getTieBaTop(tieBaName,size,model);
        model.addAttribute("name",tieBaName);
        return "tiebatop";
    }
    @RequestMapping("/tiebatop/level/{tieBaName}/")
    public String tieBaTop(@PathVariable("tieBaName") String tieBaName, Model model) throws  Exception{
       webmagicService.getLevelCount(tieBaName,model);
        model.addAttribute("name",tieBaName);
        return "tiebatoplevel";
    }
    @RequestMapping("/tieba/img")
    public String tieBaTop( Model model) throws  Exception{
       webmagicService.getTieBaImage(model);
        return "tiebaimage";
    }

}

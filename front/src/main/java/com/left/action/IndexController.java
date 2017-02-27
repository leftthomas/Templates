package com.left.action;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
/**
 * Created by left on 15/10/29.
 */
@Controller
public class IndexController {

    /**
     * 根目录下的跳转控制
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/")
    public String index(Model model, HttpServletRequest request) {
        return "index";
    }

}

package com.left.action;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by left on 16/01/04.
 */
@Controller
@RequestMapping(value = "/choices")
public class ChoicesController {

    /**
     * 精选目录下的跳转控制
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/")
    public String index(Model model, HttpServletRequest request) {
        return "choices";
    }

}

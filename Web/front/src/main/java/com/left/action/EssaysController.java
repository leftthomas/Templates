package com.left.action;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by left on 16/01/04.
 */
@Controller
@RequestMapping(value = "/essays")
public class EssaysController {

    /**
     * 文章目录下的跳转控制
     * 默认跳转到女生的内容
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/")
    public String index(Model model, HttpServletRequest request) {

        return "redirect:/essays/girl";
    }

    /**
     * 跳转到女生文章页面
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/girl")
    public String girl(Model model, HttpServletRequest request) {
        return "essays_girl";
    }

    /**
     * 跳转到男生文章页面
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/boy")
    public String boy(Model model, HttpServletRequest request) {
        return "essays_boy";
    }
}

package com.ywh.spring.example.controller;

import com.ywh.spring.core.annotation.Controller;
import com.ywh.spring.example.model.User;
import com.ywh.spring.example.service.UserService;
import com.ywh.spring.ioc.Autowired;
import com.ywh.spring.mvc.annotation.RequestMapping;
import com.ywh.spring.mvc.annotation.RequestMethod;
import com.ywh.spring.mvc.annotation.RequestParam;
import com.ywh.spring.mvc.annotation.ResponseBody;
import com.ywh.spring.mvc.bean.ModelAndView;

import java.util.List;

/**
 * @author ywh
 * @since 4/20/2021
 */
@Controller
@RequestMapping
public class TestController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "index")
    @ResponseBody
    public String hello(){
        return "Hello, World!";
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public ModelAndView getUserList() {
        List<User> list = userService.getUser();
        return new ModelAndView().setView("user_list.jsp").addObject("list", list);
    }

    @RequestMapping(value = "detail", method = RequestMethod.GET)
    public ModelAndView getUser(@RequestParam(value = "id") long id) {
        User user = userService.getUserById(id);
        return new ModelAndView().setView("user_detail.jsp").addObject("user", user);
    }
}

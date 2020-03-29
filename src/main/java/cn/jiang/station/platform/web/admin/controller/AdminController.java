package cn.jiang.station.platform.web.admin.controller;

import cn.jiang.station.platform.web.admin.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AdminController {
    @Autowired
    private AdminService adminService;

    /**
     * 跳转登录页
     *
     * @param loginCode
     * @param password
     * @return
     */
    @RequestMapping(value = {"", "/login"}, method = RequestMethod.GET)
    public String login() {
        String json = adminService.login("", "");
        System.out.println("json:" + json);
        return "index";
    }

}

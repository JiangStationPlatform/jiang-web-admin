package cn.jiang.station.platform.web.admin.service;

import cn.jiang.station.platform.web.admin.service.fallback.AdminServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "jiang-service-admin", fallback = AdminServiceFallback.class)
public interface AdminService {
    @RequestMapping(value = {"/login"}, method = RequestMethod.GET)
    public String login(@RequestParam(value = "loginCode") String loginCode, @RequestParam(value = "password") String password);
}

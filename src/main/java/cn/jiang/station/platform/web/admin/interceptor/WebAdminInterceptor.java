package cn.jiang.station.platform.web.admin.interceptor;

import cn.jiang.station.platform.common.domain.TbSysUser;
import cn.jiang.station.platform.common.utils.CookieUtils;
import cn.jiang.station.platform.common.utils.MapperUtils;
import cn.jiang.station.platform.web.admin.WebAdminApplication;
import cn.jiang.station.platform.web.admin.service.RedisService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class WebAdminInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(WebAdminApplication.class);

    @Autowired
    private RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        logger.info("WebAdmin---》拦截器开始");
        String token = CookieUtils.getCookieValue(request, "token");
        logger.info("token = " + token);
        if (StringUtils.isBlank(token)) {
            logger.info("token为空，请登陆");
            try {
                response.sendRedirect("http://localhost:8503/login?url=http://localhost:8601");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        HttpSession session = request.getSession();
        TbSysUser tbSysUser = (TbSysUser) session.getAttribute("tbSysUser");
        if (tbSysUser != null) {
            logger.info("账户已登陆");
            if (modelAndView != null) {
                modelAndView.addObject("tbSysUser", tbSysUser);
            }

        } else {
            logger.info("账户未登录");
            String token = CookieUtils.getCookieValue(request, "token");
            if (StringUtils.isNotBlank(token)) {
                String loginCode = redisService.get(token);
                if (StringUtils.isNotBlank(loginCode)) {
                    String json = redisService.get(loginCode);
                    if (StringUtils.isNotBlank(json)) {
                        try {
                            logger.info("获取登陆信息，创建局部会话");
                            tbSysUser = MapperUtils.json2pojo(json, TbSysUser.class);
                            if (modelAndView != null) {
                                modelAndView.addObject("tbSysUser", tbSysUser);
                            }
                            request.getSession().setAttribute("tbSysUser", tbSysUser);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        logger.info("二次确认信息");
        if (tbSysUser == null) {
            try {
                response.sendRedirect("http://localhost:8503/login?url=http://localhost:8601");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}

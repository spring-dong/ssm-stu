package com.keafmd.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/req")
public class RequestController {

    @RequestMapping("test")
    public void testController(HttpServletRequest request, HttpServletResponse response){
        String contextPath = request.getContextPath();
        String servletPath = request.getServletPath();
        String remoteAddr = request.getRemoteAddr();
        String queryString = request.getQueryString();
        String requestURI = request.getRequestURI();
        StringBuffer requestURL = request.getRequestURL();
        Cookie[] cookies = request.getCookies();
        String method = request.getMethod();
        String pathInfo = request.getPathInfo();
        String pathTranslated = request.getPathTranslated();
        String remoteUser = request.getRemoteUser();
        String requestedSessionId = request.getRequestedSessionId();
        request.getSession().setAttribute("sss","dongxuelei");


    }

}

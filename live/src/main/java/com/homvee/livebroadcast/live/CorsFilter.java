package com.homvee.livebroadcast.live;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-07-23 18:29
 */

public class CorsFilter implements Filter {
    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(javax.servlet.ServletRequest req, javax.servlet.ServletResponse servletResponse, javax.servlet.FilterChain chain) throws javax.servlet.ServletException, IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        httpResponse.addHeader("Access-Control-Allow-Origin", "*");
        httpResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpResponse.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        httpResponse.addHeader("Access-Control-Allow-Headers", "Keep-Alive,Upgrade-Insecure-Requests,DNT,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Range,token");
        httpResponse.addHeader("Access-Control-Expose-Headers", "Content-Length,Content-Range,Upgrade-Insecure-Requests");
        httpResponse.addHeader("Access-Control-Allow-Credentials", "true");

        chain.doFilter(req, servletResponse);
    }

    @Override
    public void init(javax.servlet.FilterConfig config) throws javax.servlet.ServletException {

    }

}

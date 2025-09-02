package org.authority.StarGuard2.config;

import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 登录拦截器
 * 用于拦截未登录用户对受保护资源的访问
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    /**
     * 在请求处理之前进行拦截
     * @param request HTTP请求对象
     * @param response HTTP响应对象
     * @param handler 处理请求的处理器
     * @return true表示继续处理请求，false表示中断请求处理
     * @throws Exception 异常
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取HTTP会话
        HttpSession session = request.getSession(false);
        
        // 获取请求的URI
        String requestURI = request.getRequestURI();
        
        // 检查是否是公开资源（不需要登录即可访问）
        if (requestURI.equals("/star-guard/login") || 
            requestURI.contains("/static/") || 
            requestURI.contains("/css/") || 
            requestURI.contains("/js/") || 
            requestURI.contains("/images/") || 
            requestURI.contains("/fonts/") || 
            requestURI.contains("/webjars/")) {
            // 公开资源，允许访问
            return true;
        }
        
        // 检查用户是否已登录（会话中是否存在用户名）
        if (session != null && session.getAttribute("username") != null) {
            // 用户已登录，允许访问
            return true;
        } else {
            // 用户未登录，重定向到登录页面
            response.sendRedirect("/star-guard/login");
            return false;
        }
    }
}
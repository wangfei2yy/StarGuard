package org.authority.StarGuard2.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 登录控制器
 * 负责处理用户登录相关的请求
 */
@Controller
public class LoginController {

    // 从配置文件中读取用户名和密码
    @Value("${auth.username}")
    private String configuredUsername;

    @Value("${auth.password}")
    private String configuredPassword;

    /**
     * 提供登录页面的访问入口
     * @param model 模型对象，用于传递数据到视图
     * @param error 错误信息参数，如果存在则传递给视图
     * @return 登录页面视图名称
     */
    @GetMapping("/login")
    public String login(Model model, @RequestParam(value = "error", required = false) String error) {
        if (error != null) {
            model.addAttribute("error", "用户名或密码错误，请重试");
        }
        return "login";
    }

    /**
     * 处理登录表单提交
     * @param username 用户输入的用户名
     * @param password 用户输入的密码
     * @param session HTTP会话对象，用于存储用户登录状态
     * @return 重定向到主页或返回登录页面
     */
    @PostMapping("/login")
    public String loginSubmit(@RequestParam String username, 
                             @RequestParam String password, 
                             HttpSession session) {
        // 验证用户名和密码是否与配置文件中的一致
        if (configuredUsername.equals(username) && configuredPassword.equals(password)) {
            // 登录成功，将用户名存储在会话中
            session.setAttribute("username", username);
            // 重定向到主页
            return "redirect:/";
        } else {
            // 登录失败，重定向到登录页面并显示错误信息
            return "redirect:/login?error=true";
        }
    }

    /**
     * 处理用户登出请求
     * @param session HTTP会话对象
     * @return 重定向到登录页面
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // 使当前会话失效
        session.invalidate();
        // 重定向到登录页面
        return "redirect:/login";
    }
}
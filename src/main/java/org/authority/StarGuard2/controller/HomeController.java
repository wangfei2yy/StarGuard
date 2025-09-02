package org.authority.StarGuard2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import javax.servlet.http.HttpSession;

/**
 * 主页控制器
 * 负责处理前端页面的访问请求
 */
@Controller
public class HomeController {

    /**
     * 提供系统首页的访问入口
     * @param model 模型对象，用于传递数据到视图
     * @param session HTTP会话对象
     * @return 首页视图名称
     */
    @GetMapping("/")
    public String index(Model model, HttpSession session) {
        // 从会话中获取用户名并添加到模型中
        String username = (String) session.getAttribute("username");
        if (username != null) {
            model.addAttribute("username", username);
        }
        return "index";
    }

    /**
     * 提供系统主页的另一个访问入口（可用于书签或链接）
     * @param model 模型对象
     * @param session HTTP会话对象
     * @return 首页视图名称
     */
    @GetMapping("/index")
    public String home(Model model, HttpSession session) {
        // 从会话中获取用户名并添加到模型中
        String username = (String) session.getAttribute("username");
        if (username != null) {
            model.addAttribute("username", username);
        }
        return "index";
    }

    /**
     * 提供系统的欢迎页面（可选的访问入口）
     * @param model 模型对象
     * @param session HTTP会话对象
     * @return 首页视图名称
     */
    @GetMapping("/welcome")
    public String welcome(Model model, HttpSession session) {
        // 从会话中获取用户名并添加到模型中
        String username = (String) session.getAttribute("username");
        if (username != null) {
            model.addAttribute("username", username);
        }
        return "index";
    }
}
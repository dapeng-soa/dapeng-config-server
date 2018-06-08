package com.github.dapeng.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author with struy.
 * Create by 2018/5/29 14:46
 * email :yq1724555319@gmail.com
 */

@Controller
@RequestMapping("/")
public class PageController {
    /**
     * 首页
     *
     * @return
     */
    @GetMapping()
    public String index(Model model) {
        model.addAttribute("tagName", "index");
        return "redirect:monitor";
    }

    /**
     * 登录
     *
     * @return
     */
    @GetMapping("security_login")
    public String login() {
        return "login";
    }

    /**
     * 服务监控
     *
     * @return
     */
    @GetMapping(value = "monitor")
    public String monitor(Model model) {
        model.addAttribute("tagName", "monitor");
        return "page/monitor";
    }

    /**
     * 配置管理
     *
     * @return
     */
    @GetMapping(value = "config")
    public String config(Model model) {
        model.addAttribute("tagName", "config");
        return "redirect:config/service";
    }

    /**
     * 配置管理-服务管理
     *
     * @return
     */
    @GetMapping(value = "config/service")
    public String service(Model model) {
        model.addAttribute("tagName", "config");
        model.addAttribute("sideName", "config-service");
        return "page/config-service";
    }

    /**
     * 配置管理-白名单管理
     *
     * @return
     */
    @GetMapping(value = "config/whitelist")
    public String whitelist(Model model) {
        model.addAttribute("tagName", "config");
        model.addAttribute("sideName", "config-whitelist");
        return "page/config-whitelist";
    }

    /**
     * 配置管理-白名单管理
     *
     * @return
     */
    @GetMapping(value = "config/apikey")
    public String apikey(Model model) {
        model.addAttribute("tagName", "config");
        model.addAttribute("sideName", "config-apikey");
        return "page/config-apikey";
    }


    /**
     * 发布部署
     *
     * @return
     */
    @GetMapping(value = "deploy")
    public String deploy(Model model) {
        model.addAttribute("tagName", "deploy");
        return "page/deploy";
    }

}

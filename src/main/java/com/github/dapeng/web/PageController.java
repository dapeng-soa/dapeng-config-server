package com.github.dapeng.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author with struy.
 * Create by 2018/5/29 14:46
 * email :yq1724555319@gmail.com
 */

@Controller
public class PageController {
    /**
     * 首页
     *
     * @return
     */
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("tagName", "index");
        return "redirect:config";
    }

    /**
     * 登录
     *
     * @return
     */
    @GetMapping(value = "/error")
    public String error() {
        return "redirect:login";
    }

    /**
     * 登录
     *
     * @return
     */
    @GetMapping(value = "/login")
    public String login() {
        return "security_login";
    }

    /**
     * 服务监控
     *
     * @return
     */
    @GetMapping(value = "/monitor")
    public String monitor(Model model) {
        model.addAttribute("tagName", "monitor");
        return "page/monitor";
    }

    /**
     * 集群管理
     *
     * @return
     */
    @GetMapping(value = "/clusters")
    public String clusters(Model model) {
        model.addAttribute("tagName", "clusters");
        model.addAttribute("sideName", "clusters");
        return "page/clusters";
    }

    /**
     * 配置管理
     *
     * @return
     */
    @GetMapping(value = "/config")
    public String config(Model model) {
        model.addAttribute("tagName", "config");
        return "redirect:config/service";
    }

    /**
     * 配置管理-服务管理
     *
     * @return
     */
    @GetMapping(value = "/config/service")
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
    @GetMapping(value = "/config/whitelist")
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
    @GetMapping(value = "/config/apikey")
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
    @GetMapping(value = "/deploy")
    public String deploy(Model model) {
        model.addAttribute("tagName", "deploy");
        return "redirect:deploy/exec";
    }

    /**
     * 环境集管理
     *
     * @return
     */
    @GetMapping(value = "/deploy/set")
    public String deploySet(Model model) {
        model.addAttribute("tagName", "deploy-set");
        model.addAttribute("sideName", "deploy-set");
        return "page/deploy-set";
    }

    /**
     * 节点信息管理
     *
     * @return
     */
    @GetMapping(value = "/deploy/host")
    public String deployHost(Model model) {
        model.addAttribute("tagName", "deploy-host");
        model.addAttribute("sideName", "deploy-host");
        return "page/deploy-host";
    }

    /**
     * 服务信息管理
     *
     * @return
     */
    @GetMapping(value = "/deploy/service")
    public String deployService(Model model) {
        model.addAttribute("tagName", "deploy-service");
        model.addAttribute("sideName", "deploy-service");
        return "page/deploy-service";
    }

    /**
     * 部署单元管理
     *
     * @return
     */
    @GetMapping(value = "/deploy/unit")
    public String deployUnit(Model model) {
        model.addAttribute("tagName", "deploy-unit");
        model.addAttribute("sideName", "deploy-unit");
        return "page/deploy-unit";
    }

    /**
     * 执行发布
     *
     * @return
     */
    @GetMapping(value = "/deploy/exec")
    public String deployExec(Model model) {
        model.addAttribute("tagName", "deploy-exec");
        model.addAttribute("sideName", "deploy-exec");
        return "page/deploy-exec";
    }

    /**
     * 发布升级日志
     *
     * @return
     */
    @GetMapping(value = "/deploy/journal")
    public String deployLog(Model model) {
        model.addAttribute("tagName", "deploy-journal");
        model.addAttribute("sideName", "deploy-journal");
        return "page/deploy-journal";
    }

}

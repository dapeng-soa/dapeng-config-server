package com.github.dapeng.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.DefaultErrorViewResolver;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-10-18 5:23 PM
 */
@Component
public class GlobalErrorViewResolver extends DefaultErrorViewResolver {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalErrorViewResolver.class);

    private ApplicationContext applicationContext;

    /**
     * Create a new {@link DefaultErrorViewResolver} instance.
     *
     * @param applicationContext the source application context
     * @param resourceProperties resource properties
     */
    @Autowired
    public GlobalErrorViewResolver(ApplicationContext applicationContext, ResourceProperties resourceProperties) {
        super(applicationContext, resourceProperties);
        this.applicationContext = applicationContext;
    }


    @Override
    public ModelAndView resolveErrorView(HttpServletRequest request, HttpStatus status, Map<String, Object> model) {
        String viewName = String.valueOf(status);
        String errorViewName = "error/" + viewName;
        String prefix = "/views/";
        Resource resource = this.applicationContext.getResource("/");
        try {
            resource = resource.createRelative(prefix + errorViewName + ".jsp");
            if (resource.exists()) {
                return new ModelAndView(errorViewName, model);
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return new ModelAndView("error/error", model);
    }
}

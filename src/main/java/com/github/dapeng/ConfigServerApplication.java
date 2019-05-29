package com.github.dapeng;

import com.github.dapeng.openapi.cache.ZkBootstrap;
import com.github.dapeng.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.support.ErrorPageFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author struy
 */
@SpringBootApplication
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
public class ConfigServerApplication {

    private static Logger logger = LoggerFactory.getLogger(ConfigServerApplication.class);

    public static void main(String[] args) {
        PropertiesUtil.loadProperties();

        logger.info("位置：ConfigServerApplication.main ==>  ********************************************** ");
        //初始化元数据
        ZkBootstrap zkBootstrap = new ZkBootstrap();
        zkBootstrap.init();

        logger.info("位置：ConfigServerApplication.main ==>  **********************************************  ENDENDEND");

        //启动配置中心
        new SpringApplicationBuilder()
                .bannerMode(Banner.Mode.LOG)
                .sources(ConfigServerApplication.class)
                .run(args);
    }

    @Bean
    public ErrorPageFilter errorPageFilter() {
        return new ErrorPageFilter();
    }

    @Bean
    public FilterRegistrationBean disableSpringBootErrorFilter(ErrorPageFilter filter) {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(filter);
        filterRegistrationBean.setEnabled(false);
        return filterRegistrationBean;
    }

    /**
     * 针对前端ajax的消息转换器
     *
     * @return
     */
    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter mappingConverter = new MappingJackson2HttpMessageConverter();
        List<MediaType> list = new ArrayList();
        list.add(MediaType.TEXT_HTML);
        list.add(MediaType.TEXT_PLAIN);
        list.add(MediaType.APPLICATION_JSON_UTF8);
        mappingConverter.setSupportedMediaTypes(list);
        return mappingConverter;
    }
}

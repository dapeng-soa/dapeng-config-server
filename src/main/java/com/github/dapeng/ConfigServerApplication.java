package com.github.dapeng;

import com.github.dapeng.util.PropertiesUtil;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author struy
 */
@SpringBootApplication
public class ConfigServerApplication {

	public static void main(String[] args) {
        PropertiesUtil.loadProperties();
		new SpringApplicationBuilder()
				.bannerMode(Banner.Mode.CONSOLE)
				.sources(ConfigServerApplication.class)
				.run(args);
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

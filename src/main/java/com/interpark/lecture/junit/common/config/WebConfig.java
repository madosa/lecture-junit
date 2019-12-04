package com.interpark.lecture.junit.common.config;

import com.interpark.lecture.junit.common.log.ServiceLogInterceptor;
import com.interpark.ncl.std.common.error.resolver.CommonExceptionResolver;
import com.interpark.ncl.std.common.log.LogFilter;
import com.interpark.ncl.std.common.log.MDCFilter;
import com.interpark.ncl.std.common.security.cors.CORSFilter;
import com.interpark.ncl.std.common.security.xss.XSSFilter;
import com.interpark.ncl.std.common.service.bean.SpringApplicationContext;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages={"com.interpark.lecture.junit", "com.interpark.ncl.std.common"})
public class WebConfig implements WebMvcConfigurer {

    @Value("${interpark.web.domain}")
    private String defaultOriginDomain;
    @Value("${interpark.ncl.image.api.domain}")
    private String defaultApiDomain;
    @Value("${interpark.ncl.restclient.socket.timeout}")
    private int socketTimeout;
    @Value("${interpark.ncl.restclient.socket.linger}")
    private int socketLinger;
    @Value("${interpark.ncl.restclient.socket.reuse_address}")
    private boolean socketReuseAddress;
    @Value("${interpark.ncl.restclient.conn_pool.max_total}")
    private int connMaxTotal;
    @Value("${interpark.ncl.restclient.conn_pool.max_per_route}")
    private int connMaxPerRoute;

    @Autowired
    private SocketConfig socketConfig;
    @Autowired
    private PoolingHttpClientConnectionManager connectionManager;
    @Autowired
    private RequestConfig requestConfig;
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new ServiceLogInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/health/check");
    }

    @Bean
    public FilterRegistrationBean corsFilter() {

        CORSFilter corsFilter = new CORSFilter();
        corsFilter.setDefaultOriginDomain(this.defaultOriginDomain);
        corsFilter.setDefaultApiDomain(this.defaultApiDomain);
        corsFilter.setAccessControlAllowMethods("POST, GET, PUT, PATCH, DELETE");
        corsFilter.setAccessControlAllowHeaders("Origin, X-Requested-With, Content-Type, Accept, X-XSRF-TOKEN");
        corsFilter.setAccessControlMaxAge("3600");
        corsFilter.setAccessControlAllowCredentials("true");

        FilterRegistrationBean registrationBean = new FilterRegistrationBean(corsFilter);
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1);

        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean logFilter() {

        FilterRegistrationBean registrationBean = new FilterRegistrationBean(new LogFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(2);

        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean mdcFilter() {

        FilterRegistrationBean registrationBean = new FilterRegistrationBean(new MDCFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(3);

        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean xssFilter() {

        FilterRegistrationBean registrationBean = new FilterRegistrationBean(new XSSFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(4);

        return registrationBean;
    }

    @Bean
    public MessageSource messageSource(){

        ReloadableResourceBundleMessageSource msgSource = new ReloadableResourceBundleMessageSource();
        msgSource.setBasename("classpath:/message/error/error-common");
        msgSource.addBasenames("classpath:/message/validation/validation-common");
        msgSource.setDefaultEncoding("UTF-8");
        msgSource.setCacheSeconds(60);

        return msgSource;
    }

    @Bean
    public LocalValidatorFactoryBean validator(MessageSource messageSource) {

        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.setValidationMessageSource(messageSource);

        return validator;
    }

    @Bean
    public CommonExceptionResolver commonExceptionResolver() {

        return new CommonExceptionResolver();
    }

    @Bean
    public SpringApplicationContext springApplicationContext() {

        return new SpringApplicationContext();
    }

    @Bean(name = "jsonView")
    public MappingJackson2JsonView jsonView(){

        MappingJackson2JsonView jsonView = new MappingJackson2JsonView();
        jsonView.setContentType("application/json;charset=UTF-8");

        return jsonView;
    }

    @Bean
    public HttpClientBuilder httpClientBuilder() {

        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        httpClientBuilder.setDefaultRequestConfig(requestConfig);
        httpClientBuilder.setConnectionManager(connectionManager);

        return httpClientBuilder;
    }

    @Bean
    public PoolingHttpClientConnectionManager connectionManager() {

        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(2000, TimeUnit.MILLISECONDS);
        connectionManager.setMaxTotal(connMaxTotal);
        connectionManager.setDefaultMaxPerRoute(connMaxPerRoute);
        connectionManager.setDefaultSocketConfig(socketConfig);

        return connectionManager;
    }

    @Bean
    public SocketConfig socketConfig() {

        SocketConfig socketConfig = SocketConfig.custom()
                .setSoTimeout(socketTimeout)
                .setSoKeepAlive(true)
                .setTcpNoDelay(true)
                .setSoLinger(socketLinger)
                .setSoReuseAddress(socketReuseAddress)
                .build();

        return socketConfig;
    }

    @Bean
    public RequestConfig requestConfig() {

        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(socketTimeout)
                .setConnectTimeout(socketTimeout)
                .setConnectionRequestTimeout(5000)
                .build();

        return requestConfig;
    }
}

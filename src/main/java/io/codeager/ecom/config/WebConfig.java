/*
 * Copyright 2017 Jiupeng Zhang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.codeager.ecom.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.codeager.ecom.interceptor.AdminSessionInterceptor;
import com.codeager.ecom.util.springfox.*;
import com.codeager.portal.AuthenticationHandler;
import com.codeager.portal.DefaultAuthenticationHandler;
import com.codeager.portal.controller.UserResolver;
import com.codeager.portal.interceptor.AuthenticationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.UrlTemplateResolver;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiListing;
import springfox.documentation.service.ResourceListing;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.json.Json;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;
import java.util.Locale;

import static com.codeager.ecom.util.Routing.ADMIN_BASE;
import static com.codeager.ecom.util.Routing.API_BASE;

/**
 * Spring MVC Pointcut
 *
 * @author Jiupeng Zhang
 * @since 11/18/2017
 */
@Configuration
@EnableWebMvc
@EnableSwagger2
@Import({
        springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration.class
})
@ComponentScan(basePackages = {
        "com.codeager.*.controller",
        "com.codeager.*.interceptor",
})
@PropertySource("classpath:/conf/springmvc.properties")
@PropertySource("classpath:/conf/swagger.properties")
public class WebConfig extends WebMvcConfigurerAdapter {
    @Value("${spmvc.mapping.resource}")
    private String resourceMapping;
    @Value("${spmvc.view}")
    private String viewLocation;
    @Value("${spmvc.resource}")
    private String resourceLocation;
    @Value("${springfox.documentation.swagger.v2.path}")
    private String swaggerPath;

    @Bean("templateResolver")
    public ITemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setPrefix(viewLocation);
        templateResolver.setSuffix(".html");
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setOrder(1);
        templateResolver.setCacheable(false);
        return templateResolver;
    }

    @Bean("thymeleafViewResolver")
    public ThymeleafViewResolver thymeleafViewResolver() {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());
        viewResolver.setCharacterEncoding("UTF-8");
        return viewResolver;
    }

    @Bean("templateEngine")
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.addDialect(new Java8TimeDialect());
        templateEngine.addTemplateResolver(new UrlTemplateResolver()); // for template ref-routing
        templateEngine.setTemplateResolver(templateResolver());
        templateEngine.setEnableSpringELCompiler(false);
        return templateEngine;
    }

    @Bean("multipartResolver")
    public MultipartResolver multipartResolver() {
        return new CommonsMultipartResolver();
//        multipartResolver.setMaxUploadSize(8 * 1024 * 1024);        // 8MB
//        multipartResolver.setMaxUploadSizePerFile(2 * 1024 * 1024); // 2MB
    }

    @Bean("gsonHttpMessageConverter")
    public GsonHttpMessageConverter gsonHttpMessageConverter() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Json.class, new SpringfoxJsonAdaptor())
                .registerTypeAdapter(ApiListing.class, new SpringfoxApiListingJsonAdaptor())
                .registerTypeAdapter(SwaggerResource.class, new SpringfoxResourceJsonAdaptor())
                .registerTypeAdapter(ResourceListing.class, new SpringfoxResourceListingJsonAdaptor())
                .registerTypeAdapter(UiConfiguration.class, new SpringfoxUiConfigurationJsonAdaptor())
                .registerTypeAdapter(SecurityConfiguration.class, new SpringfoxSecurityConfigurationJsonAdaptor())
//                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting()
                .create();

        GsonHttpMessageConverter converter = new GsonHttpMessageConverter();
        converter.setGson(gson);
        return converter;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(gsonHttpMessageConverter());
        super.configureMessageConverters(converters);
    }

    @Bean("localeResolver")
    public LocaleResolver localeResolver() {
        CookieLocaleResolver localeResolver = new CookieLocaleResolver();
        localeResolver.setDefaultLocale(Locale.US);
        localeResolver.setCookieName("locale");
        return localeResolver;
    }

    @Bean("authenticationHandler")
    public AuthenticationHandler authenticationHandler() {
        return new DefaultAuthenticationHandler();
    }

    @Bean
    public Docket swaggerDocumentationPlugin() {
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("[EAC] ECOM API CENTER")
                .description("Backend Service for Codeager IO<br/>")
                .version("1.0")
                .build();

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("ecom")
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.regex(API_BASE.concat("/.*")))
                .build()
                .forCodeGeneration(false)
                .useDefaultResponseMessages(false)
                .apiInfo(apiInfo);
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(resourceMapping)
                .addResourceLocations(resourceLocation.split(","))
                // introduce swagger documentations
                .addResourceLocations("classpath:/META-INF/resources/", "classpath:/META-INF/resources/webjars/");
    }

    /*
     * Add Argument Resolvers
     */

    private UserResolver userResolver;

    @Autowired
    public void setUserResolver(UserResolver userResolver) {
        this.userResolver = userResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(userResolver);
    }

    /*
     * Add Interceptors
     */

    private AuthenticationInterceptor authenticationInterceptor;
    private AdminSessionInterceptor adminSessionInterceptor;

    @Autowired
    public void setAuthenticationInterceptor(AuthenticationInterceptor authenticationInterceptor) {
        this.authenticationInterceptor = authenticationInterceptor;
    }

    @Autowired
    public void setAdminSessionInterceptor(AdminSessionInterceptor adminSessionInterceptor) {
        this.adminSessionInterceptor = adminSessionInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // internationalization
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        registry.addInterceptor(localeChangeInterceptor);

        // authentication
        registry.addInterceptor(authenticationInterceptor)
                .addPathPatterns(ADMIN_BASE + "/**", API_BASE + "/**");

        // admin identity info
        registry.addInterceptor(adminSessionInterceptor)
                .addPathPatterns(ADMIN_BASE + "/**");
    }
}

package com.yangbo.seckill.seckill.config;


import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 配置了Swagger 的Docket的bean实例,扫描接口的位置
 * .apis
 *   RequestHandlerSelectors 配置swagger扫描接口的方式
 *      basePackage() 指定要扫描哪些包
 *      any() 全部都扫描
 *      none() 全部不扫描
 *      withClassAnnotation() 扫描类上的注解 参数是一个注解的反射对象
 *      withMethodAnnotation() 扫描包上的注解
 * .paths
 *   PathSelectors 路径扫描接口
 *      ant 配置以xxx 开头的路径
 * @return
 */

@Configuration
@EnableSwagger2
@ConditionalOnClass(Docket.class)
public class SwaggerConfig {
    private static final String VERSION = "1.0";

    @Value("${springfox.swagger2.enabled}")
    private Boolean swaggerEnabled;
    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(swaggerEnabled)
                .groupName("SwaggerDemo")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any())
                .build();
    }

    /*
    * 添加摘要信息,配置作者信息
    * */
    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("接口文档")
                .contact(new Contact("yangbo的java日志","http://javadaily.cn","1360664142@qq.com"))
                .description("Swagger接口文档")
                .version(VERSION)
                .build();
    }



}

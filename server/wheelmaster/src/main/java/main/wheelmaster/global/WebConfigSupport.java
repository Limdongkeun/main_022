package main.wheelmaster.global;

import main.wheelmaster.global.argumentresolver.LoginMemberArgumentResolver;
import main.wheelmaster.global.interceptor.LoginCheckInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

@Configuration
@EnableSwagger2
public class WebConfigSupport extends WebMvcConfigurationSupport {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {

        super.addArgumentResolvers(resolvers);
        resolvers.add(new LoginMemberArgumentResolver());
        resolvers.add(new PageableHandlerMethodArgumentResolver());
    }

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/error", "/members/signup", "/index", "/", "api/wheel","/members/login", "/swagger-resources/**"
                ,"/swagger-ui/**", "/webjars/**","/swagger-ui.html/**");
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("구르미 Gurumi")
                .description("구르미 Gurumi API")
                .build();
    }

    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(this.apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("main.wheelmaster"))
                .paths(PathSelectors.any())
                .build();
    }
}

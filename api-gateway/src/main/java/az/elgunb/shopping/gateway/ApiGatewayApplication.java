package az.elgunb.shopping.gateway;

import az.elgunb.shopping.common.filter.JwtTokenFilter;
import az.elgunb.shopping.common.security.TokenProvider;
import az.elgunb.shopping.common.util.LogUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.env.Environment;

@SpringBootApplication
@EnableZuulProxy
@ComponentScan(basePackages = {"az.elgunb.shopping"},
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                value = {TokenProvider.class, JwtTokenFilter.class})
        })
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ApiGatewayApplication.class);
        Environment env = app.run(args).getEnvironment();
        LogUtil.logApplicationStartup(env);
    }

}

package az.elgunb.shopping.identity.config;

import az.elgunb.shopping.common.config.properties.FilterProperties;
import az.elgunb.shopping.common.security.TokenProvider;
import az.elgunb.shopping.common.security.handler.SecurityProblemHandler;
import az.elgunb.shopping.common.util.MessageSourceUtil;
import az.elgunb.shopping.common.util.WebUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({TokenProvider.class, SecurityProblemHandler.class, WebUtil.class, MessageSourceUtil.class,
        FilterProperties.class})
public class CommonLibComponents {
}

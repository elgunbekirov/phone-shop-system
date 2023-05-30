package az.elgunb.shopping.order;

import az.elgunb.shopping.common.util.LogUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication(scanBasePackages = "az.elgunb.shopping")
public class MsOrderApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(MsOrderApplication.class);
        Environment env = app.run(args).getEnvironment();
        LogUtil.logApplicationStartup(env);
    }

}

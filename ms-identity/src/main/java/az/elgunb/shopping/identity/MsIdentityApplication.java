package az.elgunb.shopping.identity;

import az.elgunb.shopping.common.util.LogUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication(scanBasePackages = "az.elgunb.shopping")
public class MsIdentityApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(MsIdentityApplication.class);
        Environment env = app.run(args).getEnvironment();
        LogUtil.logApplicationStartup(env);
    }

}

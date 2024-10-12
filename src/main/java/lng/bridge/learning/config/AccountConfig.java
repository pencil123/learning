package lng.bridge.learning.config;

import com.longport.Config;
import com.longport.ConfigBuilder;
import com.longport.OpenApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AccountConfig {

    private Logger logger = LoggerFactory.getLogger(AccountConfig.class);


    @Value("${account.key}")
    private String key;
    @Value("${account.secret}")
    private String secret;
    @Value("${account.accesstoken}")
    private String accessToken;

    public Config accountConfig() {
        logger.info("the value infos ====  key: {},secret:{},token:{}", key, secret, accessToken);
        try {
            return new ConfigBuilder(key, secret, accessToken).build();
        } catch (OpenApiException e) {
            e.printStackTrace();
        }
        return null;
    }
}

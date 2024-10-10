package lng.bridge.learning.jobs;

import com.longport.*;
import com.longport.trade.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GetAccountBalance {

    @Test
    public void getAccount() throws Exception {
        //Config config = this.config.accountConfig();
        //Config config = Config.fromEnv();
        // Init config without ENV
        // https://longportapp.github.io/openapi-sdk/java/com/longport/ConfigBuilder.html
        Config config = new ConfigBuilder("YOUR_APP_KEY", "YOUR_APP_SECRET", "YOUR_ACCESS_TOKEN").build();
        try (TradeContext ctx = TradeContext.create(config).get()) {
            for (AccountBalance obj : ctx.getAccountBalance().get()) {
                System.out.println(obj);
            }
        }
    }
}

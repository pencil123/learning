package lng.bridge.learning.jobs;

import com.longport.Config;
import com.longport.trade.AccountBalance;
import com.longport.trade.TradeContext;
import lng.bridge.learning.config.AccountConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetAccountBalance {

    @Autowired
    private AccountConfig accountConfig;

    public void getAccount() throws Exception {
        Config config = this.accountConfig.accountConfig();
        //Config config = Config.fromEnv();
        // Init config without ENV
        // https://longportapp.github.io/openapi-sdk/java/com/longport/ConfigBuilder.html
        //Config config = new ConfigBuilder("YOUR_APP_KEY", "YOUR_APP_SECRET", "YOUR_ACCESS_TOKEN").build();
        try (TradeContext ctx = TradeContext.create(config).get()) {
            for (AccountBalance obj : ctx.getAccountBalance().get()) {
                System.out.println(obj);
            }
        }
    }
}

package lng.bridge.learning.jobs;

import com.longport.Config;
import com.longport.Market;
import com.longport.quote.MarketTradingDays;
import com.longport.quote.QuoteContext;
import java.time.LocalDate;
import lng.bridge.learning.config.AccountConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 获取资产总览
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GetMarketTradingDays {

    @Autowired
    private AccountConfig accountConfig;

    @Test
    public void getAccount() throws Exception {
        Config config = this.accountConfig.accountConfig();
        LocalDate localDate = LocalDate.now();
        //Config config = Config.fromEnv();
        // Init config without ENV
        // https://longportapp.github.io/openapi-sdk/java/com/longport/ConfigBuilder.html
        //Config config = new ConfigBuilder("YOUR_APP_KEY", "YOUR_APP_SECRET", "YOUR_ACCESS_TOKEN").build();
        try (QuoteContext ctx = QuoteContext.create(config).get()) {
            MarketTradingDays resp = ctx
                    .getTradingDays(Market.HK, LocalDate.now(), localDate.plusDays(2)).get();
            System.out.println(resp);
        }
    }
}

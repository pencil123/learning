package lng.bridge.learning.jobs;
import com.longport.*;
import com.longport.quote.*;
import com.longport.trade.*;
import lng.bridge.learning.config.AccountConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 订阅实时行情
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class Subscribe {
    @Autowired
    private AccountConfig accountConfig;

    @Test
    public  void main() throws Exception {
        Config config = this.accountConfig.accountConfig();

        try (QuoteContext ctx = QuoteContext.create(config).get()) {
            ctx.setOnQuote((symbol, quote) -> {
                System.out.printf("%s\t%s\n", symbol, quote);
            });
            ctx.subscribe(new String[] { "700.HK", "AAPL.US", "TSLA.US", "NFLX.US" }, SubFlags.Quote, true).get();
            Thread.sleep(30000);
        }
    }

}

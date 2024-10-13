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

import java.math.BigDecimal;

/**
 * 委托下单
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SubmitOrder {
    @Autowired
    private AccountConfig accountConfig;

    @Test
    public  void main() throws Exception {
        Config config = this.accountConfig.accountConfig();
        try (TradeContext ctx = TradeContext.create(config).get()) {
            SubmitOrderOptions opts = new SubmitOrderOptions("700.HK",
                    OrderType.LO,
                    OrderSide.Buy,
                    200,
                    TimeInForceType.Day).setSubmittedPrice(new BigDecimal("4.46"));
            SubmitOrderResponse resp = ctx.submitOrder(opts).get();
            System.out.println(resp);
        }
    }
}

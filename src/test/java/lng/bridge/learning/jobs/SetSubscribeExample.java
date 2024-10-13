package lng.bridge.learning.jobs;

import com.longport.Config;
import com.longport.trade.*;
import lng.bridge.learning.config.AccountConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

/**
 * 交易和资产类的私有通知
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SetSubscribeExample {
    private Logger logger = LoggerFactory.getLogger(SetSubscribeExample.class);
    @Autowired
    private AccountConfig accountConfig;

    @Test
    public void execute() throws Exception {
        Config config = this.accountConfig.accountConfig();

        try (TradeContext ctx = TradeContext.create(config).get()) {
            ctx.setOnOrderChange((order_changed) -> {
                logger.info("OrderChange :{}", order_changed);
            });
            ctx.subscribe(new TopicType[]{TopicType.Private}).get();
            SubmitOrderOptions opts = new SubmitOrderOptions("700.HK",
                    OrderType.LO,
                    OrderSide.Buy,
                    200,
                    TimeInForceType.Day).setSubmittedPrice(new BigDecimal(50));
            SubmitOrderResponse resp = ctx.submitOrder(opts).get();
            logger.info("submit Order:{}", resp);
            Thread.sleep(3000);
        }
    }
}

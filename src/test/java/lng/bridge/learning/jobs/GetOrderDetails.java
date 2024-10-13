package lng.bridge.learning.jobs;

import com.longport.*;
import com.longport.trade.*;
import lng.bridge.learning.config.AccountConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 查看提交的订单的详情
 * https://open.longportapp.com/docs/trade/trade-definition#orderstatus
 * 订单状态
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GetOrderDetails {
    private Logger logger = LoggerFactory.getLogger(GetOrderDetails.class);
    @Autowired
    private AccountConfig accountConfig;

    @Test
    public void execute() throws Exception {
        Config config = this.accountConfig.accountConfig();
        try (TradeContext ctx = TradeContext.create(config).get()) {
            OrderDetail detail = ctx.getOrderDetail("1030024191201775616").get();
            System.out.println(detail);
        }
    }
}

package lng.bridge.learning.jobs;
import com.longport.*;
import com.longport.trade.*;
import lng.bridge.learning.config.AccountConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

/**
 * 获取当日订单
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GetTodayOrders {
    @Autowired
    private AccountConfig accountConfig;

    @Test
    public  void main() throws Exception {
        Config config = this.accountConfig.accountConfig();

        try (TradeContext ctx = TradeContext.create(config).get()) {
            Order[] orders = ctx.getTodayOrders(null).get();
            for (Order order : orders) {
                System.out.println(order);
            }
        }
    }
}

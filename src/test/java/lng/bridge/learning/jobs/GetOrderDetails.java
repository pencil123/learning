package lng.bridge.learning.jobs;

import com.longport.*;
import com.longport.trade.*;
import lng.bridge.learning.config.AccountConfig;
import lng.bridge.learning.entity.OrderRecord;
import lng.bridge.learning.service.OrderRecordService;
import lng.bridge.learning.service.TradingDayService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

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
    @Autowired
    private TradingDayService tradingDayService;
    @Autowired
    private OrderRecordService orderRecordService;





    @Test
    public void execute() throws Exception {
        logger.info("== Job PollingOrders execute runs");
        // 是否为交易日
//        if (!tradingDayService.isTradingDay(LocalDate.now())) {
//            return;//非交易日，则直接退出。
//        }

        //九点之前 或者 16点之后，就退出
//        if (LocalTime.now().isBefore(LocalTime.of(9, 0, 0)) || LocalTime.now().isAfter(LocalTime.of(16, 0, 0))) {
//            return;
//            //12点之后 并且 13点之前，就退出
//        } else if (LocalTime.now().isAfter(LocalTime.of(12, 0, 0)) && LocalTime.now().isBefore(LocalTime.of(13, 0, 0))) {
//            return;
//        }

        //跟踪今天的订单
        List<OrderRecord> orderRecords = orderRecordService.filterTodayOrders(LocalDate.now());
        if(orderRecords.isEmpty()){
            return;
        }

        Config config = this.accountConfig.accountConfig();
        try (TradeContext ctx = TradeContext.create(config).get()) {
            for(OrderRecord orderRecord: orderRecords){
                OrderDetail detail = ctx.getOrderDetail(orderRecord.getOrderId()).get();
                System.out.println(detail);
            }
        }
    }
}


























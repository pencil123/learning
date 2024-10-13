package lng.bridge.learning.jobs;

import com.longport.Config;
import com.longport.trade.*;
import lng.bridge.learning.config.AccountConfig;
import lng.bridge.learning.entity.OrderRecord;
import lng.bridge.learning.entity.Stock;
import lng.bridge.learning.entity.Submit;
import lng.bridge.learning.service.OrderRecordService;
import lng.bridge.learning.service.StockService;
import lng.bridge.learning.service.SubmitService;
import lng.bridge.learning.service.TradingDayService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 委托下单
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SubmitOrder2 {
    private Logger logger = LoggerFactory.getLogger(SubmitOrder2.class);
    @Autowired
    private AccountConfig accountConfig;
    @Autowired
    private TradingDayService tradingDayService;
    @Autowired
    private StockService stockService;
    @Autowired
    private SubmitService submitService;
    @Autowired
    private OrderRecordService orderRecordService;

    @Test
    public  void execute() throws Exception {
        logger.info("== Job HandleInitOrders execute runs");
        // 是否为交易日
//        if(!tradingDayService.isTradingDay(LocalDate.now())){
//            return;//非交易日，则直接退出。
//        }

        //是否有要操作的stock
        List<Stock> stocks = stockService.listRunning();
        if(stocks.isEmpty()){
            return;//没有要操作的stock，直接退出
        }

        //生成订单数据
        final List<String> stockCodes = stocks.stream().map(Stock -> Stock.getCode()).collect(Collectors.toList());
        List<Submit> submits = submitService.listSumbit(stockCodes);
        if(submits.isEmpty()){
            return;//没有要操作的Submit，直接退出
        }

        // 提交订单
        Config config = this.accountConfig.accountConfig();
        try (TradeContext ctx = TradeContext.create(config).get()) {
            final List<OrderRecord> orderRecords = submitService.submitOrders(submits);
            orderRecordService.saveBatch(orderRecords);
        } catch (Exception e) {
            logger.warn("request Market Trading Days Fall:()",e.getMessage());
        }
    }
}

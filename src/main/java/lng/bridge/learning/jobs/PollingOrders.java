package lng.bridge.learning.jobs;

import com.longport.Config;
import com.longport.trade.OrderDetail;
import com.longport.trade.OrderSide;
import com.longport.trade.OrderStatus;
import com.longport.trade.TradeContext;
import lng.bridge.learning.config.AccountConfig;
import lng.bridge.learning.entity.OrderRecord;
import lng.bridge.learning.entity.Submit;
import lng.bridge.learning.service.OrderRecordService;
import lng.bridge.learning.service.SubmitService;
import lng.bridge.learning.service.TradingDayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * 轮询提交的订单
 * 非交易时间-退出
 * 交易时间-轮询提交的订单
 * 成交的订单-生成Buy 和 Sell 的新订单
 */
@Component
public class PollingOrders {
    private Logger logger = LoggerFactory.getLogger(PollingOrders.class);
    @Autowired
    private AccountConfig accountConfig;
    @Autowired
    private TradingDayService tradingDayService;
    @Autowired
    private OrderRecordService orderRecordService;
    @Autowired
    private SubmitService submitService;


    //秒 分 小时 日期 月份 星期 年份。
    @Scheduled(fixedDelay = 30000)
    public void execute() throws Exception {
        logger.info("== Job PollingOrders execute runs");
        // 是否为交易日
        if (!tradingDayService.isTradingDay(LocalDate.now())) {
            return;//非交易日，则直接退出。
        }

        //九点之前 或者 16点之后，就退出
        if (LocalTime.now().isBefore(LocalTime.of(9, 0, 0)) || LocalTime.now().isAfter(LocalTime.of(16, 0, 0))) {
            return;
            //12点之后 并且 13点之前，就退出
        } else if (LocalTime.now().isAfter(LocalTime.of(12, 0, 0)) && LocalTime.now().isBefore(LocalTime.of(13, 0, 0))) {
            return;
        }

        //跟踪今天的订单
        List<OrderRecord> orderRecords = orderRecordService.filterTodayNotFilledOrders(LocalDate.now());
        if (orderRecords.isEmpty()) {
            return;
        }

        Config config = this.accountConfig.accountConfig();
        try (TradeContext ctx = TradeContext.create(config).get()) {
            for (OrderRecord orderRecord : orderRecords) {
                OrderDetail detail = ctx.getOrderDetail(orderRecord.getOrderId()).get();
                if (OrderStatus.Filled == detail.getStatus()) {
                    orderRecordService.updateStatusFilled(orderRecord.getOrderId());
                    constructOrder(orderRecord.getStockCode(), detail.getPrice(), detail.getQuantity());
                }
                System.out.println(detail);
            }
        }
    }

    private void constructOrder(String stockCode, BigDecimal price, Long quantity) {
        final BigDecimal buyPrice = price.multiply(new BigDecimal("0.9"), new MathContext(2));
        final BigDecimal sellPrice = price.multiply(new BigDecimal("1.1"), new MathContext(2));
        final Submit buySubmit = new Submit(stockCode, OrderSide.Buy, buyPrice, quantity, null, LocalDateTime.now(), "");
        final Submit sellSubmit = new Submit(stockCode, OrderSide.Sell, sellPrice, quantity, null, LocalDateTime.now(), "");
        submitService.save(buySubmit);
        submitService.save(sellSubmit);
        submitService.submitOrder(buySubmit);
        submitService.submitOrder(sellSubmit);
        return;
    }
}

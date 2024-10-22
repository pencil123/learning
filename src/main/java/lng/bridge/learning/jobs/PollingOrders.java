package lng.bridge.learning.jobs;

import com.longport.Config;
import com.longport.trade.OrderDetail;
import com.longport.trade.OrderSide;
import com.longport.trade.OrderStatus;
import com.longport.trade.TradeContext;
import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lng.bridge.learning.config.AccountConfig;
import lng.bridge.learning.entity.Deal;
import lng.bridge.learning.entity.OrderRecord;
import lng.bridge.learning.entity.Submit;
import lng.bridge.learning.service.DealService;
import lng.bridge.learning.service.OrderRecordService;
import lng.bridge.learning.service.SubmitService;
import lng.bridge.learning.service.TradingDayService;
import lng.bridge.learning.utils.BeanConvertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 轮询提交的订单 非交易时间-退出 交易时间-轮询提交的订单 成交的订单-生成Buy 和 Sell 的新订单
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
    @Autowired
    private DealService dealService;


    //秒 分 小时 日期 月份 星期 年份。
    @Scheduled(fixedDelay = 30000)
    public void execute() throws Exception {
        logger.info("== Job PollingOrders execute runs");
        // 是否为交易日
        if (!tradingDayService.isTradingDay(LocalDate.now())) {
            return;//非交易日，则直接退出。
        }

        //九点之前 或者 16点之后，就退出
        if (LocalTime.now().isBefore(LocalTime.of(9, 0, 0)) || LocalTime.now()
                .isAfter(LocalTime.of(16, 0, 0))) {
            return;
            //12点之后 并且 13点之前，就退出
        } else if (LocalTime.now().isAfter(LocalTime.of(12, 0, 0)) && LocalTime.now()
                .isBefore(LocalTime.of(13, 0, 0))) {
            return;
        }

        //跟踪今天的订单
        List<OrderRecord> orderRecords = orderRecordService
                .filterTodayNotFilledOrders(LocalDate.now());
        if (orderRecords.isEmpty()) {
            return;
        }

        Config config = this.accountConfig.accountConfig();
        try (TradeContext ctx = TradeContext.create(config).get()) {
            for (OrderRecord orderRecord : orderRecords) {
                OrderDetail detail = ctx.getOrderDetail(orderRecord.getOrderId()).get();
                if (OrderStatus.Filled == detail.getStatus()) {
                    orderRecordService.updateStatusFilled(orderRecord.getOrderId());
                    Submit byId = submitService.getById(orderRecord.getSubmitId());
                    // 删除 Submit中的记录，在Deal 中添加记录
                    submitService.removeById(orderRecord.getSubmitId());
                    final Deal deal = BeanConvertUtils.copyProperties(byId, Deal.class);
                    final String dealStauts = deal.getOperate() == OrderSide.Buy ? "open" : "close";
                    deal.setStatus(dealStauts);
                    dealService.save(deal);
                    //如果 Subimt 为 Sell，则修改 linkBuy 记录的状态
                    if (deal.getOperate() == OrderSide.Sell) {
                        dealService.updateStatusClose(deal.getLinkBuy());
                    }
                    //如果操作为 sell，则生成一个买单
                    //如果操作为 Buy, 则生成一个Buy 和 一个 Sell
                    //继续生成新的订单
                    // TODO: 2024/10/16  当提交的价格和成交的价格不一致时，应该怎么处理？
                    constructOrder(deal);
                }
                logger.info("== Job PollingOrders get resp:{}", detail);
            }
        }
    }

    private void constructOrder(Deal deal) {
        BigDecimal buyPrice;
        if(deal.getOperate() == OrderSide.Buy){
            final BigDecimal sellPrice = deal.getTransactionPrice()
                    .multiply(new BigDecimal("1.1"), new MathContext(3));
            final Submit sellSubmit = new Submit(deal.getStockCode(), OrderSide.Sell, sellPrice,
                    deal.getTransactionAmount(), deal.getId(), LocalDateTime.now(), "");
            submitService.save(sellSubmit);
            submitService.submitOrder(sellSubmit);
        }
        if(deal.getOperate() == OrderSide.Sell){
            buyPrice = deal.getTransactionPrice()
                    .multiply(new BigDecimal("0.99"), new MathContext(3));
        }else {
            buyPrice = deal.getTransactionPrice()
                    .multiply(new BigDecimal("0.9"), new MathContext(3));
        }
        final Submit buySubmit = new Submit(deal.getStockCode(), OrderSide.Buy, buyPrice,
                deal.getTransactionAmount(), deal.getId(), LocalDateTime.now(), "");
        submitService.save(buySubmit);
        submitService.submitOrder(buySubmit);
        return;
    }
}

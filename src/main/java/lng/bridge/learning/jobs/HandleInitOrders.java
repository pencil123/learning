package lng.bridge.learning.jobs;

import com.longport.Config;
import com.longport.Market;
import com.longport.quote.MarketTradingDays;
import com.longport.quote.QuoteContext;
import com.longport.trade.*;
import lng.bridge.learning.config.AccountConfig;
import lng.bridge.learning.entity.Stock;
import lng.bridge.learning.entity.Submit;
import lng.bridge.learning.entity.TradingDay;
import lng.bridge.learning.enums.TradingDayTypeEnum;
import lng.bridge.learning.service.StockService;
import lng.bridge.learning.service.SubmitService;
import lng.bridge.learning.service.TradingDayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 交易日初始化提交订单
 * 1、检查当日是否为交易日
 * 2、提交当日初始化订单
 */
@Component
public class HandleInitOrders {
    private Logger logger = LoggerFactory.getLogger(HandleInitOrders.class);
    @Autowired
    private AccountConfig accountConfig;
    @Autowired
    private TradingDayService tradingDayService;
    @Autowired
    private StockService stockService;
    @Autowired
    private SubmitService submitService;

    //秒 分 小时 日期 月份 星期 年份。
    @Scheduled(cron = "0 16 11 * * ?")
    public void execute() {
        logger.info("== Job HandleInitOrders execute runs");
        // 是否为交易日
        if(!tradingDayService.isTradingDay(LocalDate.now())){
            return;//非交易日，则直接退出。
        }

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
            for(Submit submit:submits) {
                SubmitOrderOptions opts = new SubmitOrderOptions(submit.getStockCode(),
                        OrderType.ELO,
                        submit.getOperate(),
                        submit.getTransactionAmount(),
                        TimeInForceType.Day).setSubmittedPrice(submit.getTransactionPrice());
                SubmitOrderResponse resp = ctx.submitOrder(opts).get();
                System.out.println(resp);
            }
        } catch (Exception e) {
            logger.warn("request Market Trading Days Fall:()",e.getMessage());
        }
    }
}

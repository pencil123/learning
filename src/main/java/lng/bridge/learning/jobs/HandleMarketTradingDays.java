package lng.bridge.learning.jobs;

import com.longport.Config;
import com.longport.Market;
import com.longport.quote.MarketTradingDays;
import com.longport.quote.QuoteContext;
import lng.bridge.learning.config.AccountConfig;
import lng.bridge.learning.entity.TradingDay;
import lng.bridge.learning.enums.TradingDayTypeEnum;
import lng.bridge.learning.service.TradingDayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 维护交易日期
 * 每日调用接口查询当日日期是否为交易日
 */
@Component
public class HandleMarketTradingDays {
    private Logger logger = LoggerFactory.getLogger(HandleMarketTradingDays.class);

    @Autowired
    private AccountConfig accountConfig;
    @Autowired
    private TradingDayService tradingDayService;

    //秒 分 小时 日期 月份 星期 年份。
    @Scheduled(cron = "0 16 11 * * ?")
    public void execute() {
        logger.info("== Job HandleMarketTRADINGDays execute runs");
        Config config = this.accountConfig.accountConfig();
        List<TradingDay> tradingDays = new ArrayList<>();
        try (QuoteContext ctx = QuoteContext.create(config).get()) {
            MarketTradingDays resp = ctx
                    .getTradingDays(Market.HK, LocalDate.now(), LocalDate.now()).get();
            for (LocalDate localDate1 : resp.getTradingDays()) {
                tradingDays.add(new TradingDay(localDate1, TradingDayTypeEnum.FULL));
            }
            for (LocalDate localDate1 : resp.getHalfTradingDays()) {
                tradingDays.add(new TradingDay(localDate1, TradingDayTypeEnum.HALF));
            }
            tradingDayService.saveBatch(tradingDays);
            logger.info("Get Market Trading Days response:()", resp);
        } catch (Exception e) {
            logger.warn("request Market Trading Days Fall:()", e.getMessage());
        }
    }
}

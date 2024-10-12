package lng.bridge.learning.jobs;

import com.longport.Config;
import com.longport.Market;
import com.longport.quote.MarketTradingDays;
import com.longport.quote.QuoteContext;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lng.bridge.learning.config.AccountConfig;
import lng.bridge.learning.entity.TradingDay;
import lng.bridge.learning.enums.TradingDayTypeEnum;
import lng.bridge.learning.service.TradingDayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class HandleMarketTradingDays {

    @Autowired
    private AccountConfig accountConfig;
    @Autowired
    private TradingDayService tradingDayService;


    //秒 分 小时 日期 月份 星期 年份。
    @Scheduled(cron = "0 16 11 * * ?")
    public void execute() {
        Config config = this.accountConfig.accountConfig();
        LocalDate localDate = LocalDate.now();
        List<TradingDay> tradingDays= new ArrayList<>();
        try (QuoteContext ctx = QuoteContext.create(config).get()) {
            MarketTradingDays resp = ctx
                    .getTradingDays(Market.HK, LocalDate.now(), localDate.plusDays(2)).get();
            for(LocalDate localDate1 : resp.getTradingDays()){
                tradingDays.add(new TradingDay(localDate1, TradingDayTypeEnum.FULL));
            }
            for(LocalDate localDate1 : resp.getHalfTradingDays()){
                tradingDays.add(new TradingDay(localDate1,TradingDayTypeEnum.HALF));
            }
            tradingDayService.saveBatch(tradingDays);
            System.out.println(resp);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}

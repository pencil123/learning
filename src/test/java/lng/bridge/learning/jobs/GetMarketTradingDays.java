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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 获取资产总览
 */
//@RunWith(SpringRunner.class)
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class GetMarketTradingDays {

    @Autowired
    private AccountConfig accountConfig;
    @Autowired
    private TradingDayService tradingDayService;

    @Test
    public void getAccount() throws Exception {
        Config config = this.accountConfig.accountConfig();
        LocalDate localDate = LocalDate.now();
        List<TradingDay> tradingDays = new ArrayList<>();
        try (QuoteContext ctx = QuoteContext.create(config).get()) {
            MarketTradingDays resp = ctx
                    .getTradingDays(Market.HK, localDate.plusDays(1), localDate.plusDays(1)).get();
            for (LocalDate localDate1 : resp.getTradingDays()) {
                tradingDays.add(new TradingDay(localDate1, TradingDayTypeEnum.FULL));
            }
            for (LocalDate localDate1 : resp.getHalfTradingDays()) {
                tradingDays.add(new TradingDay(localDate1, TradingDayTypeEnum.HALF));
            }
            tradingDayService.saveBatch(tradingDays);
            System.out.println(resp);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

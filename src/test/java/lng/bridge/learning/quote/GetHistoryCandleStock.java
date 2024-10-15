package lng.bridge.learning.quote;


import com.longport.Config;
import com.longport.quote.AdjustType;
import com.longport.quote.Candlestick;
import com.longport.quote.Period;
import com.longport.quote.QuoteContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import lng.bridge.learning.config.AccountConfig;
import lng.bridge.learning.entity.CandleStickEntity;
import lng.bridge.learning.service.CandleStickEntityService;
import lng.bridge.learning.utils.BeanConvertUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 获取某个代码的蜡烛图；每个交易日会有 330 分钟，有 330 条记录
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GetHistoryCandleStock {

    private Logger logger = LoggerFactory.getLogger(GetHistoryCandleStock.class);
    @Autowired
    private AccountConfig accountConfig;
    @Autowired
    private CandleStickEntityService candleStickEntityService;

    /**
     * 没有输入日期信息 - 取最近的数据
     */
    @Test
    public void getCandlesticks() throws Exception {
        Config config = this.accountConfig.accountConfig();
        try (QuoteContext ctx = QuoteContext.create(config).get()) {
            Candlestick[] resp = ctx
                    .getCandlesticks("700.HK", Period.Min_1, 10, AdjustType.NoAdjust)
                    .get();
            for (Candlestick obj : resp) {
                System.out.println(obj);
            }
        }
    }


    /**
     * From Datetime  提供初始日期； 从初始日期往后取数
     */
    @Test
    public void getHistoryCandlesticksByOffset() throws Exception {
        Config config = this.accountConfig.accountConfig();
        try (QuoteContext ctx = QuoteContext.create(config).get()) {
            Candlestick[] resp = ctx
                    .getHistoryCandlesticksByOffset("700.HK", Period.Min_1, AdjustType.NoAdjust,
                            true,
                            LocalDateTime.of(2024, 10, 10, 0, 0, 0), 500)
                    .get();
            for (Candlestick obj : resp) {
                System.out.println(obj);
            }
        }
    }


    /**
     * start date 开始日期 end date 结束日期
     */
    @Test
    public void getHistoryCandlesticksByDate() throws Exception {
        Config config = this.accountConfig.accountConfig();
        final ArrayList<CandleStickEntity> candleStickEntities = new ArrayList<>();
        try (QuoteContext ctx = QuoteContext.create(config).get()) {
            LocalDate theDate = LocalDate.now();
            for (; theDate.isAfter(LocalDate.of(2019, 5, 20)); theDate = theDate.minusDays(1)) {
                try {
                    Candlestick[] resp = ctx
                            .getHistoryCandlesticksByDate("7500.HK", Period.Min_1,
                                    AdjustType.NoAdjust,
                                    theDate, theDate)
                            .get();
                    for (Candlestick obj : resp) {
                        CandleStickEntity candleStickEntity = BeanConvertUtils
                                .copyProperties(obj, CandleStickEntity.class);
                        candleStickEntity.setStockCode("7500.HK");
                        candleStickEntities.add(candleStickEntity);
                        System.out.println(obj);
                    }
                    candleStickEntityService.saveBatch(candleStickEntities);
                    candleStickEntities.clear();
                } catch (Exception e) {
                    logger.warn("the date:{};the error:{}", theDate,e.getMessage());
                }
            }
        }
    }
}

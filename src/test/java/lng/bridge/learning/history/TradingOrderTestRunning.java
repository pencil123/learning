package lng.bridge.learning.history;

import com.longport.trade.OrderSide;
import lng.bridge.learning.entity.CandleStickEntity;
import lng.bridge.learning.entity.Deal;
import lng.bridge.learning.entity.Submit;
import lng.bridge.learning.service.CandleStickEntityService;
import lng.bridge.learning.service.DealService;
import lng.bridge.learning.service.SubmitService;
import lng.bridge.learning.utils.BeanConvertUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TradingOrderTestRunning {
    private Logger logger = LoggerFactory.getLogger(TradingOrderTestRunning.class);
    @Autowired
    private CandleStickEntityService candleStickEntityService;
    @Autowired
    private SubmitService submitService;
    @Autowired
    private DealService dealService;

    /**
     * 逻辑还是存在不对的地方。 close 订单后，需要修改之前的买单（上一个订单的买入价格太低了）
     * @throws Exception
     */
    @Test
    public void execute() throws Exception {

        List<CandleStickEntity> candleStickEntities = candleStickEntityService.ListOrderByTime();
        for (CandleStickEntity candleStickEntity : candleStickEntities) {
            logger.info("the entity:{}",candleStickEntity);
            Map<String, Submit> stringSubmitMap = handleRealSubmit("7500.HK");
            if (candleStickEntity.getHigh().compareTo(stringSubmitMap.get("Buy").getTransactionPrice()) <= 0) {
                submitService.removeById(stringSubmitMap.get("Buy").getId());
                final Deal deal = BeanConvertUtils.copyProperties(stringSubmitMap.get("Buy"), Deal.class);
                deal.setStatus("open");
                dealService.save(deal);
                constructOrder(deal.getStockCode(), stringSubmitMap.get("Buy").getTransactionPrice(), stringSubmitMap.get("Buy").getTransactionAmount(),stringSubmitMap.get("Buy").getId(),candleStickEntity.getTimestamp());
            }
            if (stringSubmitMap.get("Sell") != null && candleStickEntity.getLow().compareTo(stringSubmitMap.get("Sell").getTransactionPrice()) >= 0) {
                submitService.removeById(stringSubmitMap.get("Sell").getId());
                final Deal deal = BeanConvertUtils.copyProperties(stringSubmitMap.get("Sell"), Deal.class);
                deal.setStatus("open");
                dealService.save(deal);
                dealService.updateStatusClose(deal.getLinkBuy());
            }
        }
    }

    private void constructOrder(String stockCode, BigDecimal price, Long quantity, Long linkId, OffsetDateTime timeStamp) {
        final BigDecimal buyPrice = price.multiply(new BigDecimal("0.9"), new MathContext(2));
        final BigDecimal sellPrice = price.multiply(new BigDecimal("1.1"), new MathContext(2));
        final Submit buySubmit = new Submit(stockCode, OrderSide.Buy, buyPrice, quantity, linkId, timeStamp.toLocalDateTime(), "");
        final Submit sellSubmit = new Submit(stockCode, OrderSide.Sell, sellPrice, quantity, linkId, timeStamp.toLocalDateTime(), "");
        submitService.save(buySubmit);
        submitService.save(sellSubmit);
        submitService.submitOrder(buySubmit);
        submitService.submitOrder(sellSubmit);
        return;
    }

    private Map<String, Submit> handleRealSubmit(String stockCode) {
        HashMap<String, Submit> stringSubmitHashMap = new HashMap<>();
        Submit maxSell = submitService.maxSell(stockCode);
        Submit minBuy = submitService.minBuy(stockCode);
        stringSubmitHashMap.put("Buy", minBuy);
        stringSubmitHashMap.put("Sell", maxSell);
        return stringSubmitHashMap;
    }
}

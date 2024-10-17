package lng.bridge.learning.history;

import com.longport.trade.OrderSide;
import java.sql.Timestamp;
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
        Map<String, Submit> stringSubmitMap;
        for (CandleStickEntity candleStickEntity : candleStickEntities) {
            logger.info("the entity:{}",candleStickEntity);
            stringSubmitMap = handleRealSubmit("7500.HK");
            logger.info("the buy price:{}",stringSubmitMap.get("Buy").getTransactionPrice());
            if (candleStickEntity.getHigh().compareTo(stringSubmitMap.get("Buy").getTransactionPrice()) <= 0) {
                submitService.removeById(stringSubmitMap.get("Buy").getId());
                final Deal deal = BeanConvertUtils.copyProperties(stringSubmitMap.get("Buy"), Deal.class);
                deal.setStatus("open");
                dealService.save(deal);
                //如果操作为 Buy, 则生成一个Buy 和 一个 Sell
                constructOrder(deal,candleStickEntity.getTimestamp());
            }
            if (stringSubmitMap.get("Sell") != null && candleStickEntity.getLow().compareTo(stringSubmitMap.get("Sell").getTransactionPrice()) >= 0) {
                logger.info("the Sell price:{}",stringSubmitMap.get("Sell").getTransactionPrice());
                submitService.removeById(stringSubmitMap.get("Sell").getId());
                final Deal deal = BeanConvertUtils.copyProperties(stringSubmitMap.get("Sell"), Deal.class);
                deal.setStatus("open");
                dealService.save(deal);
                dealService.updateStatusClose(deal.getLinkBuy());
                //如果操作为 sell，则生成一个买单
                constructOrder(deal,candleStickEntity.getTimestamp());
            }
        }
    }

    /**
     * 如果操作为 sell，则生成一个买单
     * 如果操作为 Buy, 则生成一个Buy 和 一个 Sell
     * @param deal
     * @param timestamp
     */

    private void constructOrder(Deal deal, OffsetDateTime timestamp) {
        BigDecimal buyPrice;
        if(deal.getOperate() == OrderSide.Buy){
            final BigDecimal sellPrice = deal.getTransactionPrice().multiply(new BigDecimal("1.1"), new MathContext(3));
            final Submit sellSubmit = new Submit(deal.getStockCode(), OrderSide.Sell, sellPrice, deal.getTransactionAmount(), deal.getId(),
                    timestamp.toLocalDateTime(), "");
            submitService.save(sellSubmit);
            submitService.submitOrder(sellSubmit);
        }
        if(deal.getOperate() == OrderSide.Sell){
            buyPrice  = deal.getTransactionPrice().multiply(new BigDecimal("0.99"), new MathContext(3));
        }else {
            buyPrice  = deal.getTransactionPrice().multiply(new BigDecimal("0.9"), new MathContext(3));
        }
        final Submit buySubmit = new Submit(deal.getStockCode(), OrderSide.Buy, buyPrice, deal.getTransactionAmount(), deal.getId(),
                timestamp.toLocalDateTime(), "");
        submitService.save(buySubmit);
        submitService.submitOrder(buySubmit);
        return;
    }

    private Map<String, Submit> handleRealSubmit(String stockCode) {
        HashMap<String, Submit> stringSubmitHashMap = new HashMap<>();
        Submit maxSell = submitService.minSell(stockCode);
        Submit minBuy = submitService.maxBuy(stockCode);
        stringSubmitHashMap.put("Buy", minBuy);
        stringSubmitHashMap.put("Sell", maxSell);
        return stringSubmitHashMap;
    }
}

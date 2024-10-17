package lng.bridge.learning.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.longport.Config;
import com.longport.trade.OrderStatus;
import com.longport.trade.OrderType;
import com.longport.trade.SubmitOrderOptions;
import com.longport.trade.SubmitOrderResponse;
import com.longport.trade.TimeInForceType;
import com.longport.trade.TradeContext;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lng.bridge.learning.config.AccountConfig;
import lng.bridge.learning.dao.SubmitMapper;
import lng.bridge.learning.entity.OrderRecord;
import lng.bridge.learning.entity.Submit;
import lng.bridge.learning.service.SubmitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubmitServiceImpl extends ServiceImpl<SubmitMapper, Submit> implements SubmitService {

    private Logger logger = LoggerFactory.getLogger(SubmitService.class);
    @Autowired
    private AccountConfig accountConfig;

    @Override
    public List<Submit> listSumbit(List<String> stockCodes) {
        QueryWrapper<Submit> submitQueryWrapper = new QueryWrapper<>();
        submitQueryWrapper.in("stock_code", stockCodes);
        return list(submitQueryWrapper);
    }

    @Override
    public OrderRecord submitOrder(Submit submit) {
        Config config = this.accountConfig.accountConfig();
        try (TradeContext ctx = TradeContext.create(config).get()) {
            SubmitOrderOptions opts = new SubmitOrderOptions(submit.getStockCode(),
                    OrderType.ELO,
                    submit.getOperate(),
                    submit.getTransactionAmount(),
                    TimeInForceType.Day).setSubmittedPrice(submit.getTransactionPrice());
            SubmitOrderResponse resp = ctx.submitOrder(opts).get();
            return new OrderRecord(resp.getOrderId(), LocalDate.now(), submit.getId(),
                    OrderStatus.Unknown);
        } catch (Exception e) {
            logger.warn("submit order:()", e.getMessage());
        }
        return null;
    }


    @Override
    public List<OrderRecord> submitOrders(List<Submit> submits) {
        Config config = this.accountConfig.accountConfig();
        ArrayList<OrderRecord> orderRecords = new ArrayList<OrderRecord>();
        try (TradeContext ctx = TradeContext.create(config).get()) {
            for (Submit submit : submits) {
                SubmitOrderOptions opts = new SubmitOrderOptions(submit.getStockCode(),
                        OrderType.ELO,
                        submit.getOperate(),
                        submit.getTransactionAmount(),
                        TimeInForceType.Day).setSubmittedPrice(submit.getTransactionPrice());
                SubmitOrderResponse resp = ctx.submitOrder(opts).get();
                orderRecords.add(new OrderRecord(resp.getOrderId(), LocalDate.now(),
                        submit.getId(), OrderStatus.Unknown));
            }
            return orderRecords;
        } catch (Exception e) {
            logger.warn("submit orders : ()", e.getMessage());
        }
        return orderRecords;
    }


    @Override
    public Submit maxBuy(String stockCode) {
        QueryWrapper<Submit> submitQueryWrapper = new QueryWrapper<>();
        submitQueryWrapper.eq("stock_code", stockCode);
        submitQueryWrapper.eq("operate","Buy");
        submitQueryWrapper.orderByDesc("transaction_price");
        submitQueryWrapper.last("limit 1");
        return getOne(submitQueryWrapper);
    }

    @Override
    public Submit minSell(String stockCode) {
        QueryWrapper<Submit> submitQueryWrapper = new QueryWrapper<>();
        submitQueryWrapper.eq("stock_code", stockCode);
        submitQueryWrapper.eq("operate","Sell");
        submitQueryWrapper.orderByAsc("transaction_price");
        submitQueryWrapper.last("limit 1");
        return getOne(submitQueryWrapper);
    }
}

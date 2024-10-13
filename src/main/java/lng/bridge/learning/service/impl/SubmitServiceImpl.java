package lng.bridge.learning.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.longport.Config;
import com.longport.trade.*;
import lng.bridge.learning.config.AccountConfig;
import lng.bridge.learning.dao.SubmitMapper;
import lng.bridge.learning.entity.OrderRecord;
import lng.bridge.learning.entity.Submit;
import lng.bridge.learning.service.SubmitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
            return new OrderRecord(resp.getOrderId(), LocalDate.now(), submit.getStockCode());
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
                orderRecords.add(new OrderRecord(resp.getOrderId(), LocalDate.now(), submit.getStockCode()));
            }
            return orderRecords;
        } catch (Exception e) {
            logger.warn("submit orders : ()", e.getMessage());
        }
        return orderRecords;
    }
}

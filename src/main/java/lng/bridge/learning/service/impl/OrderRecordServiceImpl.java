package lng.bridge.learning.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.longport.trade.OrderStatus;
import lng.bridge.learning.dao.OrderRecordMapper;
import lng.bridge.learning.entity.OrderRecord;
import lng.bridge.learning.service.OrderRecordService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class OrderRecordServiceImpl extends ServiceImpl<OrderRecordMapper, OrderRecord> implements OrderRecordService {
    @Override
    public List<OrderRecord> filterTodayNotFilledOrders(LocalDate theDate) {
        QueryWrapper<OrderRecord> orderRecordQueryWrapper = new QueryWrapper<>();
        orderRecordQueryWrapper.eq("the_date", theDate);
        orderRecordQueryWrapper.ne("status", OrderStatus.Filled);
        return list(orderRecordQueryWrapper);
    }

    @Override
    public Boolean updateStatusFilled(String orderId) {
        OrderRecord orderRecord = new OrderRecord();
        orderRecord.setStatus(OrderStatus.Filled);
        UpdateWrapper<OrderRecord> orderRecordUpdateWrapper = new UpdateWrapper<>();
        orderRecordUpdateWrapper.eq("order_id",orderId);
        return update(orderRecord,orderRecordUpdateWrapper);
    }
}

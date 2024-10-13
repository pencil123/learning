package lng.bridge.learning.service;

import com.baomidou.mybatisplus.extension.service.IService;
import lng.bridge.learning.entity.OrderRecord;

import java.time.LocalDate;
import java.util.List;

public interface OrderRecordService extends IService<OrderRecord> {
    List<OrderRecord> filterTodayOrders(LocalDate theDate);
}

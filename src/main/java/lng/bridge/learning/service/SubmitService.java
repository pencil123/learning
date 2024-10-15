package lng.bridge.learning.service;

import com.baomidou.mybatisplus.extension.service.IService;
import lng.bridge.learning.entity.OrderRecord;
import lng.bridge.learning.entity.Submit;

import java.util.List;

public interface SubmitService extends IService<Submit> {

    List<Submit> listSumbit(List<String> stockCodes);

    OrderRecord submitOrder(Submit submit);

    List<OrderRecord> submitOrders(List<Submit> submits);

    Submit minBuy(String stockCode);
    Submit maxSell(String stockCode);

}

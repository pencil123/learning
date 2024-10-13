package lng.bridge.learning.service;

import com.baomidou.mybatisplus.extension.service.IService;
import lng.bridge.learning.entity.Deal;
import lng.bridge.learning.entity.Stock;

import java.util.List;

public interface StockService extends IService<Stock> {
    List<Stock> listRunning();

}

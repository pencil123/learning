package lng.bridge.learning.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lng.bridge.learning.dao.StockMapper;
import lng.bridge.learning.entity.Stock;
import lng.bridge.learning.service.StockService;
import org.springframework.stereotype.Component;

@Component
public class StockServiceImpl extends ServiceImpl<StockMapper, Stock> implements StockService {

}

package lng.bridge.learning.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lng.bridge.learning.dao.StockMapper;
import lng.bridge.learning.entity.Stock;
import lng.bridge.learning.service.StockService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockServiceImpl extends ServiceImpl<StockMapper, Stock> implements StockService {

    @Override
    public List<Stock> listRunning() {
        QueryWrapper<Stock> stockQueryWrapper = new QueryWrapper<>();
        stockQueryWrapper.eq("status", "running");
        return list(stockQueryWrapper);
    }
}

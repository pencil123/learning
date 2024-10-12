package lng.bridge.learning.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lng.bridge.learning.dao.TradingDayMapper;
import lng.bridge.learning.entity.TradingDay;
import lng.bridge.learning.service.TradingDayService;
import org.springframework.stereotype.Component;

@Component
public class TradingDayServiceImpl extends ServiceImpl<TradingDayMapper, TradingDay> implements TradingDayService {

}

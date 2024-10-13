package lng.bridge.learning.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lng.bridge.learning.dao.TradingDayMapper;
import lng.bridge.learning.entity.TradingDay;
import lng.bridge.learning.service.TradingDayService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class TradingDayServiceImpl extends ServiceImpl<TradingDayMapper, TradingDay> implements TradingDayService {

    @Override
    public Boolean isTradingDay(LocalDate theDate) {
        QueryWrapper<TradingDay> tradingDayQueryWrapper = new QueryWrapper<>();
        tradingDayQueryWrapper.eq("the_date", theDate);
        if (null == getOne(tradingDayQueryWrapper, false)) {
            return false;
        } else {
            return true;
        }
    }
}

package lng.bridge.learning.service;

import com.baomidou.mybatisplus.extension.service.IService;
import lng.bridge.learning.entity.Stock;
import lng.bridge.learning.entity.TradingDay;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public interface TradingDayService extends IService<TradingDay> {
    Boolean isTradingDay(LocalDate theDate);
}

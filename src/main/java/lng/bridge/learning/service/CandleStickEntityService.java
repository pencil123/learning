package lng.bridge.learning.service;

import com.baomidou.mybatisplus.extension.service.IService;
import lng.bridge.learning.entity.CandleStickEntity;

import java.util.List;

public interface CandleStickEntityService extends IService<CandleStickEntity> {
    List<CandleStickEntity> ListOrderByTime();

}

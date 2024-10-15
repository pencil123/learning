package lng.bridge.learning.service;

import com.baomidou.mybatisplus.extension.service.IService;
import lng.bridge.learning.entity.Deal;

public interface DealService extends IService<Deal> {
    Boolean updateStatusClose(Long id);

}

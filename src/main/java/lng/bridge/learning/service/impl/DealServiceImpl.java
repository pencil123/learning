package lng.bridge.learning.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lng.bridge.learning.dao.DealMapper;
import lng.bridge.learning.entity.Deal;
import lng.bridge.learning.service.DealService;
import org.springframework.stereotype.Component;

@Component
public class DealServiceImpl extends ServiceImpl<DealMapper, Deal> implements DealService {

}

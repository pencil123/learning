package lng.bridge.learning.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lng.bridge.learning.dao.CandleStickEntityMapper;
import lng.bridge.learning.entity.CandleStickEntity;
import lng.bridge.learning.service.CandleStickEntityService;
import org.springframework.stereotype.Service;

@Service
public class CandleStickEntityServiceImpl extends ServiceImpl<CandleStickEntityMapper, CandleStickEntity> implements CandleStickEntityService {

}

package lng.bridge.learning.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lng.bridge.learning.dao.CandleStickEntityMapper;
import lng.bridge.learning.entity.CandleStickEntity;
import lng.bridge.learning.service.CandleStickEntityService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CandleStickEntityServiceImpl extends ServiceImpl<CandleStickEntityMapper, CandleStickEntity> implements CandleStickEntityService {

    @Override
    public List<CandleStickEntity> ListOrderByTime() {
        QueryWrapper<CandleStickEntity> candleStickEntityQueryWrapper = new QueryWrapper<>();
        candleStickEntityQueryWrapper.orderByAsc("timestamp");
        return list(candleStickEntityQueryWrapper);
    }
}

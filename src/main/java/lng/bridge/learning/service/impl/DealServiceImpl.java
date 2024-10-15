package lng.bridge.learning.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lng.bridge.learning.dao.DealMapper;
import lng.bridge.learning.entity.Deal;
import lng.bridge.learning.service.DealService;
import org.springframework.stereotype.Service;

@Service
public class DealServiceImpl extends ServiceImpl<DealMapper, Deal> implements DealService {

    @Override
    public Boolean updateStatusClose(Long id) {
        Deal deal = new Deal();
        deal.setStatus("close");
        deal.setId(id);
        UpdateWrapper<Deal> dealUpdateWrapper = new UpdateWrapper<>();
        dealUpdateWrapper.eq("id",id);
        return update(deal,dealUpdateWrapper);
    }
}

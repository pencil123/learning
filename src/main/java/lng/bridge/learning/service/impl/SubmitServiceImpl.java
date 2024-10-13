package lng.bridge.learning.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lng.bridge.learning.dao.SubmitMapper;
import lng.bridge.learning.entity.Submit;
import lng.bridge.learning.service.SubmitService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubmitServiceImpl extends ServiceImpl<SubmitMapper, Submit> implements SubmitService {

    @Override
    public List<Submit> listSumbit(List<String> stockCodes) {
        QueryWrapper<Submit> submitQueryWrapper = new QueryWrapper<>();
        submitQueryWrapper.in("stock_code",stockCodes);
        return list(submitQueryWrapper);
    }
}

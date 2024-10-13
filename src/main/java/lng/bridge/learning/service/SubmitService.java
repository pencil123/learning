package lng.bridge.learning.service;

import com.baomidou.mybatisplus.extension.service.IService;
import lng.bridge.learning.entity.Stock;
import lng.bridge.learning.entity.Submit;

import java.util.List;

public interface SubmitService extends IService<Submit> {

    List<Submit> listSumbit(List<String> stockCodes);

}

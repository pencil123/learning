package lng.bridge.learning.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import lng.bridge.learning.entity.Deal;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface DealMapper extends BaseMapper<Deal> {

}

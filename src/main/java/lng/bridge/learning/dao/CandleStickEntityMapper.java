package lng.bridge.learning.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import lng.bridge.learning.entity.CandleStickEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface CandleStickEntityMapper extends BaseMapper<CandleStickEntity> {

}

package lng.bridge.learning.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import lng.bridge.learning.entity.Submit;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface SubmitMapper extends BaseMapper<Submit> {

}

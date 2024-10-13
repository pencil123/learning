package lng.bridge.learning.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 操作的stock 列表
 */
@Data
public class Stock {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String code;
    private String name;
    private LocalDateTime addDate;
    private String status;
    private LocalDateTime updateDate;
    private Long amount;
    private String notes;
}

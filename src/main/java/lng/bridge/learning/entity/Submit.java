package lng.bridge.learning.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class Submit {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String stockCode;
    private String operate;
    private BigDecimal transactionPrice;
    private BigDecimal transactionAmount;
    private Long linkBuy;
    private LocalDateTime addDate;
    private String notes;
}

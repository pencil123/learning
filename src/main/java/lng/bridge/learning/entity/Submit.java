package lng.bridge.learning.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.longport.trade.OrderSide;
import lombok.Data;
/**
 * 提交了未成交的订单
 */
@Data
public class Submit {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String stockCode;
    private OrderSide operate;
    private BigDecimal transactionPrice;
    private Long transactionAmount;
    private Long linkBuy;
    private LocalDateTime addDate;
    private String notes;
}

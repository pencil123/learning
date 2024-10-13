package lng.bridge.learning.entity;


import com.longport.trade.OrderSide;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 成交了的订单
 */
@Data
public class Deal {
    private Long id;
    private String stockCode;
    private OrderSide operate;
    private BigDecimal transactionPrice;
    private Long transactionAmount;
    private Long linkBuy;
    private String status;
    private LocalDateTime addDate;
    private String notes;
}

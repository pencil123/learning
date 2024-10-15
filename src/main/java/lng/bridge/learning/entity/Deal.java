package lng.bridge.learning.entity;


import com.longport.trade.OrderSide;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.NoArgsConstructor;

/**
 * 成交了的订单
 */
@Data
@NoArgsConstructor
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

    public Deal(String stockCode, OrderSide operate, BigDecimal transactionPrice,
            Long transactionAmount, Long linkBuy, String status, LocalDateTime addDate,
            String notes) {
        this.stockCode = stockCode;
        this.operate = operate;
        this.transactionPrice = transactionPrice;
        this.transactionAmount = transactionAmount;
        this.linkBuy = linkBuy;
        this.status = status;
        this.addDate = addDate;
        this.notes = notes;
    }
}

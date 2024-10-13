package lng.bridge.learning.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.longport.trade.OrderSide;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    public Submit() {
    }

    public Submit(String stockCode, OrderSide operate, BigDecimal transactionPrice, Long transactionAmount, Long linkBuy, LocalDateTime addDate, String notes) {
        this.stockCode = stockCode;
        this.operate = operate;
        this.transactionPrice = transactionPrice;
        this.transactionAmount = transactionAmount;
        this.linkBuy = linkBuy;
        this.addDate = addDate;
        this.notes = notes;
    }
}

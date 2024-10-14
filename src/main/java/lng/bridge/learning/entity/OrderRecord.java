package lng.bridge.learning.entity;


import com.longport.trade.OrderSide;
import com.longport.trade.OrderStatus;
import lombok.Data;

import java.time.LocalDate;

/**
 * 订单记录
 */
@Data
public class OrderRecord {
    private String orderId;
    private LocalDate theDate;
    private String stockCode;
    private OrderStatus status;

    public OrderRecord() {
    }

    public OrderRecord(String orderId, LocalDate theDate, String stockCode,OrderStatus status) {
        this.orderId = orderId;
        this.theDate = theDate;
        this.stockCode = stockCode;
        this.status = status;
    }
}

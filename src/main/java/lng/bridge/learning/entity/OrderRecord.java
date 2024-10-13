package lng.bridge.learning.entity;


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

    public OrderRecord() {
    }

    public OrderRecord(String orderId, LocalDate theDate, String stockCode) {
        this.orderId = orderId;
        this.theDate = theDate;
        this.stockCode = stockCode;
    }
}

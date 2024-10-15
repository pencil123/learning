package lng.bridge.learning.entity;


import com.longport.trade.OrderSide;
import com.longport.trade.OrderStatus;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import lombok.NoArgsConstructor;

/**
 * 订单记录
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRecord {
    private String orderId;
    private LocalDate theDate;
    private Long submitId;
    private OrderStatus status;
}

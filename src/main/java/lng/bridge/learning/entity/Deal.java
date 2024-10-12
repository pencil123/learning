package lng.bridge.learning.entity;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class Deal {

    private Long id;
    private String stockCode;
    private String operate;
    private BigDecimal transactionPrice;
    private BigDecimal transactionAmount;
    private Long linkBuy;
    private String status;
    private LocalDateTime addDate;
    private String notes;
}

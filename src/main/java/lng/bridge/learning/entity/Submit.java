package lng.bridge.learning.entity;


import java.math.BigDecimal;
import java.time.LocalDate;
public class Submit {
    private Long id;
    private String stockCode;
    private String operate;
    private BigDecimal transactionPrice;
    private BigDecimal transactionAmount;
    private Long linkBuy;
    private LocalDate addDate;
    private String notes;
}

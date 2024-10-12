package lng.bridge.learning.entity;

import java.time.LocalDate;
import lng.bridge.learning.enums.TradingDayTypeEnum;
import lombok.Data;

@Data
public class TradingDay {
    private LocalDate theDate;
    private TradingDayTypeEnum tradingDayType;
    public TradingDay(){}

    public TradingDay(LocalDate theDate, TradingDayTypeEnum tradingDayType) {
        this.theDate = theDate;
        this.tradingDayType = tradingDayType;
    }
}

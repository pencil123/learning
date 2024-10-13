package lng.bridge.learning.entity;

import lng.bridge.learning.enums.TradingDayTypeEnum;
import lombok.Data;

import java.time.LocalDate;

/**
 * 交易日
 */
@Data
public class TradingDay {
    private LocalDate theDate;
    private TradingDayTypeEnum tradingDayType;

    public TradingDay() {
    }

    public TradingDay(LocalDate theDate, TradingDayTypeEnum tradingDayType) {
        this.theDate = theDate;
        this.tradingDayType = tradingDayType;
    }
}

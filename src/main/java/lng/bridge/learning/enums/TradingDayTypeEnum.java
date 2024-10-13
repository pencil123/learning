package lng.bridge.learning.enums;

import lombok.Getter;

@Getter
public enum TradingDayTypeEnum {
    HALF("半日交易"),
    FULL("交易日");

    private final String description;

    TradingDayTypeEnum(String description) {
        this.description = description;
    }
}

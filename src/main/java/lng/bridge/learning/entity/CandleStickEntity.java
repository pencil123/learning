package lng.bridge.learning.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.longport.quote.Candlestick;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import lombok.Data;

/**
 * 股票蜡烛图
 */
@Data
@TableName("candle_stick")
public class CandleStickEntity extends Candlestick {
    private String stockCode;
    private BigDecimal close;
    private BigDecimal open;
    private BigDecimal low;
    private BigDecimal high;
    private long volume;
    private BigDecimal turnover;
    private OffsetDateTime timestamp;
}

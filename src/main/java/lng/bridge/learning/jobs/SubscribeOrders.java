package lng.bridge.learning.jobs;

import lng.bridge.learning.config.AccountConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 订阅 - 交易和资产类的私有通知
 */
@Component
public class SubscribeOrders {
    private Logger logger = LoggerFactory.getLogger(SubscribeOrders.class);
    @Autowired
    private AccountConfig accountConfig;

    //秒 分 小时 日期 月份 星期 年份。
    @Scheduled(cron = "0 16 11 * * ?")
    public void execute() {
        logger.info("== Job SubscribeOrders execute runs");
    }
}

package lng.bridge.learning.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class SubscribeOrders {
    private Logger logger = LoggerFactory.getLogger(SubscribeOrders.class);
    @Autowired
    private AccountConfig accountConfig;

}

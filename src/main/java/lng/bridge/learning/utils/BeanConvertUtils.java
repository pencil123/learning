package lng.bridge.learning.utils;

import com.longport.quote.Candlestick;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

/**
 * Bean转换
 *
 * @author 20113368
 * @date 2021/1/19 16:43
 */
public class BeanConvertUtils {

    private static final Logger logger = LoggerFactory.getLogger(BeanConvertUtils.class);

    private BeanConvertUtils() {
    }

    /**
     * @param source Object
     * @param targetClass Class
     * @param <T> T
     * @return T
     */
    public static <T> T copyProperties(Object source, Class<T> targetClass) {
        T doInstance = null;
        try {
            doInstance = targetClass.newInstance();
            BeanUtils.copyProperties(source, doInstance);
        } catch (Exception e) {
            logger.error("copyProperties error:{}", e);
        }
        return doInstance;
    }


    public static <T> T TransformerString(String value, Class<T> clazz) {
        System.out.println(clazz.getTypeName());
        System.out.println(Integer.class.getTypeName());
        if (clazz.getClass().equals(Integer.class)) {
            System.out.println("测试");
            return (T) Integer.valueOf(value);
        }
        return null;
    }
}


import com.keafmd.service.ProduceService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @ Author     ：scw
 * @ Date       ：Created in 下午 2:27 2018/7/15 0015
 * @ Description：生产者的测试
 * @ Modified By：
 * @Version: $version$
 */
public class ProducerTest {
    public static void main(String[] args){
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("classpath:producer.xml");
        ProduceService bean = classPathXmlApplicationContext.getBean(ProduceService.class);
        //进行发送消息
        for (int i = 0; i < 100 ; i++) {
            bean.sendMessage("test" + i);
        }
        //当消息发送完后，关闭容器
        classPathXmlApplicationContext.close();
    }
}


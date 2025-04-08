import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import javax.annotation.Resource;

@SpringBootTest
public class SpringbootWebTest {

    @Resource
    private ApplicationContext applicationContext;
    @Test
    public void testScope() {
        for (int i = 0; i < 10; i++) {
            Object cartController = applicationContext.getBean("mybatisPlusInterceptor");
            System.out.println(cartController);
        }
    }
}

import com.lagou.edu.AutoDeliverApplication8090;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@SpringBootTest(classes = {AutoDeliverApplication8090.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class AutodeliverApplicationTest {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Test
    public void testInstanceMetaData(){
        List<ServiceInstance> instances = discoveryClient.getInstances("lagou-service-resume");
        // 2. 如果有多个实例，选择一个使用;LoadBalance
        ServiceInstance serviceInstance = instances.get(0);
        System.out.println(serviceInstance);
    }

}

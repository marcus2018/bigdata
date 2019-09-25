import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class test {
    @Autowired
    private RedisTemplate redisTemplate;
    @Test
    public  void test(){
        SetOperations<String, String> set = redisTemplate.opsForSet();

        set.add("set1","22");

        set.add("set1","33");

        set.add("set1","44");

        Set<String> resultSet =redisTemplate.opsForSet().members("set1");
    }
    public static void main(String[] args) {

    }
}

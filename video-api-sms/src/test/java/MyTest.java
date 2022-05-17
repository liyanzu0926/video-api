import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;

/**
 * @Description
 * @Author lww
 * @Date 2022/5/17 17:38
 */
public class MyTest {

    @Test
    public void test1(){
        String s1 = RandomStringUtils.randomNumeric(4);
        System.out.println(s1);
    }
}

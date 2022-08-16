package scheduler.tasking;

import io.github.talelin.latticy.LatticyApplication;
import io.github.talelin.latticy.scheduler.tasking.CoreIndexBack;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LatticyApplication.class)
public class CoreIndexBackTest {

    @Autowired
    private CoreIndexBack coreIndexBack;

    @Test
    public void test1() throws Exception {
        coreIndexBack.createOrUpdateCoreIndexBack();
    }

}

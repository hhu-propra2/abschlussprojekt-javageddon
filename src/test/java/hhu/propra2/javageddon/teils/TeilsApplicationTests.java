package hhu.propra2.javageddon.teils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-dev.properties")
@SpringBootTest

public class TeilsApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Test
	public void thisTestHaveToFail(){
		assertThat(false).isEqualTo(true);
	}

}


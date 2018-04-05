import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jms.annotation.EnableJms;

/**
 * Created by mihailkopchev on 4/5/18.
 */

@SpringBootApplication(scanBasePackages = "com.misho")
@EnableJms
public class JmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(JmsApplication.class, args);
    }
}

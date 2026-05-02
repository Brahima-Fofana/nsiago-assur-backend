package ci.nsiago.assur;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class NsiagoApplication {

    public static void main(String[] args) {
        SpringApplication.run(NsiagoApplication.class, args);
    }

}

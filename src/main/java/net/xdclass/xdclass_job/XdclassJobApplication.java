package net.xdclass.xdclass_job;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan(basePackages = "net.xdclass.xdclass_job.domain")
@SpringBootApplication
public class XdclassJobApplication {

	public static void main(String[] args) {
		SpringApplication.run(XdclassJobApplication.class, args);
	}

}

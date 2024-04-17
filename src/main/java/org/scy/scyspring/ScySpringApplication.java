package org.scy.scyspring;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.scy.scyspring.core.mapper")
public class ScySpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScySpringApplication.class, args);
    }

}

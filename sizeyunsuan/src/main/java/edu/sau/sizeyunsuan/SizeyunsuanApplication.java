package edu.sau.sizeyunsuan;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@MapperScan("edu.sau.sizeyunsuan.mapper")
@SpringBootApplication
public class SizeyunsuanApplication {

    public static void main(String[] args) {
        SpringApplication.run(SizeyunsuanApplication.class, args);
    }

}

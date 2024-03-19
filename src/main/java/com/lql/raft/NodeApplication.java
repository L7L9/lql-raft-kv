package com.lql.raft;

import com.lql.raft.utils.SpringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author lql
 * @date 2024/01/09
 */
@SpringBootApplication
public class NodeApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(NodeApplication.class, args);
        SpringUtils.setContext(applicationContext);
    }
}

package com.mzlalal.multithread;

import com.mzlalal.multithread.dao.PersonDAO;
import com.mzlalal.multithread.utils.ConsumerTestUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class MultiThreadApplication {

    public static void main(String[] args) {
        // 获取返回的上下文
        ConfigurableApplicationContext context = SpringApplication.run(MultiThreadApplication.class, args);

        // 添加测试数组到list
        List<String> uuidList = new ArrayList<>(1000);
        for (int i = 0; i < 1000; i++) {
            uuidList.add(UUID.randomUUID().toString());
        }

        // 实例化dao
        PersonDAO personDAO = context.getBean(PersonDAO.class);

        ConsumerTestUtil.test1(uuidList, personDAO);
        ConsumerTestUtil.test2(uuidList, personDAO);
    }
}

package com.mzlalal.multithread;

import com.mzlalal.multithread.utils.thread.ConsumerMethodsUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootApplication
public class MultiThreadApplication {

    public static void main(String[] args) {
        // 获取返回的上下文
        ConfigurableApplicationContext context = SpringApplication.run(MultiThreadApplication.class, args);
        ConsumerMethodsUtil consumerMethodsUtil = context.getBean(ConsumerMethodsUtil.class);

        // 添加测试数组到list
        List<String> uuidList = new ArrayList<>(1000);
        for (int i = 0; i < 1000; i++) {
            uuidList.add(UUID.randomUUID().toString());
        }
        // 根据参数查询方法
        Class[] clszz = {List.class, AtomicInteger.class};

        // 参数值
        Object[] parameters = {uuidList, new AtomicInteger(0)};
        // 开始测试
        consumerMethodsUtil.invokeConsumerMethod(uuidList, "consumerUUIDList", clszz, parameters);
//        consumerTestUtil.test2(uuidList, personDAO);
    }
}

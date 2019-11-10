package com.mzlalal.multithread;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest
public class MultiThreadApplicationTests {

    @Test
    public void contextLoads() {
        new Thread(() -> System.out.println("lambda")).start();
    }

    @Test
    public void lambda() {
        List<Integer> integerList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            integerList.add(i);
        }
        AtomicInteger atomicInt = new AtomicInteger(0);
        ExecutorService executor = Executors.newFixedThreadPool(4);

        for (int j = 0; j < 4; j++) {
            executor.execute(new Thread(() -> {
                while (atomicInt.get() < integerList.size()) {
                    System.out.println("当前条件参数状态:" + atomicInt.get() + "\t\t" + integerList.size());
                    System.out.println(Thread.currentThread().getName()
                            + "正在消费:" + integerList.get(atomicInt.getAndIncrement()));
                }
            }));
        }
    }

}


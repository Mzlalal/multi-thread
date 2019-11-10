package com.mzlalal.multithread.utils;

import com.mzlalal.multithread.dao.PersonDAO;
import com.mzlalal.multithread.entity.Person;
import com.mzlalal.multithread.factory.MzThreadFactory;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 测试
 */
public class ConsumerTestUtil {

    /**
     * 多线程测试
     *
     * @param personDAO
     */
    public static void test1(List<String> uuidList, PersonDAO personDAO) {

        // 设置与数组相同的信号量
        Semaphore semaphore = new Semaphore(uuidList.size());

        // 初始化线程池 初始化 0 最大4 存活时间 60s
        ExecutorService executor = new ThreadPoolExecutor(0, 8,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(),
                new MzThreadFactory());
        // 设置一个全局获取下标的atomicint
        AtomicInteger atomicInt = new AtomicInteger(0);

        // 开启四个线程
        for (int j = 0; j < 8; j++) {
            executor.execute(() -> {
                // 开始时间
                long startTime = System.currentTimeMillis();
                // 进行消费计数
                AtomicInteger count = new AtomicInteger(0);
                while (semaphore.availablePermits() > 0) {
                    // 消费一个信号
                    try {
                        semaphore.acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // 新增一个person对象 从uuidList中获取数据, atomic获取下标后立即执行
                    // atomicint的get方法不是线程安全
                    Person person = new Person();
                    person.setName(uuidList.get(atomicInt.getAndIncrement()));
                    person.setC_name(Thread.currentThread().getName());
                    // 保存到数据库
                    personDAO.save(person);
                    // 计数
                    count.incrementAndGet();
                }
                System.out.println(String.format("%s共计消费%s条", Thread.currentThread().getName(), count.get()));

                // 结束时间
                long endTime = System.currentTimeMillis();

                System.out.println(String.format("test1总耗时:%d", (endTime - startTime)));
            });
        }

    }

    /**
     * 多线程测试
     *
     * @param personDAO
     */
    public static void test2(List<String> uuidList, PersonDAO personDAO) {
        // 开始时间
        long startTime = System.currentTimeMillis();

        for (String temp : uuidList) {
            // 新增一个person对象 从uuidList中获取数据, atomic获取下标后立即执行
            Person person = new Person();
            person.setName(temp);
            person.setC_name(Thread.currentThread().getName());
            // 保存到数据库
            personDAO.save(person);
        }
        // 结束时间
        long endTime = System.currentTimeMillis();

        System.out.println(String.format("test2总耗时:%d", (endTime - startTime)));
    }
}

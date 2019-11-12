package com.mzlalal.multithread.utils.thread;

import com.mzlalal.multithread.factory.MzThreadFactory;
import com.mzlalal.multithread.utils.common.SpringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.*;

/**
 * 测试
 */
@Component
public class ThreadConsumerUtil {

    @Value("${consumer.thread.number}")
    private int threadNumber;

    /**
     * 多线程测试
     *
     * @param size 数字大小
     * @param method 消费方法
     * @param parameters 参数
     */
    public void consumer(int size, Method method, Object[] parameters) {

        // 设置与数组相同的信号量
        Semaphore semaphore = new Semaphore(size);

        // 初始化线程池 初始化 0 最大4 存活时间 60s
        ExecutorService executor = new ThreadPoolExecutor(0, threadNumber,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(),
                new MzThreadFactory());

        // 开启四个线程
        for (int j = 0; j < threadNumber; j++) {
            executor.execute(() -> {
                // 开始时间
                long startTime = System.currentTimeMillis();
                // 进行消费计数
                int count = 0;
                while (semaphore.availablePermits() > 0) {
                    // 消费一个信号
                    try {
                        semaphore.acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        method.invoke(SpringUtils.getBean(ConsumerMethodsUtil.class), parameters);
                    } catch (IllegalAccessException | InvocationTargetException e2) {
                        e2.printStackTrace();
                    }
                    // 计数
                    count++;
                }
                System.out.println(String.format("%s共计消费%d条", Thread.currentThread().getName(), count));

                // 结束时间
                long endTime = System.currentTimeMillis();

                System.out.println(String.format("%s消费总耗时:%d", Thread.currentThread().getName(), (endTime - startTime)));
            });
        }
    }
}

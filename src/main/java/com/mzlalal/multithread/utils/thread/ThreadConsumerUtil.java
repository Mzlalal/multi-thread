package com.mzlalal.multithread.utils.thread;

import com.mzlalal.multithread.factory.MzThreadFactory;
import com.mzlalal.multithread.utils.method.MethodUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.*;

/**
 * 测试
 */
@Component
public class ThreadConsumerUtil<T>{

    @Value("${consumer.thread.number}")
    private int threadNumber;

    /**
     * 通过 ConsumerMethodsUtil 类中的 methodName(parametersType) 方法进行消费
     * 通过传入方法名称methodName, 方法参数类型 parametersType 来获取消费方法
     * 多线程测试
     * @param size 数字大小
     * @param methodName 消费方法名称
     * @param parametersType 消费方法参数类型
     * @param parameters 消费参数
     */
    public void consumer(int size, String methodName, Class[] parametersType, Object[] parameters) {
        this.consumer(size, ConsumerMethodsUtil.class, methodName, parametersType, parameters);
    }

    /**
     * 通过ConsumerMethodsUtil类中的methodName方法进行消费
     * 消费方法为遍历 ConsumerMethodsUtil 中的类获取第一个方法名为methodName的方法
     * 多线程测试
     * @param size 数字大小
     * @param methodName 消费方法名称
     * @param parameters 消费参数
     */
    public void consumer(int size, String methodName, Object[] parameters) {
        this.consumer(size, ConsumerMethodsUtil.class, methodName, null, parameters);
    }

    /**
     * 通过指定消费类 消费类名来进行多线程消费
     * 执行方法为遍历指定消费类 clazz 中的类获取第一个方法名为 methodName 的方法
     * 多线程测试
     * @param size 数组大小
     * @param clazz 消费类
     * @param methodName 消费方法
     * @param parameters 消费参数
     */
    public <T> void consumer(int size, Class<T> clazz, String methodName, Object[] parameters) {
        this.consumer(size, clazz, methodName, null, parameters);
    }

    /**
     * 通过指定消费类 消费类名来进行多线程消费
     * 通过 clazz 类中的 methodName(parametersType) 进行匹配消费方法执行
     * 多线程测试
     * @param size 数组大小
     * @param clazz 消费类
     * @param methodName 消费方法
     * @param parametersType 消费方法参数类型
     * @param parameters 消费参数
     */
    public <T> void consumer(int size, Class<T> clazz, String methodName,
                             Class[] parametersType, Object[] parameters) {
        // 获取调用方法
        // 如果传入的参入类型为空 则获取
        Method invokeMethod = null;
        if (parametersType == null || parametersType.length == 0) {
            invokeMethod = MethodUtil.getMethodByDeclared(clazz, methodName);
        } else {
            invokeMethod = MethodUtil.getAccessibleMethod(clazz, methodName, parametersType);
        }

        // 检测不能为空
        Assert.notNull(invokeMethod, "多线程调用方法能不为空!");

        this.consumerFinal(size, clazz, invokeMethod, parameters);
    }

    /**
     * 通过 执行消费类clazz 产生实例 根据方法参数parameters 调用 消费方法invokeMethod
     * 多线程测试
     * @param size 数组大小
     * @param clazz 消费类
     * @param invokeMethod 消费方法
     * @param parameters 消费参数
     */
    public <T> void consumerFinal(int size, Class<T> clazz, Method invokeMethod, Object[] parameters) {
        // 检测不能为空
        Assert.notNull(clazz, "多线程调用类不能为空!");

        // 设置与数组相同的信号量
        Semaphore semaphore = new Semaphore(size);

        // 初始化线程池 初始化 0 最大4 存活时间 60s
        ExecutorService executor = new ThreadPoolExecutor(0, threadNumber,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(),
                new MzThreadFactory());

        // 开启线程数
        for (int j = 0; j < threadNumber; j++) {
            executor.execute(() -> {
                // 开始时间
                long startTime = System.currentTimeMillis();
                // 进行消费计数
                int count = 0;

                // 实例化需要消费方法的类
                T temp = null;
                try {
                    temp = clazz.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                while (semaphore.availablePermits() > 0) {
                    try {
                        // 消费一个信号
                        semaphore.acquire();
                        // 调用消费方法
                        invokeMethod.invoke(temp, parameters);
                    } catch (InterruptedException | IllegalAccessException | InvocationTargetException e2) {
                        e2.printStackTrace();
                    }
                    // 计数
                    count++;
                }
                // 置空
                temp = null;

                System.out.println(String.format("%s共计消费%d条", Thread.currentThread().getName(), count));

                // 结束时间
                long endTime = System.currentTimeMillis();

                System.out.println(String.format("%s消费总耗时:%d", Thread.currentThread().getName(), (endTime - startTime)));
            });
        }
    }
}

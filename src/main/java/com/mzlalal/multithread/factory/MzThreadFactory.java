package com.mzlalal.multithread.factory;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 默认构建工厂工具
 */
public class MzThreadFactory implements ThreadFactory {
    /**
     * 工厂创建的连接池数目
     * 例如使用本类创建第二个factory时 poolNumber 变成2 表示是另外一个线程池
     */
    private static final AtomicInteger poolNumber = new AtomicInteger(1);
    /**
     * 线程组
     * 线程组表示一个线程的集合
     * ThreadGroup可以随时的获取在他里面的线程的运行状态，信息，或者一条命令关闭掉这个group里面的所有线程
     */
    private final ThreadGroup group;
    /**
     * 连接池当前数目
     */
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    /**
     * 线程名称前缀
     */
    private final String namePrefix;

    /**
     * 构建参数
     */
    public MzThreadFactory() {
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() :
                Thread.currentThread().getThreadGroup();
        namePrefix = "Mz线程工厂-第" +
                poolNumber.getAndIncrement() +
                "个线程池-线程编号:";
    }

    /**
     * 新建一个线程
     *
     * @param r Runnable
     * @return Thread
     */
    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r,
                namePrefix + threadNumber.getAndIncrement(),
                0);
        if (t.isDaemon()) {
            t.setDaemon(false);
        }
        if (t.getPriority() != Thread.NORM_PRIORITY) {
            t.setPriority(Thread.NORM_PRIORITY);
        }
        return t;
    }
}

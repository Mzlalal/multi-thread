package com.mzlalal.multithread.utils.thread;

import com.mzlalal.multithread.dao.PersonDAO;
import com.mzlalal.multithread.entity.Person;
import com.mzlalal.multithread.utils.common.SpringUtils;
import org.apache.commons.lang3.ClassUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @description: 线程消费方法工具类
 * 最好不要命名重复或者重载
 * 将需要定制化的方法卸载这里 通过反射在多线程中调用定制化方法
 * @author: Mzlalal
 * @date: 2019/11/12 17:15
 * @version: 1.0
 */
@Component
public class ConsumerMethodsUtil {

    /**
     * 公共多线程调用方法
     *
     * @param size 数组大小
     * @param methodName 方法名称
     * @param parameters 参数
     */
    public void invokeConsumerMethod(int size, String methodName, Object[] parameters) {
        // 获取消费者
        ThreadConsumerUtil threadConsumerUtil = SpringUtils.getBean(ThreadConsumerUtil.class);

        // 获取所有方法
        Method[] methods = ConsumerMethodsUtil.class.getDeclaredMethods();

        // 获取调用方法
        Method invokeMethod = null;
        for (Method method : methods) {
            // 判断方法名是否相等
            if (method.getName().equals(methodName)) {
                invokeMethod = method;
                // 跳出当前循环
                break;
            }
        }
        // 检测不能为空
        Assert.notNull(invokeMethod, "多线程调用方法不能不为空!");
        // 开始测试
        threadConsumerUtil.consumer(size, invokeMethod, parameters);
    }

    /**
     * 公共多线程调用方法
     * 通过指定方法名,参数获取方法
     *
     * @param size 数组大小
     * @param methodName 方法名称
     * @param clszz 方法参数类型
     * @param parameters 参数
     */
    public void invokeConsumerMethodByParameter(int size, String methodName, Class[] clszz, Object[] parameters) {
        // 获取消费者
        ThreadConsumerUtil threadConsumerUtil = SpringUtils.getBean(ThreadConsumerUtil.class);

        // 获取调用方法
        Method invokeMethod = null;
        try {
            invokeMethod = ClassUtils.getPublicMethod(ConsumerMethodsUtil.class, methodName, clszz);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        // 检测不能为空
        Assert.notNull(invokeMethod, "多线程调用方法不能不为空!");
        // 开始测试
        threadConsumerUtil.consumer(size, invokeMethod, parameters);
    }

    /**
     * 调用UUID list 方法
     *
     * @param uuidList
     * @param atomicInt
     */
    public void consumerUUIDList(List<String> uuidList, AtomicInteger atomicInt) {
        // 获取服务
        PersonDAO dao = SpringUtils.getBean(PersonDAO.class);

        // 新增一个person对象 从uuidList中获取数据, atomic获取下标后立即执行
        // atomicint的get方法不是线程安全
        Person person = new Person();
        person.setName(uuidList.get(atomicInt.getAndIncrement()));
        person.setC_name(Thread.currentThread().getName());

        // 保存到数据库
        dao.save(person);
    }


    /**
     * 单线程测试
     */
    public void singleUUIDList(List<String> uuidList) {
        // 开始时间
        long startTime = System.currentTimeMillis();
        // 获取服务
        PersonDAO dao = SpringUtils.getBean(PersonDAO.class);
        // 遍历添加
        for (String temp : uuidList) {
            // 新增一个person对象 从uuidList中获取数据
            Person person = new Person();
            person.setName(temp);
            person.setC_name(Thread.currentThread().getName());
            // 保存到数据库
            dao.save(person);
        }
        // 结束时间
        long endTime = System.currentTimeMillis();

        System.out.println(String.format("test2总耗时:%d", (endTime - startTime)));
    }
}

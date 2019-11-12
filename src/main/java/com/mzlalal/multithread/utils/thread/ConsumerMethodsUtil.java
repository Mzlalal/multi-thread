package com.mzlalal.multithread.utils.thread;

import com.mzlalal.multithread.dao.PersonDAO;
import com.mzlalal.multithread.entity.Person;
import com.mzlalal.multithread.utils.common.SpringUtils;
import org.apache.commons.lang3.ClassUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @description: 线程消费方法工具类
 * @author: Mzlalal
 * @date: 2019/11/12 17:15
 * @version: 1.0
 */
@Component
public class ConsumerMethodsUtil {

    /**
     * 公共多线程调用方法
     *
     * @param list
     * @param methodName
     */
    public void invokeConsumerMethod(List list, String methodName, Class[] clszz, Object[] parameters) {
        // 获取消费者
        ThreadConsumerUtil threadConsumerUtil = SpringUtils.getBean(ThreadConsumerUtil.class);

        // 消费方法
        Method method = null;
        try {
            method = ClassUtils.getPublicMethod(ConsumerMethodsUtil.class, methodName, clszz);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        // 开始测试
        threadConsumerUtil.consumer(list.size(), method, parameters);
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
}

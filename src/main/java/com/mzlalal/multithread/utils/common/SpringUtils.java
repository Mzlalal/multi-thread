package com.mzlalal.multithread.utils.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 手动获取bean工具类 实现ApplicationContextAware
 * 在注解扫描为开始时,需要手动获取bean
 * 在一些未知的情况 可能自动注入失败 手动获取bean
 * 使用component注入到容器里
 * @author Mzlalal
 * 2019年11月12日 17:19:25
 */
@Component
public class SpringUtils implements ApplicationContextAware {

	/**
	 *  声明全局上下文
	 */
    private static ApplicationContext applicationContext;

    /**
     *  ApplicationContext配置成功,在普通类可以通过调用SpringUtils.getAppContext()获取applicationContext对象,applicationContext=
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        if (SpringUtils.applicationContext == null)
        {
            SpringUtils.applicationContext = applicationContext;
        }

       
    }

    /**
     * 获取applicationContext
     * @return
     */
    public static ApplicationContext getApplicationContext()
    {
        return applicationContext;
    }

    /**
     * 通过name获取 Bean.
     * @param name 资源名
     * @return
     */
    public static Object getBean(String name)
    {
        return getApplicationContext().getBean(name);
    }

    /**
     * 通过class获取Bean.
     * @param clazz 类名
     * @return
     */
    public static <T> T getBean(Class<T> clazz)
    {
        return getApplicationContext().getBean(clazz);
    }

    /**
     * 通过name,以及Clazz返回指定的Bean
     * @param name 资源名
     * @param clazz 类名
     * @return
     */
    public static <T> T getBean(String name, Class<T> clazz)
    {
        return getApplicationContext().getBean(name, clazz);
    }

}
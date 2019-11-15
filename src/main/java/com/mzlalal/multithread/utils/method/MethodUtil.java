package com.mzlalal.multithread.utils.method;

import org.apache.commons.lang3.reflect.MethodUtils;

import java.lang.reflect.Method;

/**
 * 获取方法工具类
 */
public class MethodUtil extends MethodUtils {
    /**
     *
     * @param clazz 目标类
     * @param methodName 目标方法
     * @return
     */
    public static Method getMethodByDeclared(Class clazz, String methodName) {
        // 获取所有方法
        Method[] methods = clazz.getDeclaredMethods();

        // 遍历获取调用方法
        for (Method method : methods) {
            // 判断方法名是否相等
            if (method.getName().equals(methodName)) {
                // 返回方法名相同的第一个方法
                return method;
            }
        }
        return null;
    }
}

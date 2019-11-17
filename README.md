导入项目为maven
等待解析依赖
<br/>创建mysql数据库multi-thread-demo
<br/>修改application.prop对应的用户名密码


<br/>**如何使用测试：**<br/>
使用ThreadConsumerUtil.cosumer方法
 * 通过指定消费类 消费类名来进行多线程消费
 * 执行方法为遍历指定消费类 clazz 中的类获取第一个方法名为 methodName 的方法
 * 多线程测试
 * @param size 数组大小 数组大小主要为设置消费信号量 Semaphore
 * @param clazz 消费类 所需要调用的类
 * @param methodName 消费方法 所需要调用的方法
 * @param parameters 消费参数 调用消费方法的参数

<br/>例如:threadConsumerUtil.consumer(uuidList.size(), ConsumerMethodsUtil.class,"consumerUuidList", parameters);
<br/>如何上述调用方法解释为 需要创建 uuidList.size() 个信号量给多线程消费 多线程中执行的方法为ConsumerMethodsUtil中的consumerUuidList方法，传递给该方法的参数为parameters


SQL查询重复数据可能会使用到的语句：
select * from person order by name;

查询总数目语句
select count(*) from person;

重复数据查询语句<br/>
SELECT
	count(name) AS '重复次数',
	name
FROM
	person
GROUP BY
	name
HAVING
	count(*) > 1
ORDER BY
	name DESC
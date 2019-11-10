导入项目为maven
等待解析依赖<br/>
创建mysql数据库multi-thread-demo<br/>
修改application.prop对应的用户名密码

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
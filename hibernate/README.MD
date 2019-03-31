一、修改/etc/mysql/my.conf
找到bind-address = 127.0.0.1这一行
改为bind-address = 0.0.0.0即可

二、为需要远程登录的用户赋予权限
1、新建用户远程连接mysql数据库
grant all on *.* to admin@'%' identified by '123456' with grant option; 
flush privileges;
允许任何ip地址(%表示允许任何ip地址)的电脑用admin帐户和密码(123456)来访问这个mysql server。
注意admin账户不一定要存在。

2、支持root用户允许远程连接mysql数据库
grant all privileges on *.* to 'root'@'%' identified by '123456' with grant option;
flush privileges;
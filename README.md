# WForum
## 关于WForum
WForum 为本人2018年毕业设计，参考golang123（ https://www.golang123.com ）设计。
后端主要选用SpringBoot框架，ORM框架选用Mybatis，Redis使用Spring-data-redis。前端选用vue.js，nuxt.js。
## 环境搭建
### 1.tomcat
后端选用外置tomcat运行，使用根路径。tomcat版本选用8.0，jdk使用1.8。
### 2.mysql
本系统使用版本为5.5，数据库名为wforum，账号wforum，密码123456。重新配置请修改application.yml文件。
### 3.redis
redis详细设置请参考网络文档，本项目redis配置文件在config下。（仅作参考使用）
### 4.node npm
运行前端使用，通过npm管理，详情请参照官方文档。
### 5.nginx
nginx详细设置请参考网络文档，本项目nginx配置文件在config下。（仅作参考使用）
### 6.文件系统
系统使用操作系统文件存储，请注意各操作系统之间的差异。
## 运行
请先启动mysql，redis；然后启动tomcat以及node服务器；最后启动nginx。（不清楚请网上查阅资料）  
关于redis启动，进入redis根目录，输入命令：./redis-server ./redis.conf  
关于node，请先执行npm install命令。开发环境使用npm run dev，生产环境请先构建项目。  
所有启动完毕后访问 http://127.0.0.1/ 到达主页。  
### 效果图
![主页](/homePage.png)

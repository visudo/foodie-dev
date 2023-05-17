# 服务发布

## 创建公共网络
* docker network create --driver bridge --subnet 191.138.2.0/24 --gateway 192.138.2.1 common-network
  eaf8b34ce2198d8c5a61c82c30d693fa023e7abaaca63ec36627fef1b1c1bcc4
  
* 查询网络 docker network ls
* 查询网络详情 docker network inspect eaf8b34ce219

## 容器加入网络
* tomcat_static加入网络 docker network connect common-network --ip 192.138.2.2 tomcat_static
* mysql_standalone加入网络 docker network connect common-network --ip 192.168.0.3 mysql_standalone
* httpd加入网络 docker network connect common-network --ip 192.168.0.4 httpd

## 容器创建镜像
* docker commit --author "单机_tomcat" tomcat_static tomcat_static:v1
* docker commit --author "单机_mysql" mysql_standalone mysqll:v1
* docker commit --author "单机_httpd" httpd myhttpd:v1

## mysql无法commit出镜像
* 参考https://blog.csdn.net/CNBDZGL/article/details/125312515

## 部署方案
* tomcat_backend为后端项目
  docker run -d -p 8001:8080 --ip 10.88.0.2 --add-host='shop.z.demo.com:10.88.0.3' --add-host='center.z.demo.com:10.88.0.3' --add-host='api.z.demo.com:10.88.0.2' --name tomcat_backend docker.io/library/tomcat:latest
* tomcat_static为 前端项目
  docker run -d -p 8000:8080 --ip 10.88.0.3 --add-host='shop.z.demo.com:10.88.0.3' --add-host='center.z.demo.com:10.88.0.3' --add-host='api.z.demo.com:10.88.0.2' --name tomcat_frontend docker.io/library/tomcat:latest
* db部署
  docker run -d -p 3307:3306 --ip 10.88.0.4 --add-host='shop.z.demo.com:10.88.0.3' --add-host='center.z.demo.com:10.88.0.3' --add-host='api.z.demo.com:10.88.0.2' --env MYSQL_ROOT_PASSWORD=root --privileged=true --name db mysql:latest
* 静态文件部署
  docker run -d -p 9999:80 --ip 10.88.0.5 --add-host='shop.z.demo.com:10.88.0.3' --add-host='center.z.demo.com:10.88.0.3' --add-host='api.z.demo.com:10.88.0.2' --name httpd_static httpd:latest
* 测试互通
  curl http://shop.z.demo.com:8000/foodie-shop/index.html
## 域名方案
* 10.88.0.2:8080 api.z.demo.com 后端服务,外部 10.211.55.5:8001
* 10.88.0.3:8080 shop.z.demo.com 门户,外部 10.211.55.5:8000
* 10.88.0.3:8080 center.z.demo.com 用户中心,外部 10.211.55.5:8000
* 10.88.0.4:3306 db,外部 10.211.55.5:3307
* 10.88.0.5:80 静态文件,外部 10.211.55.5:9999 

## profile 多环境配置
1. 数据源
2. mybatis日志打印
3. 用户图像保存位置 file-upload-prod.properties
4. 支付回调地址修改 BaseController.java

## springboot 打war包
* 添加 <packaging>war</packaging>
* 移除内置tomcat
```xml
            <exclusions>
                <exclusion>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-tomcat</artifactId>
                </exclusion>
            </exclusions>
```
* 添加servlet依赖到api工程
```xml
        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>javax.servlet-api</artifactId>
            <scope>provided</scope>
        </dependency>
```
* 添加war包启动类 com.demo.WarApplication



curl http://10.211.55.5:8001/v1/hello


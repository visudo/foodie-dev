# 服务发布

## 创建公共网络
* docker network create --driver bridge --subnet 192.168.0.0/16 --gateway 192.168.0.1 common-network
  eaf8b34ce2198d8c5a61c82c30d693fa023e7abaaca63ec36627fef1b1c1bcc4
  
* 查询网络 docker network ls
* 查询网络详情 docker network inspect eaf8b34ce219

## 容器加入网络
* tomcat_static加入网络 docker network connect common-network --ip 192.168.0.2 tomcat_static
* mysql_standalone加入网络 docker network connect common-network --ip 192.168.0.3 mysql_standalone
* httpd加入网络 docker network connect common-network --ip 192.168.0.4 httpd

## 容器创建镜像
* docker commit --author "单机_tomcat" tomcat_static tomcat_static:v1
* docker commit --author "单机_mysql" mysql_standalone mysql:v1
* docker commit --author "单机_httpd" httpd myhttpd:v1

## 容器配置源
* 配置镜像源 /etc/apt/sources.list
* 配置容器与外网互通 
* 更新源 apt update -y










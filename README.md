# docker

## 安装brew(软件管理)
* /bin/zsh -c "$(curl -fsSL https://gitee.com/cunkai/HomebrewCN/raw/master/Homebrew.sh)"
* 导入环境变量 source /Users/xxxxxx/.zprofile
* 查看版本 brew --version

## 安装docker
* brew install docker

## 配置docker镜像
```shell
  "registry-mirrors": [
    "https://registry.docker-cn.com",
    "https://docker.mirrors.ustc.edu.cn",
    "http://hub-mirror.c.163.com",
    "https://cr.console.aliyun.com/"
  ]
```

## docker常用命令
* 查看版本 docker --version
* 拉取镜像 docker pull mysql
* 查看当前容器 docker ps -a
* 删除容器 docker rm $id
* 查看容器异常日志 docker logs $id
* 批量删除容器 for i in `docker ps -a | awk '{print$1}'`;do docker rm $i;done
* 查看容器id  docker inspect -f '{{.ID}}' tomcat_static
* 拷贝文件到容器 docker cp -a local_file_path container_id:container_path

## docker 部署mysql
* 拉取镜像  
docker pull mysql
* 将容器中的日志、数据、配置文件关联映射到宿主机当中，容器删除或者挂掉也不怕数据丢失，主机中的数据还在  
docker run -p 3307:3306 --privileged=true --name mysql_standalone --env MYSQL_ROOT_PASSWORD=root -d mysql
* 解释  
docker run -p 3307:3306 -d mysql --privileged=true  
--env MYSQL_ROOT_PASSWORD=root  #root密码  
--name mysql_standalone   #设置名字  

## 项目

### 用户登录判断
* 用户是否存在

### 用户入库
* 新增用户入库

### swagger 3.0 api
* 导入引用
* application.yml配置文件中增加映射路径

### 部署静态资源
* docker pull tomcat:9.0
* 启动 tomcat docker run -d -p 8000:8080 --name tomcat_static tomcat:9.0
* 拷贝文件到容器 docker cp -a /Users/xxxxx/Downloads/foodie-shop 6cd151ab82d6394df586d0369ddac11a49ab513ae70fd27fa844c45dd5a9bfe4:/usr/local/tomcat/webapps/ 
* 运行前台 http://127.0.0.1:8000/foodie-shop/

### 配置前后台跨域问题
* 注册CorsConfig bean到容器

### 用户登录
* 后台设置cookie
* 前台从cookie中读取用户信息
* 配置自己的日志打印
  log4j
* 通过日志监控函数执行时间
  通过切面实现
* 用户退出
* mybatis日志打印

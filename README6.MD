# 集群

## 单体面临的问题
* 单机点宕机导致服务不可用--》集群实现高可用
* 耦合度太高--》业务拆分
* 单节点并发能力有限--》负载均衡降低压力

## 使用集群注意点
* 用户会话共享
* 定时任务
* 内网互通

## 配置nginx
* tcp_nopush
* keepalive_timeout
* gzip 
* 部署nginx 
  docker run -d  --ip 10.88.0.6 --add-host='shop.z.demo.com:10.88.0.3' --add-host='center.z.demo.com:10.88.0.3' --add-host='api.z.demo.com:10.88.0.2' --name nginx nginx:latest
* 常用命令
  nginx -s quit 优雅关闭，针对http请求
  nginx -t 测试配置
* 静态资源配置方式
  1. 使用root
  ```shell
  location / {
        root   /usr/share/nginx/html;
        index  index.html index.htm;
  }
  ```
  2. 使用alias
  ```shell
  location /xxx {
        alias  /usr/share/nginx;
        index  index.html index.htm;
  }
  ```

* 匹配规则

     空格，默认匹配

  = 精确匹配

  ～正则匹配（～*不区分大小）

  ^~以某个字符路径开头


## 跨域
  通过配置header解决

- Access-Control-Allow-Origin 允许跨域请求的域
- Access-Control-Allow-Credentials 允许带上cookie请求
- Access-Control-Allow-Headers 允许请求头
- Access-Control-Allow-Methods 允许请求方法

## 防盗链

- 对源站点进行验证，防止非法站点访问 valid_referers

## 负载均衡

- 四层负载均衡

  f5 

  lvs

  haproxy

  nginx

- 七层负载均衡

  nginx

  haproxy

  apache

- dns地域负载均衡

  就近访问原则

- jmeter压测

- nginx负载均衡策略

  轮循

  权重 weight

  ip hash

  一致性hash    

  url hash

  least_conn 最少连接数

  

## 搭建tomcat集群

- nginx一台

  docker run -d  --ip 10.88.0.7  --name nginx2 nginx

  配置文件如下

  ```nginx
  upstream tomcats {
  server 10.88.0.8:8080;
  server 10.88.0.9:8080;
  server 10.88.1.0:8080;
  }
  server {
      listen       80;
      server_name  www.tomcats.com;
      location / {
        proxy_pass http://tomcats;
      }
  }
  ```

  

- tomcat三台

  docker run -d  --ip 10.88.0.8  --name tomcat1 tomcat

  docker run -d  --ip 10.88.0.9  --name tomcat2 tomcat

  docker run -d  --ip 10.88.1.0  --name tomcat3 tomcat

## nginx缓存控制

- 浏览器缓存控制 expires
- 反向代理缓存控制proxy_cache

## 动静分离方式

- cdn 加速静态资源访问
- 使用nginx

## 使用nginx部署站点

- nginx静态配置

  ```nginx
  server {
      listen       80;
      server_name  shop.z.demo.com;
      location / {
        root /home/website/foodie-shop;
        index index.html;
       }
  }
  
  server {
      listen       80;
      server_name  center.z.demo.com;
      location / {
        root /home/website/foodie-center;
        index index.html; 
       }
  }
  ```

- nginx api配置

  ```nginx
  upstream api.z.demo.com {
  server 10.88.0.2:8080;
  }
  server {
      listen       80;
      server_name  api.z.demo.com;
      location / {
        proxy_pass http://api.z.demo.com;
      }
  }
  ```

   

- 域名配置

  ```shell
  10.88.0.6  api.z.demo.com
  10.88.0.6  shop.z.demo.com
  10.88.0.6  center.z.demo.com
  ```

## nginx高可用

- keepalive 双机主备

  会带来额外开销，被节点始终不处理请求

  ![image-20230525225109403](/Users/tangyanghai/javaproject/foodie-dev/image/image-20230525225109403.png)

- keepalive 双主热备

  nginx节点互为主备

  ![image-20230525224654358](/Users/tangyanghai/javaproject/foodie-dev/image/demo.png)

## 双机主备部署

- nginx节点部署

  docker run -d  --ip 10.88.1.1 --net=podman --cap-add NET_ADMIN --cap-add=NET_BROADCAST --cap-add=NET_RAW --add-host='shop.z.demo.com:10.88.0.6' --add-host='center.z.demo.com:10.88.0.6' --add-host='api.z.demo.com:10.88.0.6' --name nginx_1 nginx:latest

  1. 修改nginx节点镜像源，安装keepalived

  ```shell
  sed -i "s@http://deb.debian.org@https://mirrors.ustc.edu.cn@g" /etc/apt/sources.list
  apt-get update
  apt-get install keepalived
  ```

  2. 配置环境

  ```shell
  docker cp foodie.conf 958c073439d7:/etc/nginx/conf.d/foodie.conf
  docker cp keepalived.conf 958c073439d7:/etc/keepalived/keepalived.conf
  ```

  3. keepalive配置

  ```shell
  global_defs {
    router_id keep_xxx13
  }
  vrrp_instance VI_1 {
    state MASTER
    interface eth0
    virtual_router_id 51
    priority 100
    advert_int 2
    authentication {
      auth_type PASS
      auth_pass 1111
   }
    virtual_ipaddress {
      10.88.1.10
   }
  }
  ```

  docker run -d  --ip 10.88.1.2 --net=podman --cap-add NET_ADMIN --cap-add=NET_BROADCAST --cap-add=NET_RAW --add-host='shop.z.demo.com:10.88.0.6' --add-host='center.z.demo.com:10.88.0.6' --add-host='api.z.demo.com:10.88.0.6' --name nginx_2 nginx:latest

   1. 同上修改
   1. keepalived配置

  ```shell
  global_defs {
    router_id keep_xxx12
  }
  vrrp_instance VI_1 {
    state BACKUP
    interface eth0
    virtual_router_id 51
    priority 80
    advert_int 2
    authentication {
      auth_type PASS
      auth_pass 1111
   }
    virtual_ipaddress {
      10.88.1.10
   }
  }
  ```

  3. 备机未绑定虚拟ip

- keepalive部署

  1. 启动：keepalived -l -f /etc/keepalived/keepalived.conf

  2. 配置说明

  ```shell
  global_defs {
    #路由id：当前安装keepalived的节点主机标识符，保证全局唯一
    router_id keep_xxx13
  }
  vrrp_instance VI_1 {
    #表示状态是MASTER主机还是备用机BACKUP
    state MASTER
    #该实例绑定的网卡，需要登入容器查看
    interface eth0
    #保证主备节点一致即可
    virtual_router_id 51
    #权重，master权重	一般高于backup，如果有多个，那就是选举，谁的权重高，谁就当选
    priority 100
    #主备之间同步检查时间间隔，单位秒
    advert_int 2
    #认证权限密码，防止非法节点进入
    authentication {
      auth_type PASS
      auth_pass 1111
    }
    #虚拟出来的ip，可以有多个（vip）
    virtual_ipaddress {
      10.88.1.3
    }
  }
  ```

  ```shell
  bash-4.4# ip addr
  2: eth0@if18: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc noqueue state UP group default qlen 1000
      link/ether 16:d8:19:20:ca:8e brd ff:ff:ff:ff:ff:ff link-netnsid 0
      inet 10.88.0.15/16 brd 10.88.255.255 scope global eth0
         valid_lft forever preferred_lft forever
      inet 10.88.1.3/32 scope global eth0  # 绑定了多个网卡
         valid_lft forever preferred_lft forever
      inet6 fe80::14d8:19ff:fe20:ca8e/64 scope link 
         valid_lft forever preferred_lft forever
  ```

  3. 测试配置：/usr/sbin/keepalived -n -l -f 配置文件测试是否正常

- 监控脚本

  1. nginx故障后，keepalived需要定时监测nginx，如果拉起失败，需要停止keepalived服务切换虚拟ip

  ```shell
  vrrp_script check_nginx_alive {
    script "/etc/keepalived/check_nginx_alive_or_not.sh"
    interval 2
    weight 10
  }
  在vrrp_instance实例中添加监控内容
  track_script {
    check_nginx_alive
  }
  ```

  脚本内容如下

  ```shell
  #!/bin/bash
  A=`ps -C nginx --no-header|wc -l`
  if [ $A -eq 0 ];then
    /usr/sbin/nginx
    sleep 3
    if [ `ps -C nginx --no-header | wc -l` -eq 0 ];then
      killall keepalived
    fi
  fi
  ```

## 双主热备

  - 需要增加虚拟ip
  - 主节点keepalived实例内容

  ```shell
  # 修改名称
  vrrp_instance VI_2 { 
    # 备机
    state BACKUP 
    interface eth0
    # 实例唯一
    virtual_router_id 52
    # 备机权重
    priority 80
    advert_int 2
    authentication {
      auth_type PASS
      auth_pass 1111
   }
    virtual_ipaddress {
      # 虚拟ip 2
      10.88.1.11
   }
  }
  ```

  - 主节点网卡信息

  ```shell
  2: eth0@if37: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc noqueue state UP group default qlen 1000
      link/ether 3a:69:82:64:a6:21 brd ff:ff:ff:ff:ff:ff link-netnsid 0
      inet 10.88.1.1/16 brd 10.88.255.255 scope global eth0
         valid_lft forever preferred_lft forever
      inet 10.88.1.10/32 scope global eth0
         valid_lft forever preferred_lft forever
      inet6 fe80::3869:82ff:fe64:a621/64 scope link 
         valid_lft forever preferred_lft forever
  ```

  

  - 备点keepalived实例内容

  ```shell
  vrrp_instance VI_2 { 
    # 主机
    state MASTER 
    interface eth0
    # 实例唯一
    virtual_router_id 52
    # 主机权重
    priority 100
    advert_int 2
    authentication {
      auth_type PASS
      auth_pass 1111
   }
    virtual_ipaddress {
      # 虚拟ip 2
      10.88.1.11
   }
  }
  ```

  - 备节点网卡信息

  ```shell
  2: eth0@if36: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc noqueue state UP group default qlen 1000
      link/ether 4a:0f:45:45:f1:c6 brd ff:ff:ff:ff:ff:ff link-netnsid 0
      inet 10.88.1.2/16 brd 10.88.255.255 scope global eth0
         valid_lft forever preferred_lft forever
      inet 10.88.1.11/32 scope global eth0
         valid_lft forever preferred_lft forever
      inet6 fe80::480f:45ff:fe45:f1c6/64 scope link 
         valid_lft forever preferred_lft forever
  ```

  - 主节点宕机被节点网卡信息

  ```shell
  2: eth0@if36: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc noqueue state UP group default qlen 1000
      link/ether 4a:0f:45:45:f1:c6 brd ff:ff:ff:ff:ff:ff link-netnsid 0
      inet 10.88.1.2/16 brd 10.88.255.255 scope global eth0
         valid_lft forever preferred_lft forever
      inet 10.88.1.11/32 scope global eth0
         valid_lft forever preferred_lft forever
      inet 10.88.1.10/32 scope global eth0
         valid_lft forever preferred_lft forever
      inet6 fe80::480f:45ff:fe45:f1c6/64 scope link 
         valid_lft forever preferred_lft forever
  ```

 ## LVS 负载均衡

- 优点

  工作在四层，对于请求直接转发，工作效率高

- lvs nat模式

- lvs tun模式

- lvs dr模式

  docker run -itd  --ip 10.88.1.4  -p 80:80 --name centos_1 centos:latest

  安装ps apt-get install procps

  安装网卡配置 apt install ifupdown

  

  

##### 1. 基于Linux环境安装Redis

###### 1.1 基于Cent OS7安装Redis

- 下载安装包
    wget http://download.redis.io/release/redis-?.?.?.tar.gz
- 解压
    tar -xvf 文件名.tar.gz
- 编译
    make
- 安装
    make install


###### 1.2 Redis服务启动

- 默认配置启动

    redis-server
    redis-server --port 6379
    redis-server --port 6380

- 指定配置文件启动

    redis-server redis.conf
    redis-server conf/redis-6379.conf
    
###### 1.3 Redis服务端配置

- 基本配置
    daemonize yes
    以守护进程方式启动，使用本启动方式，redis将以服务的形式存在，日志不再打印到命令窗口中
    
    port 6***
    设定当前服务启动端口号
    
    dir '/自定义目录/redis/data'
    设定当前服务文件保存文职，包含日志文件、持久化文件等
    
    logfile '6***.log'
    设定日志文件名，便于查阅
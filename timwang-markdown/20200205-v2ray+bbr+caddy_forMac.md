## 1. 申请域名

网站参考：[域名注册_虚拟主机_云服务器_企业邮箱-万网-阿里云旗下品牌](https://wanwang.aliyun.com/)

1. 先实名认证
2. 申请域名，我申请了.top的域名，67块钱3年，还是可以的
3. 添加解析设置（后面用到caddy的Web 服务端配置用）

<img src="http://ww1.sinaimg.cn/large/007OGB2sly1gblbuvhgysj31w60mkdl1.jpg" alt="image-20200205095809050" style="zoom:50%;" />

## 2. 申请境外VPS服务器

我用的vps服务商地址：[Vultr-VPS服务商](https://www.vultr.com/)

1. 注册账号
2. 申请服务器，具体可参考网上文章>>> [Vultr 教程：Vultr VPS 的购买使用以及常见测试方法和问题](https://zhuanlan.zhihu.com/p/34111789)
3. ！！这里记得选择CentOS，并且Version要选择CentOS7，因为这里后面配置踩过坑....

<img src="http://ww1.sinaimg.cn/large/007OGB2sly1gblbuvh0bjj31xo0l4djb.jpg" alt="image-20200205100745187" style="zoom:50%;" />

## 3. 配置服务器bbr

全程无脑参考这个链接>>> [vultr vps 开启BBR加速 （CentOS 7）](https://www.cnblogs.com/dyhaohaoxuexi/p/11204690.html)

## 4. 配置V2ray

[V2ray Caddy配置](https://www.ailearn666.com/2019/07/25/linux/v2ray-caddy-pei-zhi/)

参考配置v2ray，主要是执行脚本，可以把端口号和id改成自己想要的

## 5. 配置Caddy

```bash
curl https://getcaddy.com | bash -s personal
```

```bash
#查看安装位置
which caddy
#out /usr/local/bin/caddy
```

```bash
#出于安全考虑，切勿以root身份运行Caddy二进制文件。 为了让Caddy能够以非root用户身份绑定到特权端口（例如80,443），您需要运行setcap命令，如下所示
sudo setcap 'cap_net_bind_service=+ep' /usr/local/bin/caddy
```

```bash
#为Caddy创建一个专用的系统用户：caddy和一组同名的用户：
sudo useradd -r -d /var/www -M -s /sbin/nologin caddy
#注意：此处创建的用户caddy只能用于管理Caddy服务，不能用于登录。
```

```bash
#为Caddy Web服务器创建主目录/ var / www，为您的站点创建主目录/var/www/example.com：
sudo mkdir -p /var/www/example.com
sudo chown -R caddy:caddy /var/www
```

```bash
#创建存储SSL证书的目录：
sudo mkdir /etc/ssl/caddy
sudo chown -R caddy:root /etc/ssl/caddy
sudo chmod 0770 /etc/ssl/caddy
```

```bash
#创建一个专用目录来存储Caddy配置文件Caddyfile：
sudo mkdir /etc/caddy
sudo chown -R root:caddy /etc/caddy
```

```bash
#创建名为Caddyfile的Caddy配置文件：
sudo touch /etc/caddy/Caddyfile
sudo chown caddy:caddy /etc/caddy/Caddyfile
sudo chmod 444 /etc/caddy/Caddyfile
# tee命令用于将数据重定向到文件，另一方面还可以提供一份重定向数据的副本作为后续命令的stdin。简单的说就是把数据重定向到给定文件和屏幕上。
cat <<EOF | sudo tee -a /etc/caddy/Caddyfile
mydomain.me # 域名
{
  log ./caddy.log
  proxy /ray localhost:36722 { # 36722是V2ray配置的端口
    websocket
    header_upstream -Origin
  }
}
EOF
```

注意：上面创建的Caddyfile文件只是运行静态网站的基本配置。[您可以在此处了解有关如何编写Caddyfile的更多信息](https://caddyserver.com/tutorial/caddyfile)。

创建Caddy systemd单元文件：

```bash
curl -s https://raw.githubusercontent.com/mholt/caddy/master/dist/init/linux-systemd/caddy.service -o /etc/systemd/system/caddy.service	
```

改一下配置文件里面的用户信息

```bash
vi /etc/systemd/system/caddy.service
```

> ​	;User and group the process will run as.
>
> ​	User=caddy
>
> ​	Group=caddy
注意：上面创建的Caddyfile文件只是运行静态网站的基本配置。[您可以在此处了解有关如何编写Caddyfile的更多信息](https://caddyserver.com/tutorial/caddyfile)。



启动Caddy服务并使其在系统启动时自动启动：

```bash
sudo systemctl daemon-reload
sudo systemctl start caddy.service
sudo systemctl enable caddy.service
```

```bash
## 查看启动状态
sudo systemctl status caddy.service
## 如果启动不起来说明邮箱没有配置，配置一下邮箱
nohup /usr/local/bin/caddy -log stdout -log-timestamps=false -agree=true -conf=/etc/caddy/Caddyfile -root=/var/tmp 2>&1 &
```

## 6. 修改防火墙规则

为了允许访问者访问您的Caddy站点，您需要打开端口80和443：

```bash
sudo firewall-cmd --permanent --zone=public --add-service=http 
sudo firewall-cmd --permanent --zone=public --add-service=https
sudo firewall-cmd --reload
```

为您的网站创建测试页
使用以下命令在Caddy站点主目录中创建名为index.html的文件：

```bash
echo '<h1>Hello World!</h1>' | sudo tee /var/www/example.com/index.html
```

重新启动Caddy服务以加载新内容：

```bash
sudo systemctl restart caddy.service
```

最后，将您的Web浏览器指向[http://example.com或https](http://example.xn--comhttps-bt7p/)://example.com。 您应该看到消息Hello World！。

## 7. Mac端配置

[下载地址](https://github.com/Cenmrev/V2RayX/releases)

安装启动后会看到一个这样的图标
![image.png](https://segmentfault.com/img/remote/1460000018242769)
点击Configure进入配置

<img src="https://segmentfault.com/img/remote/1460000018242770" alt="image-20200205095809050" style="zoom:50%;float:left" />

<img src="https://segmentfault.com/img/remote/1460000018242771" alt="image-20200205095809050" style="zoom:50%;float:left" />

> 接下来点击transport settings进入配置
> websocket 
<img src="https://segmentfault.com/img/remote/1460000018242772" alt="image-20200205095809050" style="zoom:50%;float:left" />

> http/2配置
<img src="https://segmentfault.com/img/remote/1460000018242773" alt="image-20200205095809050" style="zoom:50%;float:left" />

> tls配置
<img src="https://segmentfault.com/img/remote/1460000018242774" alt="image-20200205095809050" style="zoom:50%;float:left" />

最后ok,在开启服务端V2ray和Caddy服务后看看是否能[Google](https://www.google.com/?gws_rd=ssl)了

## 链接参考

[CentOS7使用firewalld打开关闭防火墙与端口](https://www.cnblogs.com/Crazy-Liu/p/10837478.html)

[[V2ray+websocket+tls+caddy+serverSpeeder](https://segmentfault.com/a/1190000018242765)](https://segmentfault.com/a/1190000018242765)

[在centos里面安装配置caddy](https://blog.csdn.net/peihexian/article/details/88180678)

[V2Raｙ+WebSocket+TLS+Nginx 配置及使用](https://www.cnblogs.com/bndong/p/11763377.html)

[基于v2ray的websocket+tls+web实现安全网络代理](https://www.conum.cn/share/191.html)

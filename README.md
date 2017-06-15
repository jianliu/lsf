 分布式服务框架  : L Service Framework
===
当前很多互联网企业不再采用WebService这种基于HTTP方式的通信（没有用过WebService），而改为更底层高效的tcp层做点对点通信。lsf是一个实践，覆盖的面有限，旨在为搞清楚一些核心的东西而努力。

### 功能已实现的
* 使用javassist来做java代理，它提前生成可顺序执行的字节码，而非反射的方式，效率较高
* 使用zookeeper来做注册中心，consumer通过zookeeper来寻找server，使用开源的zkClient来调用zookeeper api
* 使用jaskson来做序列化工具
* 使用netty来实现传输层，利用其高可用的nio实现来保证点对点通信

### 未实现的
* 监控，性能、调用次数等未监控，原因是高性能的监控需要基于日志同步等高性能的方案
* ping-pong
* 没有web可视化工具


## 服务端启动方式
```java
server main entrance: com.liuj.lsf.demo.ServerMain
```
## 消费者端启动方式
```java
client main entrance: com.liuj.lsf.demo.ClientMain
```
## zookeeper配置
com.liuj.lsf.GlobalManager 配置zookeeper 的 `serverHost port`


### 遗留的问题
* zookeeper不太适合做服务发现，一旦节点太多，会使zookeeper连接数爆满，甚至挂掉
* jaskson做序列化的性能（包括序列化后的空间、序列化速度）低于类似messagepack这样的序列化工具，它其实更常用于web开发，网络传输中，应尽量使用序列化后体积小的工具
* 未集成spring 命名空间，采用原始main方法形式，不过集成spring命名空间可以参考另一个仓库 [lmq](https://github.com/jianliu/lmq)

# 微服务前言

![微服务不只是SpringCloud](./images/微服务不只是SpringCloud.png)

**微服务**只是**分布式架构**的一种！

分布式架构就是要把服务做拆分，而拆分的过程中会产生各种各样的问题需要解决，**Spring Cloud**只解决了服务拆分时的**服务治理**问题。



## 微服务技术栈

传统的**单体应用**，所有的业务功能全部写在一起，随着业务功能不断增加，代码耦合的越来越多，不利于将来的升级维护。

![单体应用模型](./images/单体应用模型.png)

微服务在做拆分的时候，会根据业务功能模块将一个单体项目拆分成许多个独立的项目，每个项目完成一部分业务功能，将来独立开发和部署。

我们把这些独立的项目称之为**服务**。一个大型的微服务项目往往会包含数百甚至上千个服务，最终形成一个**服务集群**。

![服务集群模型](./images/服务集群模型.png)

而一个业务往往需要由多个服务共同完成，例如一个请求来了，它可能先去调用服务A，随后服务A会调用服务B，服务B又会调用服务C。

当业务越来越多，越来越复杂的时候，这些服务之间的调用关系就会越来越复杂，而这些复杂的调用关系光靠人去记录和维护这是不可能的。

![服务间的复杂调用关系](./images/服务间的复杂调用关系.png)

这个时候就需要用到**注册中心**，它可以去记录每一个服务的IP、端口和功能这些信息。

当有一个服务需要调用另外的服务时，只需要到注册中心中去拉取对应的服务信息。

![注册中心模型](./images/注册中心模型.png)

另外，随着服务越来越多，每个服务都有自己的配置文件，如果我们需要更改配置，逐一更改太麻烦了，所以就需要用到**配置中心**。

它可以去统一管理整个服务集群的成千上百的配置，如果我们需要变更某些配置，只需要通过配置中心去通知相关的微服务实现配置的**热更新**。

![配置中心模型](./images/配置中心模型.png)

当我们的微服务运行起来以后，用户就可以来访问我们了，但是这个时候还需要一个**网关组件**。（就像小区保安，拦截不法分子、指明住户住所）

因为我们有这么多的微服务，用户怎么知道该访问哪一个呢？（**网关组件功能1**：需要把用户的请求路由到具体的服务，同时还可以做**负载均衡**）

而且也不是随便什么人都可以访问我们的微服务的。（**网关组件功能2**：需要对用户的身份做校验）

![网关组件模型](./images/网关组件模型.png)

用户发起访问请求后，服务接到请求就开始处理业务，该访问数据库的时候就会去访问**数据库集群**。

![数据库集群模型](./images/数据库集群模型.png)

但无论数据库集群有多庞大也不可能有用户多，所以数据库肯定无法抗住高的并发。因此就需要加入**分布式缓存**，它也是一个集群。

缓存会把数据库的数据放到内存中，从而提高查询效率。

通过分布式缓存就可以应对高并发了，用户请求会先查询缓存，缓存未命中了再去查询数据库，从而降低了数据库的查询压力。

![分布式缓存模型](./images/分布式缓存模型.png)

以后我们的业务中还会有一些复杂的搜索功能。简单查询可以做缓存，但是一些海量数据的复杂搜索、统计分析无法用缓存去实现，此时就需要用到**分布式搜索**。

数据库的主要职责就是做数据的 **写** 操作，以及一些**事务**类型的，对数据安全要求比较高的数据存储。

![分布式搜索模型](./images/分布式搜索模型.png)

最后，在微服务中还需要一种**异步通信**的**消息队列**组件。

因为对于分布式服务（或微服务）来说，它的业务往往会跨越多个服务，例如一个请求来了先调用了服务A，服务A再调用服务B，服务B再调用服务C……这就会导致整个业务的链路很长，从而**调用时长=每个服务的执行时长之和**，所以其性能是有一定的下降的。

而异步通信的意思就是，请求调用服务A之后，服务A不再直接调用服务B，而是通知服务B执行（即发送一条通知消息），随后服务A就直接结束了。这就使得业务链路缩短，响应时间也缩短，进而分布式服务（或微服务）的吞吐能力变强。

- 其中服务A发送的通知消息就存储在消息队列组件中，服务B只有在接收到该消息时才会去执行。（同理，服务B调用服务C也是通知）

所以异步通信能够大大提高我们服务的并发，在一些像“秒杀”这样的高并发场景下就可以去利用了。

![消息队列模型](./images/消息队列模型.png)

当然，如此庞大且复杂的一个服务，在运行的过程中如果出现什么问题是不太好排查的。所以我们需要引入两个新的组件，来解决运行过程中服务的异常定位。

1.**分布式日志服务**：负责统计整个集群中成千上百个服务的运行日志，统一去做存储、统计、分析，方便快速定位服务运行过程中出现的问题。

2.**系统监控+链路追踪**：实时监控整个集群中每个服务节点的运行状态、CPU的负载、内存的占用等情况，一旦出现任何问题，直接可以定位到具体的某一个方法（栈信息），从而帮助我们快速定位到异常所在。

![分布式日志&系统监控+链路追踪](./images/分布式日志&系统监控+链路追踪.png)

如此庞大且复杂的一个微服务集群，将来其中的服务数量可能上万，届时如何**部署**将成为一个问题。人工部署显然不现实，那么就需要考虑**自动化部署**。

1.**Jenkins**：可以帮助我们对这些微服务项目进行自动化编译

2.**Docker**：基于docker做一些打包，形成镜像

3.**kubernetes**：实现自动化部署

4.**RANCHER**：同上

以上这一套我们称之为**持续集成**

![持续集成技术](./images/持续集成技术.png)

### 分类总结

![技术栈分类](./images/技术栈分类.png)



由浅入深，可以将微服务技术栈做如下分层

![技术栈分层](./images/技术栈分层.png)



# 微服务架构

## 认识微服务

### 1.服务架构演变

==单体架构==：将业务的所有功能集中在一个项目中开发，打成一个包部署。

优点：架构简单；部署成本低。

![单体架构模型](./images/单体架构模型.png)

缺点：耦合度高

![单体应用工作流程](./images/单体应用工作流程.png)



==分布式架构==：根据业务功能对系统进行拆分，每个业务模块作为独立项目开发，称为一个服务。

优点：降低服务耦合度；有利于服务升级拓展

![分布式架构模型](./images/分布式架构模型.png)

（服务治理）分布式架构要考虑的问题：

- 服务拆分粒度如何？
- 服务集群地址如何维护？
- 服务之间如何实现远程调用？
- 服务健康状态如何感知？

为了解决上述问题，诞生了很多解决方案，其中应用最广泛且最火热的就是<span style="color:red;">**微服务**</span>方案：

![分布式架构解决方案——微服务](./images/分布式架构解决方案——微服务.png)

> 微服务是一种经过良好架构设计的**分布式**架构方案，微服务架构特征：
>
> - 单一职责：微服务拆分粒度更小，每一个服务都对应唯一的业务功能，做到单一职责，避免重复业务开发
> - 面向服务：微服务对外暴露业务接口
> - 自治：团队独立、技术独立、数据独立、部署独立
> - 隔离性：服务调用做好隔离、容错、降级，避免出现级联问题

![微服务架构特征](./images/微服务架构特征.png)

==总结：==

单体架构特点：

- 简单方便，高度耦合，扩展性差，适合小型项目
- 例如：学生管理系统

分布式架构特点：

- 松耦合，扩展性好，但架构复杂，难度大。适合大型互联网项目
- 例如：京东、淘宝

微服务：一种良好的分布式架构方案

- 优点：拆分粒度更小、服务更独立、耦合度更低
- 缺点：架构非常复杂，运维、监控、部署难度更高



### 2.SpringCloud

#### 微服务技术对比

微服务这种方案需要技术框架来落地，全球的互联网公司都在积极尝试自己的微服务落地技术。国内最知名的就是 <span style="color:red;">**SpringCloud**</span> 和 Alibaba的 <span style="color:red;">**Dubbo**</span> 。

|                |         Dubbo         |       SpringCloud        |    SpringCloudAlibaba    |
| :------------: | :-------------------: | :----------------------: | :----------------------: |
|    注册中心    |   zookeeper、Redis    |      Eureka、Cousul      |      Nacos、Eureka       |
|  服务远程调用  |       Dubbo协议       |    Feign（http协议）     |       Dubbo、Feign       |
|    配置中心    |          无           |   Spring Cloud Config    | SpringCloudConfig、Nacos |
|    服务网关    |          无           | SpringCloudGateway、Zuul | SpringCloudGateway、Zuul |
| 服务监控和保护 | dubbo-admin（功能弱） |         Hystrix          |         Sentinel         |

企业中可能使用到的技术组合如下：

![企业中使用的微服务技术组合](./images/企业中使用的微服务技术组合.png)

==**SpringCloudAlibaba** 本质上是按照 **SpringCloud** 标准实现的，可以认为它是 **SpringCloud** 中的一部分。==



#### SpringCloud

[SpringCloud](https://spring.io/projects/spring-cloud) 是目前国内使用最广泛的微服务框架。

**SpringCloud** 集成了各种微服务功能组件，并基于 **SpringBoot** 实现了这些组件的<span style="color:red;">自动装配</span>，从而提供了良好的开箱即用体验：

![微服务集成的各种功能组件](./images/微服务集成的各种功能组件.png)

**SpringCloud** 与 **SpringBoot** 的版本兼容关系如下：

![SpringCloud与SpringBoot的版本兼容关系](./images/SpringCloud与SpringBoot的版本兼容关系.png)



## 分布式服务架构案例

### 服务拆分

> ==服务拆分时需要注意的事项：==
>
> 1.不同服务，不要重复开发相同业务
>
> 2.各服务数据独立，不要访问其它服务的数据库
>
> 3.服务可以将自己的业务暴露为接口，供其它服务调用

拆分案例：cloud-demo

#### 1.建库建表 cloud_order

![cloud_order库](./images/cloud_order库.png)



```sql
/*
 Navicat Premium Data Transfer

 Source Server         : local-db
 Source Server Type    : MySQL
 Source Server Version : 80016
 Source Host           : localhost:3306
 Source Schema         : cloud_order

 Target Server Type    : MySQL
 Target Server Version : 80016
 File Encoding         : 65001

 Date: 23/11/2023 16:03:17
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_order
-- ----------------------------
DROP TABLE IF EXISTS `tb_order`;
CREATE TABLE `tb_order`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `price` decimal(10, 2) NOT NULL COMMENT '商品价格',
  `num` int(11) NULL DEFAULT 0 COMMENT '商品数量',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ordername`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_order
-- ----------------------------
INSERT INTO `tb_order` VALUES (1, 1, 'Apple 苹果 iPhone 12', 699900.00, 1);
INSERT INTO `tb_order` VALUES (2, 2, '雅迪 yadea 新国标电动车', 20990.00, 1);
INSERT INTO `tb_order` VALUES (3, 3, '骆驼（CAMEL） 休闲运动服', 43900.00, 1);
INSERT INTO `tb_order` VALUES (4, 4, '小米14 Pro 双模5G 骁龙8第三代', 35900.00, 1);
INSERT INTO `tb_order` VALUES (5, 5, 'OPPO Reno3 Pro 双模5G', 299900.00, 1);
INSERT INTO `tb_order` VALUES (6, 6, '美的（Midea）新能效空调', 544900.00, 1);
INSERT INTO `tb_order` VALUES (7, 2, '西昊/SIHOO 人体工学椅', 79900.00, 1);
INSERT INTO `tb_order` VALUES (8, 3, '梵班（FAMDBANN）', 31900.00, 1);

SET FOREIGN_KEY_CHECKS = 1;
```



#### 2.建库建表 cloud_user

![cloud_user库](./images/cloud_user库.png)



```sql
/*
 Navicat Premium Data Transfer

 Source Server         : local-db
 Source Server Type    : MySQL
 Source Server Version : 80016
 Source Host           : localhost:3306
 Source Schema         : cloud_user

 Target Server Type    : MySQL
 Target Server Version : 80016
 File Encoding         : 65001

 Date: 23/11/2023 16:03:29
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收件人',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '地址',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_user
-- ----------------------------
INSERT INTO `tb_user` VALUES (1, '柳岩', '湖南省衡阳市');
INSERT INTO `tb_user` VALUES (2, '文二狗', '陕西省西安市');
INSERT INTO `tb_user` VALUES (3, '雨化田', '湖北省十堰市');
INSERT INTO `tb_user` VALUES (4, '张必沉', '天津市');
INSERT INTO `tb_user` VALUES (5, '郑爽爽', '辽宁省沈阳市大东区');
INSERT INTO `tb_user` VALUES (6, '范冰冰', '山东省青岛市');

SET FOREIGN_KEY_CHECKS = 1;
```



#### 3.建项目

![cloud_demo项目结构](./images/cloud_demo项目结构.png)

- 创建父工程 cloud_demo，pom文件如下：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.djn</groupId>
    <artifactId>cloud-demo</artifactId>
    <packaging>pom</packaging>
    <version>1.0</version>
    <modules>
        <module>order-service</module>
        <module>user-service</module>
    </modules>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.6.4</version>
        <relativePath/>
    </parent>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <java.version>1.8</java.version>
        <spring-cloud.version>2021.0.3</spring-cloud.version>
        <mysql.version>8.0.33</mysql.version>
        <mybatis.version>2.2.2</mybatis.version>
    </properties>

    <dependencyManagement>
        <dependencies>
            <!--SpringCloud-->
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
            <!--数据库驱动 mysql-->
            <dependency>
                <groupId>mysql</groupId>
                <artifactId>mysql-connector-java</artifactId>
                <version>${mysql.version}</version>
            </dependency>
            <!--Mybatis-->
            <dependency>
                <groupId>org.mybatis.spring.boot</groupId>
                <artifactId>mybatis-spring-boot-starter</artifactId>
                <version>${mybatis.version}</version>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <dependencies>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>
    </dependencies>
</project>
```

<span style="color:red;">**关于SpringCloud依赖坐标下的 type 标签和 scope 标签的作用**</span>

**Know1**：Maven项目的父子工程是**单继承**的，即一个 pom 文件中只能有一个 parent 标签

**Know2**：dependencyManagement 标签和 dependencies 标签的区别

> dependencyManagement里只是声明依赖，并不实现引入，因此子项目需要显式的声明需要用的依赖。如果不在子项目中声明依赖，是不会从父项目中继承下来的；只有在子项目中写了该依赖项，并且没有指定具体版本，才会从父项目中继承该项，并且version和scope都读取自父pom。另外如果子项目中指定了版本号，那么会使用子项目中指定的jar版本。
>
> dependencies即使在子模块中不写该依赖项，那么子模块仍然会从父项目中继承该依赖项（全部继承）。
>
> 链接：https://www.jianshu.com/p/c8666474cf9a

**Know3**：通过 `<type>pom</type>` 和 `<scope>import</import>` 可以解决Maven子工程单继承的问题。

 <span style="color:green;">**即 可以在我们的工程中引入多个 pom 工程的 dependencyManagement ，而非只有 parent 标签中的 pom 工程的 dependencyManagement**</span> 

首先，我这个 pom 文件是作为**其它子模块 pom 文件的<span style="color:red;">父类</span>**，在该模块下我使用 <span style="color:red;">parent</span> 标签继承了 SpringBoot 的**所有**依赖(但不是引入jar包！)，这样方便于版本管理。

其次，我<span style="color:red;">还要</span>在项目中用到 SpringCloud 的相关依赖，但是 <span style="color:red;">parent 标签已经被占用了，怎么办？</span>

这时我们就需要使用 ==\<dependencyManagement> + \<dependencies>== 来引入 SpringCloud 的相关依赖，也就是我们常说的需要解决“**Maven依赖单继承问题**”。

```xml
    <dependencyManagement>
        <dependencies>
            <!--SpringCloud-->
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
	</dependencyManagement>
```

一个一个导入会很麻烦而且会使 pom 文件过大，所以官方提供了一个依赖合集——直接引入 **spring-cloud-dependencies**，<span style="color:red;">**即整个cloud的全套的依赖！！**</span>

这样就解决了单继承的问题。要注意的是 `<scope>import</scope>` 必须在 \<dependencyManagement> 下使用并且==<span style="color:red;">必须</span>==声明 `<type>pom</type>` 。

> 当一个父 pom 中的 dependencyManagement 标签中需要导入另一个 pom 中的 dependencyManagement 的时候（此处是想引入 **spring-cloud-dependencies** 工程中的<dependencyManagement>）,则必须同时使用 `<scope>import</scope>` 和 `<type>pom</type>`

==注意：==dependencyManagement 只在父工程（即 pom 类型的 Maven 工程）中声明有效，然后在子工程中引入依赖无需声明版本。**如果在 jar 类型的 maven 工程中添加了 dependencyManagement 是<span style="color:red;">没有意义</span>的。**



- 创建子模块 order_service

pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>cloud-demo</artifactId>
        <groupId>com.djn</groupId>
        <version>1.0</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>order-service</artifactId>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

application.yml

```yaml
server:
  port: 9981
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/cloud_order?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: 1234
    driver-class-name: com.mysql.cj.jdbc.Driver
mybatis:
  type-aliases-package: com.djn.order.domain
  configuration:
    map-underscore-to-camel-case: true
  mapper-locations: classpath*:mapper/*Mapper.xml
logging:
  pattern:
    dateformat: MM-dd HH:ss:SSS
  level:
    com.djn: debug
```

==业务代码详见项目代码==



- 创建子模块 user_service

pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>cloud-demo</artifactId>
        <groupId>com.djn</groupId>
        <version>1.0</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>user-service</artifactId>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
        </dependency>
    </dependencies>

    <build>
        <finalName>app</finalName>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

application.yml

```yaml
server:
  port: 8864
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/cloud_user?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: 1234
    driver-class-name: com.mysql.cj.jdbc.Driver
mybatis:
  type-aliases-package: com.djn.user.domain
  configuration:
    map-underscore-to-camel-case: true
  mapper-locations: classpath*:mapper/*Mapper.xml
logging:
  pattern:
    dateformat: MM-dd HH:ss:SSS
  level:
    com.djn: debug
```

==业务代码详见项目代码==



#### 4.启动测试

分别访问两个服务的接口，得到如下结果

http://localhost:9981/order/1

![Order服务接口访问](./images/Order服务接口访问.png)

http://localhost:8864/user/1

![User服务接口访问](./images/User服务接口访问.png)



### 远程调用

案例：根据订单id查询订单的同时，把订单所属的用户信息一起返回

![远程调用案例效果图](./images/远程调用案例效果图.png)

远程调用方式分析

![远程调用方式分析](./images/远程调用方式分析.png)

既然浏览器、Ajax都可以发起http请求，那么服务也可以。Spring提供了一个工具 <span style="color:red;">RestTemplate</span> 供我们发起各种http请求！

#### 1.注册RestTemplate

在order-service的**OrderApplication**中注册RestTemplate

```java
@MapperScan("com.djn.order.mapper")
@SpringBootApplication
public class OrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }

    /**
     * 创建RestTemplate并注入Spring
     *
     * @return org.springframework.web.client.RestTemplate
     * @date 2023/11/23 18:36
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```

#### 2.注入并使用

改造OrderServiceImpl，实现服务的远程调用

```java
@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private RestTemplate restTemplate;

    @Override
    public Order queryOrderById(Long orderId) {
        //1.查询订单
        Order order = orderMapper.findById(orderId);
        //2.利用RestTemplate发起http请求
        //2.1拼接url路径
        String url = "http://localhost:8864/user/" + order.getUserId();
        //2.2发送http请求，实现远程调用
        User user = restTemplate.getForObject(url, User.class);
        //3.封装User到Order
        order.setUser(user);
        //4.返回
        return order;
    }
}
```

#### 3.调用结果

![OrderService远程调用结果](./images/OrderService远程调用结果.png)



## Eureka注册中心

### 提供者与消费者

服务提供者：一次业务中，被其他服务调用的服务（提供接口给其它服务）

服务消费者：一次业务中，调用其它服务的服务（调用其它服务提供的接口）

~一个服务既可以是提供者，也可以是消费者。例如：当服务A调用服务B，服务B再调用服务C的时候，服务B既是A的提供者，又是C的消费者。~



### Eureka原理分析

#### 远程调用出现的问题

在 **cloud-demo** 的服务拆分案例中，我们在实现远程调用时，是把调用服务的url在代码中写死的，这样非常不灵活。

> 在实际工作中，我们会有 *开发环境* 、*测试环境* 、*生产环境*  等多个环境，在不同的环境中我们服务的地址也会发生变化。如果使用硬编码的方式，每次更换环境都需要我们手动修改地址，重新编译打包，这样非常的麻烦。
>
> 为了将来应对更多的并发，我们的每一种服务都有可能部署多个实例，形成一个集群。此时我们调用的服务的url就变得不确定了，硬编码的方式也变得不切实际了。
>
> - ==那么我们的服务消费者该如何获取服务提供者的地址信息？==
>
> - ==如果有多个服务者，消费者该如何选择？==
> - ==消费者如何得知服务提供者的健康状态？==



#### Eureka的作用

在Eureka的结构当中，存在两个概念：**eureka-server** 和 **eureka-client**。

其中 **注册中心** 就是 eureka-server，它的作用就是记录、管理这些微服务；而 **服务提供者&服务消费者** 统称为 eureka-client，它们每个服务在每次启动时都会把自己的信息注册到 eureka注册中心。

![eureka工作原理-注册服务信息](./images/eureka工作原理-注册服务信息.png)



当有服务消费者想要“消费”的时候，就会到 eureka注册中心 拉取目标服务的信息。当目标服务有多个时，可以通过**负载均衡**挑选出一个目标服务以供“消费”。

![eureka工作原理-拉取信息&负载均衡](./images/eureka工作原理-拉取信息&负载均衡.png)



当然，拉取的目标服务都是健康的，因为已经注册的服务每隔30秒都会向注册中心发送一次心跳，以此来确保自己是健康状态的。所以那些非健康状态的服务信息在被拉取之前就会被筛除掉。

![eureka工作原理-发送心跳](./images/eureka工作原理-发送心跳.png)



#### 总结

在Eureka架构中，微服务角色有两类：

- EurekaServer：服务端，注册中心
    - 记录服务信息
    - 心跳监控
- EurekaClient：客户端
    - Provider：服务提供者，例如案例中的 user-service
        - 注册自己的信息到EurekaServer
        - 每隔30秒向EurekaServer发送心跳
    - Consumer：服务消费者，例如案例中的 order-service
        - 根据服务名称从EurekaServer拉取服务列表
        - 基于服务列表做负载均衡，选中一个微服务后发起远程调用



### Eureka实用案例

搭建EurekaServer ——> 将user-service、order-service都注册到Eureka ——> 在order-service中完成服务拉取，然后通过负载均衡挑选一个服务实现远程调用

#### 1.搭建注册中心

①创建一个子模块 eureka-server，并引入eureka-server的相关依赖

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>com.djn</groupId>
        <artifactId>cloud-demo</artifactId>
        <version>1.0</version>
    </parent>

    <artifactId>eureka-server</artifactId>

    <properties>
        <maven.compiler.source>8</maven.compiler.source>
        <maven.compiler.target>8</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>
        <!--eureka服务端-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
        </dependency>
    </dependencies>

</project>
```

②编写启动类 `EurekaApplication`，添加 `@EnableEurekaServer` 注解

```java
package com.djn.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class EurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaApplication.class, args);
    }
}
```

③添加 `application.yml` 文件，编写如下配置

```yaml
server:
  port: 10086 # 服务端口

# 为 Eureka自身的服务注册 而做的配置
spring:
  application:
    name: eurekaServer # Eureka的服务名称
eureka:
  client:
    service-url: # eureka的地址信息
      defaultZone: http://127.0.0.1:10086/eureka
```

==tips：== eureka-server 本质也是一个服务，所以它在启动时会将自身也注册到 eureka 上（这是为了方便将来eureka集群之间的通信），

这就要求我们配置 **defaultZone** 这个属性（单机eureka时只需要写一个地址，eureka集群时配置多个地址用逗号隔开）。

④启动项目并访问url：http://localhost:10086/

![eureka管理页面](./images/eureka管理页面.png)

图中，EUREKA_SERVER 服务实例的 Status 一栏中的<span style="color:red;">“UP”表示正常状态</span>，如果是<span style="color:blue;">“DOWN”则表示该服务挂掉了</span>。



#### 2.服务注册

需求：将 user-service 服务注册到 eureka-server

①在 user-service 的 pom 文件中引入如下依赖

```xml
<!--eureka客户端-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

②在 application.yml 文件中编写如下配置

```yaml
spring:
  application:
    name: userService # user服务的服务名称
eureka:
  client:
    service-url: # eureka的地址信息
      defaultZone: http://127.0.0.1:10086/eureka/
```

③ order-service 的注册步骤同理，启动项目后到 eureka 中查看注册的服务实例

![Eureka中注册的服务实例](./images/Eureka中注册的服务实例.png)

④==如何启动多个相同的服务==

我们可以将一个服务多次启动，模拟多实例部署，但为了避免端口冲突，需要修改端口配置：

第一步：

![IDEA启动多个相同服务1](./images/IDEA启动多个相同服务1.png)

第二步：

![IDEA启动多个相同服务2](./images/IDEA启动多个相同服务2.png)

第三步：

![IDEA启动多个相同服务3](./images/IDEA启动多个相同服务3.png)

最终效果：

![向eureka中注册多个相同服务实例](./images/向eureka中注册多个相同服务实例.png)



#### 3.服务发现

需求：在 order-service 完成服务拉取

~服务拉取是基于服务名称获取服务列表，然后再对服务列表做负载均衡~

①修改 order-service 的代码，修改访问user服务的url路径：用服务名称代替ip、端口

```java
String url = "http://userservice/user/" + order.getUserId();
```

②在 order-service 项目的启动类中注册RestTemplate时，添加**负载均衡**注解

```java
@Bean
@LoadBalanced
public RestTemplate restTemplate() {
    return new RestTemplate();
}
```

③重启 order-service 项目后，对服务接口进行多次访问，可以看到每次访问都会<span style="color:red;">通过负载均衡做不同的 user-service 服务调用</span>

![orderService的服务访问](./images/orderService的服务访问.png)

![userService的服务调用](./images/userService的服务调用.png)

![userService的服务调用2](./images/userService的服务调用2.png)



## 负载均衡

SpringCloud 中的负载均衡一开始是由一个名为 Ribbon 的组件实现的；<span style="color:red;">**但由于 Ribbon 已经停更，SpringCloud 在 Hoxton.M2 版本将 Ribbon 移除，并使用 SpringCloud Loadbalancer作为其替代品！**</span>

![SpringCloudLoadbalancer替代Ribbon](./images/SpringCloudLoadbalancer替代Ribbon.png)

二者区别

|           组件           | 组件提供的负载策略                                           | 支持负载的客户端                            |
| :----------------------: | :----------------------------------------------------------- | :------------------------------------------ |
|          Ribbon          | 轮询、随机、重试策略、权重优先策略、<br/>BestAvailableRule：会先过滤掉由于多次访问故障而处于断路器跳闸状态的服务，然后选择一个并发量最小的服务、<br/>AvailabilityFilterRule：先过滤掉故障实例，再选择并发较小的实例、<br/>ZoneAvoidanceRule：默认规则，复合判断server所在区域的性能和server的可用性选择服务器 | Feign或OpenFeign、RestTemplate等Web调用工具 |
| SpringCloud Loadbalancer | 轮询、随机                                                   | Ribbon所支持的、WebClient                   |

LoadBalancer 的优势主要是，支持**响应式编程**的方式**异步访问**客户端，依赖 Spring Web Flux 实现客户端负载均衡调用。



### 负载均衡原理

*负载均衡的流程*（图中SpringCloud版本较低，所以还在使用Ribbon做负载均衡）

![负载均衡的流程](./images/负载均衡的流程.png)

在 Eureka实用案例 中，做“服务发现”时我们在给 RestTemplate 注册时加上了**负载均衡**注解

![负载均衡原理1](./images/负载均衡原理1.png)

在 `LoadBalancerAutoConfiguration` 自动配置类中会给标有 @LoadBalanced 注解的 RestTemplate 添加一个负载均衡拦截器，这样就能通过 **LoadBalancerInterceptor** 去添加负载均衡策略

```java
/**
 * 对 阻塞客户端负载均衡 的自动配置
 * Auto-configuration for blocking client-side load balancing.
 *
 * @author Spencer Gibb
 * @author Dave Syer
 * @author Will Tran
 * @author Gang Li
 * @author Olga Maciaszek-Sharma
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(RestTemplate.class)
@ConditionalOnBean(LoadBalancerClient.class)
@EnableConfigurationProperties(LoadBalancerClientsProperties.class)
public class LoadBalancerAutoConfiguration {

    /**
     * 会注入标有 @LoadBalanced 注解的 RestTemplate
     */
	@LoadBalanced
	@Autowired(required = false)
	private List<RestTemplate> restTemplates = Collections.emptyList();

	@Autowired(required = false)
	private List<LoadBalancerRequestTransformer> transformers = Collections.emptyList();

    // 函数名：负载均衡RestTemplate初始化器弃用后，返回智能初始化单例对象
    // 推测：该函数的作用是 注册一个 “当RestTemplate设置负载均衡拦截器失败后，会被调用来给RestTemplate设置负载均衡拦截器” 的单实例对象
	@Bean
	public SmartInitializingSingleton loadBalancedRestTemplateInitializerDeprecated(
			final ObjectProvider<List<RestTemplateCustomizer>> restTemplateCustomizers) {
		return () -> restTemplateCustomizers.ifAvailable(customizers -> {
			for (RestTemplate restTemplate : LoadBalancerAutoConfiguration.this.restTemplates) {
				for (RestTemplateCustomizer customizer : customizers) {
					customizer.customize(restTemplate);
				}
			}
		});
	}

	@Bean
	@ConditionalOnMissingBean
	public LoadBalancerRequestFactory loadBalancerRequestFactory(LoadBalancerClient loadBalancerClient) {
		return new LoadBalancerRequestFactory(loadBalancerClient, this.transformers);
	}

    // 在静态内部类中 配置负载均衡拦截器 和 RestTemplate定制器
	@Configuration(proxyBeanMethods = false)
	@Conditional(RetryMissingOrDisabledCondition.class)
	static class LoadBalancerInterceptorConfig {

		@Bean
		public LoadBalancerInterceptor loadBalancerInterceptor(LoadBalancerClient loadBalancerClient,
				LoadBalancerRequestFactory requestFactory) {
            // 返回创建的 LoadBalancerInterceptor 负载均衡拦截器实例对象
			return new LoadBalancerInterceptor(loadBalancerClient, requestFactory);
		}

		@Bean
		@ConditionalOnMissingBean
		public RestTemplateCustomizer restTemplateCustomizer(final LoadBalancerInterceptor loadBalancerInterceptor) {
            // 给 RestTemplate 添加 LoadBalancerInterceptor 负载均衡拦截器
			return restTemplate -> {
				List<ClientHttpRequestInterceptor> list = new ArrayList<>(restTemplate.getInterceptors());
				list.add(loadBalancerInterceptor);
				restTemplate.setInterceptors(list);
			};
		}

	}
    
    // 略...
}
```

负责执行拦截动作的是 **LoadBalancerInterceptor** 类

![负载均衡原理2](./images/负载均衡原理2.png)

**LoadBalancerInterceptor** 实现了 **ClientHttpRequestInterceptor** 接口。

![负载均衡原理3](./images/负载均衡原理3.png)

当我们给 **LoadBalancerInterceptor** 的 *intercept* 方法打上断点后，会发现它拦截了 RestTemplate 发起的HTTP请求

![负载均衡原理4](./images/负载均衡原理4.png)

在SpringCloud Loadbalancer 替代 Ribbon 后，上图中负责执行负载均衡策略的实现类也就从 **RibbonLoadBalancerClient** 变成了 **BlockingLoadBalanerClient**，在实现类的 *execute* 方法中会执行负载均衡策略

![负载均衡原理5](./images/负载均衡原理5.png)

把断点打到 *choose* 方法中后，可以看到其实是通过 **RoundRobinLoadBalancer** 的负载均衡策略来获取最终的目标服务实例的

![负载均衡原理6](./images/负载均衡原理6.png)

通过 `Ctrl` + `H` 我们可以看到 **ReactiveLoadBalancer** 的层级结构如下，其中 **RoundRobinLoadBalancer** 是它的<span style="color:red;">子实现类</span>

![负载均衡原理7](./images/负载均衡原理7.png)

在 **RoundRobinLoadBalancer** 中，是如何获取到 Eureka 中注册的服务列表的呢？

![负载均衡原理8](./images/负载均衡原理8.png)



#### 时序流程图

==对 RestTemplate 请求调用时经过的类流程图如下：==

![负载均衡工作原理的时序流程](./images/负载均衡工作原理的时序流程.png)

*负载均衡的详细流程*

![负载均衡详细流程图](./images/负载均衡详细流程图.png)



### 负载均衡策略

Ribbon 的负载均衡规则是一个叫做 **IRule** 的接口来定义的，每一个子接口都是一种规则

![IRule的子接口](./images/IRule的子接口.png)

这些负载均衡策略如下

|                内置负载均衡规则类                 |                           规则描述                           |
| :-----------------------------------------------: | :----------------------------------------------------------: |
|                  RoundRobinRule                   |  简单轮训服务列表来选择服务器，它是Ribbon默认的负载均衡规则  |
|             AvailabilityFilteringRule             | 对以下两种服务器进行忽略：<br>(1)在默认情况下，这台服务器如果3次连接失败，这台服务器就会被设置为“短路”状态。短路状态将持续30秒，如果再次连接失败，短路的持续时间就会几何级地增加。<br>(2)并发数过高的服务器。如果一个服务器的并发连接数过高，配置了AvailabilityFilteringRule规则的客户端也会将其忽略。并发连接数的上限，可以由客户端的\<clientName>.\<clientConfigNameSpace>.ActiveConnectionsLimit属性进行配置。 |
|              WeightResponseTimeRule               | 为每一个服务器赋予一个权重值。服务器响应时间越长，这个服务器的权重就越小。这个规则会随机选择服务器，这个权重值会影响服务器的选择。 |
| <span style="color:red;">ZoneAvoidanceRule</span> | 以区域可用的服务器为基础进行服务器的选择。使用Zone对服务器进行分类，这个Zone可以理解为一个机房、一个机架等。而后再对Zone内的多个服务做轮询。 |
|                 BestAvailableRule                 |       忽略那些短路的服务器，并返回并发数较低的服务器。       |
|                    RandomRule                     |                  随机选择一个可用的服务器。                  |
|                     RetryRule                     |                     重试机制的选择逻辑。                     |

通过定义IRule实现，可以修改负载均衡规则，有两种方式：

1.代码方式：在 order-service 中的 OrderApplication 类中，定义一个新的IRule

==此方式可以改变所有 order-service 调用的服务的负载均衡策略==

```java
@Bean
public IRule randomRule() {
    return new RandomRule();
}
```

2.配置文件方式：在 order-service 的 application.yml 文件中，添加新的配置也可以修改规则

==此方式可以改变指定服务的负载均衡策略==

```yaml
userservice:
  ribbon:
    NFLoadBalancerRuleClassName: com.netflix.loadbalancer.RandomRule # 负载均衡规则
```



#### 新版本SpringCloud自定义负载均衡策略

> 2020年前的SpringCloud是采用Ribbon作为负载均衡实现的，
>
> 但是2020后采用了LoadBalancer作为替代！

LoadBalancer 默认提供了两种负载均衡策略：

- **RandomLoadBalancer** —— 随机分配策略
- **RoundRobinLoadBalancer** —— 轮询分配策略（默认）

[参考文章](https://blog.csdn.net/gsls200808/article/details/132603527)



##### 1.切换 RandomLoadBalancer 策略

①新建配置类 CustomLoadBalancerConfiguration.java

![CustomLoadBalancerConfiguration类](./images/CustomLoadBalancerConfiguration类.png)

（<span style="color:red;">不要加 @Configuration </span>，会导致 `environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME)` 返回 Null，后面调用RestTemplate失败）

```java
package com.djn.order.config;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.RandomLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.RoundRobinLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;


/**
 * 自定义负载均衡策略配置类
 */
public class CustomLoadBalancerConfiguration {

    @Bean
    public ReactorLoadBalancer<ServiceInstance> randomLoadBalancer(Environment environment,
                                                                   LoadBalancerClientFactory factory) {
        String name = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);

        // 返回轮询负载均衡策略（默认）
        //return new RoundRobinLoadBalancer(factory.getLazyProvider(name, ServiceInstanceListSupplier.class), name);

        // 返回随机轮询负载均衡策略
        return new RandomLoadBalancer(factory.getLazyProvider(name, ServiceInstanceListSupplier.class), name);
    }
}
```

==[官方文档](https://docs.spring.io/spring-cloud-commons/docs/current/reference/html/#eager-loading-of-loadbalancer-contexts) 中也说明不能直接用 @Configuration==

![官方说明不能用@Configuration](./images/官方说明不能用@Configuration.png)



②通过在启动类上（也可以在配置类上）添加 @LoadBalancerClient ，可以单独为某一个服务指定负载均衡策略。[参考文章](https://blog.51cto.com/knifeedge/5847505)

其中，**name 或 value** 为需要指定负载均衡策略的服务名称；**configuration** 为我们上面自定义的负载均衡策略配置类

```java
package com.djn.order;

import com.djn.order.config.CustomLoadBalancerConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * OrderService启动类
 */
@LoadBalancerClient(name = "userService", configuration = CustomLoadBalancerConfiguration.class)
@MapperScan("com.djn.order.mapper")
@SpringBootApplication
public class OrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }


    /**
     * 创建RestTemplate并注入Spring
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```

另外，我们也可以通过 @LoadBalancerClients 为所有服务指定统一的负载均衡策略。

```java
@LoadBalancerClients(defaultConfiguration= CustomLoadBalancerConfiguration.class)
```

最后，我们还可以通过 @LoadBalancerClient 与 @LoadBalancerClients 的组合使用，为多个服务指定统一的负载均衡策略。

```java
@LoadBalancerClients(value = {
    @LoadBalancerClient(value = "stores", configuration = StoresLoadBalancerClientConfiguration.class),
    @LoadBalancerClient(value = "customers", configuration = CustomersLoadBalancerClientConfiguration.class)
})
```

其中，**value** 为 @LoadBalancerClient 数组；**defaultConfiguration** 为默认的负载均衡策略配置。



③可以看到，现在调用 UserService 的负载均衡策略是 **RandomLoadBalancer**

![调用RandomLoadBalancer负载均衡策略](./images/调用RandomLoadBalancer负载均衡策略.png)



##### 2.使用自定义负载均衡策略

如果我们想自定义策略，可以参考 **RoundRobinLoadBalancer** 类自己实现。例如：调用3次轮换的自定义策略

①创建 CustomLoadBalancer 类，实现自定义的负载均衡逻辑

```java
package com.djn.order.rule;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultResponse;
import org.springframework.cloud.client.loadbalancer.EmptyResponse;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 自定义负载均衡策略：每3次调用后轮换
 */
public class CustomLoadBalancer implements ReactorServiceInstanceLoadBalancer {

    private static final Log log = LogFactory.getLog(CustomLoadBalancer.class);

    /**
     * 被调用的次数
     */
    private int total = 0;

    /**
     * 当前提供服务的实例的索引
     */
    private int index = 0;

    private ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierObjectProvider;

    private String serviceId;

    public CustomLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierObjectProvider,
                              String serviceId) {
        this.serviceInstanceListSupplierObjectProvider = serviceInstanceListSupplierObjectProvider;
        this.serviceId = serviceId;
    }

    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {
        ServiceInstanceListSupplier supplier = serviceInstanceListSupplierObjectProvider.getIfAvailable();
        assert supplier != null;
        return supplier.get().next().map(this::getInstanceResponse);
    }

    private Response<ServiceInstance> getInstanceResponse(List<ServiceInstance> instances) {
        log.info("进入自定义负载均衡");

        if(instances.isEmpty()) {
            return new EmptyResponse();
        }

        log.info("每个服务访问3次后轮换");
        int size = instances.size();

        ServiceInstance serviceInstance = null;

        while (serviceInstance == null) {
            System.out.println("======" + total);
            System.out.println("======" + index);
            if (total < 3) {
                serviceInstance = instances.get(index);
                total++;
            } else {
                total = 0;
                index ++;
                if (index >= size) {
                    index = 0;
                }
                serviceInstance = instances.get(index);
            }
        }

        return new DefaultResponse(serviceInstance);
    }
}
```

②修改 CustomLoadBalancerConfiguration 类，指定我们自定义的负载均衡策略

```java
/**
 * 自定义负载均衡策略配置类
 */
public class CustomLoadBalancerConfiguration {

    @Bean
    public ReactorLoadBalancer<ServiceInstance> randomLoadBalancer(Environment environment,
                                                                   LoadBalancerClientFactory factory) {
        String name = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);

        // 返回轮询负载均衡策略（默认）
        //return new RoundRobinLoadBalancer(factory.getLazyProvider(name, ServiceInstanceListSupplier.class), name);

        // 返回随机轮询负载均衡策略
        //return new RandomLoadBalancer(factory.getLazyProvider(name, ServiceInstanceListSupplier.class), name);

        // 返回自定义负载均衡策略
        return new CustomLoadBalancer(factory.getLazyProvider(name, ServiceInstanceListSupplier.class), name);
    }
}
```

③最终的运行结果如下

![自定义负载均衡策略运行效果1](./images/自定义负载均衡策略运行效果1.png)



![自定义负载均衡策略运行效果2](./images/自定义负载均衡策略运行效果2.png)



![自定义负载均衡策略运行效果3](./images/自定义负载均衡策略运行效果3.png)



### 懒加载

**饥饿加载**

Ribbon 默认是采用”懒加载“的，即第一次访问时才会去创建LoadBalancerClient，这样请求的时间会很长。

而“饥饿加载”则会在项目启动时创建，降低第一次访问的耗时，通过下面的配置开启“饥饿加载”：

```yaml
ribbon:
  eager-load:
    enabled: true # 开启饥饿加载
    clients: # clients是一个集合，多服务的写法如下 
      - userservice # 指定对 userservice 这个服务进行饥饿加载
      - xxxservice
```



## Nacos注册中心

### 认识和安装Nacos

[Nacos](https://nacos.io/zh-cn/index.html) 是阿里巴巴的产品，现在是 SpringCloud 的一个组件。相比 Eureka 功能更加丰富，在国内受欢迎程度较高。

![Nacos官网](./images/Nacos官网.png)

#### 安装Nacos

在 [官方文档](https://nacos.io/zh-cn/docs/v2/quickstart/quick-start.html) 中可以找到安装包的下载链接，选择版本后将安装包下载下来（这里下载的是2.2.3版本）

![Nacos下载](./images/Nacos下载.png)

下载完成后将压缩文件解压

![Nacos的目录结构](./images/Nacos的目录结构.png)

打开`conf`目录下的 application.properties 文件，修改 Nacos 的启动端口号（默认为 8848）

![Nacos端口号](./images/Nacos端口号.png)

在`bin`目录下运行 cmd 窗口，执行以下命令即可成功启动Nacos

```shell
startup.cmd -m standalone
```

~其中-m指的是mode，standalone值代表以单机模式启动Nacos~

启动成功后的效果图如下

![Nacos启动成功](./images/Nacos启动成功.png)

随后我们访问 Nacos 首页 http://localhost:8848/nacos/index.html

![Nacos首页](./images/Nacos首页.png)

可以注意到，首页上方红框中的提示信息，官方文档中给出了如下的说明

![Nacos官方说明](./images/Nacos官方说明.png)

由此可知，我们需要手动配置才能让Nacos启用登录功能，配置内容如下

![Nacos开启登录的配置](./images/Nacos开启登录的配置.png)

修改完配置后，再次启动Nacos，可以看到出现了登录页面

![Nacos登录页面](./images/Nacos登录页面.png)

==其中“身份识别键值对”的具体作用是什么呢？==

这两个属性是权限认证的配置项，用于标识来自其他服务器的请求。当一个请求的请求头中配置了指定的属性时，即可跳过权限认证。

[参考文章](https://blog.csdn.net/qq_41057885/article/details/125059535)

我们在开启Nacos权限认证后，调用 /nacos/v1/cs/configs 接口，会直接跳转到登录界面，且提示 <span style="color:red;">403</span>，服务器拒绝访问。

![身份识别键值对的作用1](./images/身份识别键值对的作用1.png)

此时，我们选择编辑并重发该请求，向该请求头中新增 spring=stone 属性（这里填写我们配置文件中设置的键值对）。

![身份识别键值对的作用2](./images/身份识别键值对的作用2.png)

此时，请求成功了，拿到了Nacos的配置管理列表。

![身份识别键值对的作用3](./images/身份识别键值对的作用3.png)



### Nacos快速入门

[Spring官方手册](https://spring-cloud-alibaba-group.github.io/github-pages/2021/en-us/index.html#_more_information_about_nacos_discovery_starter_configurations)

![Nacos服务发现的官方手册](./images/Nacos服务发现的官方手册.png)

[Nacos官方案例](https://nacos.io/zh-cn/docs/archive/use-nacos-with-springcloud.html)

![Nacos服务发现官方案例](./images/Nacos服务发现官方案例.png)

如何查看SpringCloudAlibaba提供的Nacos服务发现案例

![查看SpringCloudAlibaba提供的Nacos服务发现案例](./images/查看SpringCloudAlibaba提供的Nacos服务发现案例.png)



#### 服务的注册与发现

<span style="color:red;">在 SpringCloud组件中，有一个非常重要的模块——SpringCloudCommons，其主要作用就是 **定义接口规范** 的。</span>

其中 *DiscoveryClient* 接口定义了 “服务发现” 的规范；*ServiceRegistry* 接口定义了 “服务注册” 的规范，所以不论是 Eureka 还是 Nacos 都会在做服务注册发现时遵循这套接口规范。==因此，当我们把 cloud-demo 项目中的注册中心从 Eureka 更换为 Nacos 时，是不需要改动服务提供者和服务消费者的代码的！==

![SpringCloudCommons](./images/SpringCloudCommons.png)



1.在 cloud-demo 父工程中添加 spring-cloud-alibaba 的管理依赖

```xml
<!--SpringCloudAlibaba-->
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-alibaba-dependencies</artifactId>
    <version>2021.0.4.0</version>
    <type>pom</type>
    <scope>import</scope>
</dependency>
```

SpringBoot和SpringCloudAlibaba的版本对应关系

![SpringBoot和SpringCloudAlibaba的版本对应关系](./images/SpringBoot和SpringCloudAlibaba的版本对应关系.png)



2.注释掉 order-service 和 user-service 中原有的 Eureka 依赖，并添加 Nacos 的客户端依赖

```xml
<!--Nacos客户端-->
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
</dependency>
```



3.根据 “Nacos官方案例” 可知，我们还需要在**服务消费者**中单独引入**SpringCloudLoadbalancer**，

并做如下配置来启用**SpringCloudAlibaba**提供的Nacos负载均衡策略：

![Nacos服务发现需引入spring-cloud-loadbalancer](./images/Nacos服务发现需引入spring-cloud-loadbalancer.png)

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-loadbalancer</artifactId>
    </dependency>
</dependencies>
```

```yaml
spring:
  cloud:
    loadbalancer:
      nacos:
        enabled: true
      ribbon:
        enabled: false
```



4.修改 user-service 和 order-service 中的 application.yml 文件，注释 Eureka 地址，添加 Nacos 地址

```yaml
spring:
  cloud:
    nacos:
      server-addr: localhost:8848 # Nacos服务端地址
      discovery:
        username: nacos # Nacos开启权限验证后，需要设置登录用户名
        password: nacos # Nacos开启权限验证后，需要设置登录密码
        namespace: f20f0bdd-8b41-41a2-96c8-6aa946f903fc # 设置服务在Nacos中所属的命名空间ID
        group: stone1 # 设置服务在Nacos中所属的分组
```

- username 和 password：在安装Nacos的时候，我们开启了Nacos的鉴权功能，所以必须设置这两项，我们的服务才能成功注册到Nacos中。
- namespace：是 Nacos 针对于企业级开发设计用来针对于不同环境的区分。
    - 比如正在企业开发时有测试环境、生产环境等其他环境，为了保证不同环境配置实现隔离，提出了namespace的概念！
    - Nacos 中存在一个默认的命名空间 `public`，所有配置以及服务注册，在没有指定命名空间时都会默认从 `public` 这个命名空间拉取配置以及注册到该命名空间下的注册表中！！！
    - <span style="color:orange;">**同名的命名空间只能创建一个！**</span>
    - <span style="color:red;">**服务注册到不同的 namespace 下，服务间无法通过指定服务名进行负载通信！！！**</span>
    - <span style="color:red;">即服务间如果需要通信，则需要保证其discovery要在一个命名空间下（实际上一般都不会更改服务注册的命名空间）</span>

![Nacos的命名空间](./images/Nacos的命名空间.png)

- group：在 Nacos 中，可以根据业务需求，对不同的服务以及配置进行分组，通过不同的字符串名（分组名）来表示不同的分组。
    - Nacos 中如果未显示的指明分组名，那么就会默认的划分在DEFAULT_GROUP分组之中！
    - <span style="color:red;">**Group间的服务仍然是隔离的，即服务注册到不同的分组时，无法通过指定服务名进行负载调用！**</span>

> 前边已经划分了各个区域（`namespace`）了，那还来个分组，是啥意思呢？
>
> 简单！零食区中，薯片是放一个柜的吧！！糖是放一个柜的吧！！鲫鱼与鲫鱼是在一起的吧！！猪排与猪排是在一起的吧！ 这便是分组，实际上也是一个更细微的环境隔离机制罢了！！！

[参考文章](https://blog.csdn.net/leilei1366615/article/details/111405644)

==关于 Nacos 的更过配置信息参考：== [Nacos Starter的更多配置项信息](https://github.com/alibaba/spring-cloud-alibaba/wiki/Nacos-discovery) 

![NacosStarter的更多配置](./images/NacosStarter的更多配置.png)



5.启动测试，<span style="color:red;">在启动项目之前一定要先将 Nacos 服务端 启动起来！！！</span>

![注册到Nacos中的服务](./images/注册到Nacos中的服务.png)



6.进行服务间的调用  http://localhost:9981/order/1

<span style="color:red;">**注意：**</span>Nacos 和 Eureka 不同，对于服务名称的书写要求很严格，

所以为了保证服务的成功调用，我们要确保代码中调用的服务名称和Nacos中注册的服务名称是一致的。

![保证调用的服务名称和注册的服务名称的一致性](./images/保证调用的服务名称和注册的服务名称的一致性.png)

最终成功调用的结果如下

![Nacos中的服务调用结果](./images/Nacos中的服务调用结果.png)



### Nacos服务分级存储模型

现在我们已经知道，一个服务是可以包含多个实例的。不过，随着我们业务规模越来越扩大，服务的实例也会增加，但是如果还像之前一样**把所有服务实例都部署在一个机器或者说一个机房中**，那么就像把所有鸡蛋都放在一个篮子里一样是很不安全的。

为了解决这个安全隐患，我们会**把一个服务的多个实例部署到多个机房**（一些有实力的企业还会将这些机房分别布置在不同地域），以此来实现<span style="color:red;">**容灾**</span>。

![Nacos分级存储模型](./images/Nacos分级存储模型.png)

在 Nacos 服务分级存储模型中就引入了这样的“机房”概念，把某个 <span style="color:red;">服务</span> 在同一个机房的多个 <span style="color:red;">实例</span> 称之为 <span style="color:red;">“集群”</span> 。



#### 服务跨集群调用问题

==假设以下的情况：==

某公司在 **杭州** 和 **上海** 两个地域都部署了服务器机房，

然后将一个项目的 order-service 和 user-service 的服务实例部署到了这两个机房的服务器上。

此时，如果一个 order-service 实例想要访问 user-service 实例，那么会出现两种情况：

- 1.访问本地服务器上的 user-service 实例
- 2.访问异地服务器上的 user-service 实例

对比之下，本地服务器在物理距离上比较短，响应速度更快，延迟也就更低，而跨集群调用延迟较高。

所以<span style="color:red;">服务调用应该尽可能选择本地集群的服务</span>，只有当本地集群不可用时，再去考虑访问其他集群。

Nacos 引入”集群“的概念，就是为了<span style="color:blue;">**尽可能避免出现夸集群调用**</span>。

![服务夸集群调用问题](./images/服务夸集群调用问题.png)



#### 为服务设置集群属性

1.修改 application.yml，添加如下内容

```yaml
spring:
  cloud:
    nacos:
      discovery:
        cluster-name: HZ # 配置集群名称，也就是机房位置，例如：HZ 杭州
```



2.在Nacos控制台可以看到集群变化

![Nacos中的服务实例在不同的集群下](./images/Nacos中的服务实例在不同的集群下.png)



总结

1.Nacos服务分级存储模型

​	一级是服务，例如 UserService

​	二级是集群，例如杭州或上海

​	三级是实例，例如杭州房的某台部署了UserService实例的服务器

2.如何设置实例的集群属性：参照上文



#### Nacos 负载均衡策略

在“Nacos快速入门”章节中，我们已经知道了可以通过配置文件的方式来启用 Nacos 负载均衡策略 **NacosLoadBalancer** ，

```yaml
spring:
  cloud:
    loadbalancer:
      nacos:
        enabled: true
```

那么这个由 SpringCloud Alibaba 提供的负载均衡策略有什么特点呢？

<span style="color:blue;">**其最大的特点就是：能够让 服务消费者 在调用 服务提供者 时，优先选择同一个集群的服务提供者！**</span>

<span style="color:green;">当同一个集群的服务提供者有多个实例时，又会采用**随机**方式进行负载均衡！</span>

<span style="color:red;">只有当同一个集群中没有可以调用的服务实例时，才会去其他集群进行远程调用。</span>

![启用了Nacos负载均衡策略](./images/启用了Nacos负载均衡策略.png)

除了配置文件的方式，我们还可以采用自定义负载均衡策略配置类的方式来启用 **NacosLoadBalancer**

```java
/**
 * 自定义负载均衡策略配置类
 */
public class CustomLoadBalancerConfiguration {

    @Bean
    public ReactorLoadBalancer<ServiceInstance> randomLoadBalancer(Environment environment,
                                                                   LoadBalancerClientFactory factory,
                                                                   NacosDiscoveryProperties properties) {
        String name = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);

        // 返回轮询负载均衡策略（默认）
        //return new RoundRobinLoadBalancer(factory.getLazyProvider(name, ServiceInstanceListSupplier.class), name);

        // 返回随机轮询负载均衡策略
        //return new RandomLoadBalancer(factory.getLazyProvider(name, ServiceInstanceListSupplier.class), name);

        // 返回自定义负载均衡策略
        //return new CustomLoadBalancer(factory.getLazyProvider(name, ServiceInstanceListSupplier.class), name);

        // 返回Nacos的负载均衡策略
        return new NacosLoadBalancer(factory.getLazyProvider(name, ServiceInstanceListSupplier.class), name, properties);
    }
}
```

==注意点==：我们自定义的负载均衡策略配置类会 <span style="color:red;">**覆盖**</span> application.yml 文件中的 `spring.cloud.loadbalancer.nacos.enabled=true` 属性的效果



#### 为服务设置权重属性

实际部署中会出现这样的场景：

- 服务器设备性能有差异，部分实例所在机器性能较好，另一些较差，我们希望性能好的机器承担更多的用户请求

Nacos 提供了权重配置来控制访问频率，权重越大则访问频率越高。在 Nacos 控制台可以设置实例的权重值：

![Nacos控制台设置服务实例的权重](./images/Nacos控制台设置服务实例的权重.png)

将权重设置为0.1，测试可以发现该服务被访问的频率大大降低了。

OrderService被访问多次，

![OrderService被访问多次](./images/OrderService被访问多次.png)

但是端口号7749的UserService却只被调用了2次！

![UserService7749只被调用了2次](./images/UserService7749只被调用了2次.png)



### Nacos环境隔离

Nacos 中服务存储和数据存储的最外层都是一个名为 **namespace** 的东西，用来做最外层的隔离。

详见“ Nacos 快速入门”一节的“服务注册与发现”。



## 注册中心对比

### Nacos细节分析

1、服务消费者拉取服务信息的动作不是**按需执行**的，而是<span style="color:red;">**定时执行**</span>的，这样可以减小注册中心的压力。（==在Eureka中也是如此==）

每隔一定的时间（例如30s），服务消费者会向注册中心拉取一次最新的服务信息，并将它们缓存起来，然后在接下来的一段时间内直接从缓存中获取服务信息，这样就大大减小了注册中心承担的访问压力。

2、在 Nacos 中，服务实例会被划分成 <span style="color:red;">临时实例</span> 和 <span style="color:blue;">非临时实例</span>，两者的**健康检测机制**是不一样的。其中：

- 临时实例采用心跳检测（与Eureka相同），并且当临时实例挂掉时，它会被 Nacos 从列表中剔除。
- 非临时实例则由 Nacos 主动发请求询问，并且当非临时实例挂掉了，Nacos 会等待其恢复正常。

![Nacos中的临时实例](./images/Nacos中的临时实例.png)

3、在服务信息的拉取方式上，Eureka 与 Nacos 是有区别的

Eureka 的服务信息只会由<span style="color:green;">服务消费者单方面的拉取</span>，这样的信息更新不够及时；

而 Nacos 采用了<span style="color:red;">服务消费者拉取 + 主动推送变更信息</span>二者相结合的方式。

![Nacos注册中心细节分析](./images/Nacos注册中心细节分析.png)



### 非临时实例配置

服务注册到Nacos时，可以通过下面的配置来注册为非临时实例

```yaml
spring:
  cloud:
    nacos:
      discovery:
        ephemeral: false # 设置为非临时实例
```

我们可以在Nacos控制台中看到如下结果

![非临时实例服务](./images/非临时实例服务.png)

其中端口号7749的服务实例健康状态异常，但是并没有被Nacos从列表中剔除掉。



### 总结

1.Nacos 与 Eureka 的共同点

- 都支持服务注册和服务拉取
- 都支持服务提供者心跳方式做健康检测

2.Nacos 与 Eureka 的区别

- Nacos 支持服务端主动检测提供者状态：临时实例采用心跳模式，非临时实例采用主动检测模式
- 临时实例心跳不正常会被剔除，非临时实例则不会被剔除
- Nacos 支持服务列表变更的消息推送模式，服务列表更新更及时
- Nacos 集群默认采用AP方式，当集群中存在非临时实例时，采用CP模式；而 Eureka 采用AP模式



## Nacos配置管理

### 统一配置管理

现在我们知道，微服务架构的基本原理如下

![微服务架构的基本原理](./images/微服务架构的基本原理.png)

但是随着业务的扩张，服务实例的数量也将增多，部署需要的服务器也将随之增加。此时我们就会遇到一些问题：

<span style="color:red;">我们需要对某个服务的配置文件做一些修改，而这个配置文件关联着数个服务实例，此时我们就不得不逐个实例的调整配置，这将会非常麻烦；</span>

<span style="color:red;">并且，在完成配置调整后我们还需要将这些服务实例逐个重启，这在生产环境下将会带来不小的影响。</span>

为了解决上述的问题，我们就产生了以下需求：

- **配置更改热更新**

那么微服务架构的基本原理就需要增加一些内容

![微服务架构的配置管理服务](./images/微服务架构的配置管理服务.png)

而如果我们同时采用 Nacos 作为配置管理的话，上图又可以进一步演化

![Nacos作为配置管理](./images/Nacos作为配置管理.png)



#### Nacos创建配置

①来到 Nacos 控制台的配置管理页面

![Nacos配置管理-创建配置](./images/Nacos配置管理-创建配置.png)

②创建一个新的配置（<span style="color:red;">注意：为了和前面的项目保持一致，我们选择在 stoneSpace 命名空间下创建</span>）

![Nacos配置管理-新建配置页](./images/Nacos配置管理-新建配置页.png)

③通过“发布”按钮发布配置

![Nacos配置管理-发布配置](./images/Nacos配置管理-发布配置.png)

④查看配置列表，可以看到我们刚刚新建的配置

![Nacos配置管理-查看配置](./images/Nacos配置管理-查看配置.png)



#### 微服务获取配置

微服务获取本地配置的流程：

![微服务获取配置的流程](./images/微服务获取配置的流程.png)

微服务要想从 Nacos 中获取配置，就必须要知道 Nacos 的地址。

但是我们现在的 Nacos 地址是配置在服务的本地配置文件中的，这也就意味着服务只有读取了配置才能知道 Nacos 地址，这就产生了矛盾。

因此我们不能把 Nacos 地址放在本地配置文件中，

<span style="color:red;">Spring 提供了一个 bootstrap.yaml 文件，它在项目启动时的优先级要比 application.yaml 文件高很多，所以会在项目启动后被优先读取。</span>

因此我们只需要把 Nacos 地址配置在 bootstrap.yaml 文件中即可。

（注意：与 Nacos 地址 和 配置文件 有关的所有信息都应该配置在 bootstrap.yaml 文件中）

![微服务从Nacos获取配置的流程](./images/微服务从Nacos获取配置的流程.png)

①在父工程中引入依赖，启用 bootstrap 包

```xml
<!--Spring Cloud 不再默认启用 bootstrap 包了，要想启用需要引入依赖-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-bootstrap</artifactId>
</dependency>
```

②在服务模块中引入 Nacos 的配置管理客户端依赖

```xml
<!--Nacos配置管理客户端-->
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
</dependency>
```

③在 UserService中的 resource 目录添加一个 <span style="color:red;">bootstrap.yml</span> 文件，这个文件是引导文件，优先级高于 application.yml

- 把 application.yml 中关于 Nacos 的所有配置移到 bootstrap.yml 中；

- <span style="color:red;">以下三个属性组合，指定了服务要从 Nacos 中获取的配置的 **DataID**</span>
    -  `spring.application.name`
    -  `spring.profiles.active`
    - `spring.cloud.nacos.config.file-extension` 

```yaml
spring:
  application:
    name: userservice # user服务的服务名称
  profiles:
    active: dev # 项目环境，这里是 dev
  cloud:
    nacos:
      server-addr: localhost:8848 # Nacos服务端地址
      username: nacos # Nacos开启权限验证后，需要设置登录用户名
      password: nacos # Nacos开启权限验证后，需要设置登录密码
      # 配置管理
      config:
        file-extension: yaml # 文件后缀名
        namespace: f20f0bdd-8b41-41a2-96c8-6aa946f903fc # 设置服务配置在Nacos中所属的命名空间ID
      # 服务发现
      discovery:
        namespace: f20f0bdd-8b41-41a2-96c8-6aa946f903fc # 设置服务在Nacos中所属的命名空间ID
        group: stone1 # 设置服务在Nacos中所属的分组
        cluster-name: BJ # 配置集群名称，也就是机房位置，例如：HZ 杭州
        ephemeral: false # 设置为非临时实例
```

④编写接口去验证我们是否真的从 Nacos 中获取到了配置

```java
package com.djn.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Name: UserController
 * Description: 用户Controller
 * 
 * @since 2023-11-22 15:27
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    ......

    /**
     * 通过 @Value 注解获取 application.yml 中的配置
     */
    @Value("${pattern.dateformat}")
    private String dateformat;

    /**
     * 此接口用于验证服务是否从 Nacos 中获取到了配置
     *
     * @return java.lang.String
     * @author SpringStone
     * @date 2023-12-24 16:55
     */
    @GetMapping("/now")
    public String now() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(dateformat));
    }

    ......
}
```

接口访问结果如下

![验证服务从Nacos获取配置](./images/验证服务从Nacos获取配置.png)



### 配置热更新

通过两种配置方式可以实现 Nacos 配置的热更新：Nacos 中的配置文件变更后，微服务无需重启就可以感知。

#### 方式一：@RefreshScope

在 @Value 注入的变量所在类上添加注解 @RefreshScope

```java
package com.djn.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Name: UserController
 * Description: 用户Controller
 * 
 * @since 2023-11-22 15:27
 */
@Slf4j
@RestController
@RequestMapping("/user")
@RefreshScope
public class UserController {

    ......

    /**
     * 通过 @Value 注解获取 application.yml 中的配置
     */
    @Value("${pattern.dateformat}")
    private String dateformat;

    /**
     * 此接口用于验证服务是否从 Nacos 中获取到了配置
     *
     * @return java.lang.String
     * @author SpringStone
     * @date 2023-12-24 16:55
     */
    @GetMapping("/now")
    public String now() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(dateformat));
    }

    ......
}
```

修改 Nacos 中的配置后，我们可以在项目运行控制台中看到如下内容

![配置热更新后服务运行的控制台](./images/配置热更新后服务运行的控制台.png)



#### 方式二：@ConfigurationProperties

将配置属性注入到自定义的属性类中，即可实现这些配置的热更新

```java
package com.djn.user.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "pattern")
public class PatternProperties {
    private String dateformat;
}
```

随后修改 UserController 中的代码

```java
    @Resource
    private PatternProperties properties;

    /**
     * 此接口用于验证服务是否从 Nacos 中获取到了配置
     *
     * @return java.lang.String
     * @author SpringStone
     * @date 2023-12-24 16:55
     */
    @GetMapping("/now")
    public String now() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(properties.getDateformat()));
    }
```



#### 总结

Nacos 配置更改后，微服务可以实现热更新，方式：

①通过 @Value 注解注入，结合 @RefreshScope 来刷新

②通过 @ConfigurationProperties 注入，自动刷新（**推荐**！！！）

注意事项：

- 不是所有的配置都适合放到配置中心的，维护起来比较麻烦
- 建议将一些关键参数，需要运行时调整到的参数放到 Nacos 配置中心，一般的都是自定义配置



### 多环境配置共享

> <span style="color:#1bd1a5;">多环境配置共享的实际应用场景</span>
>
> 有一些配置属性在开发、测试和生产等环境下的值是一样的。
>
> 如果把它们在每个环境的配置文件中都写一遍是比较浪费的，而且将来如果配置发生改动，还需要逐个更改每个配置文件。
>
> 于是就产生了多环境配置共享的需求：<span style="color:red;">无论环境怎么变，这个配置都能够被加载。</span>

微服务在启动时会从 Nacos 读取多个配置文件：

- [spring.application.name]-[spring.profiles.active].yaml，例如：userservice-dev.yaml
- [spring.application.name].yaml，例如：userservice.yaml

<span style="color:blue;">无论 profile 如何变化，[spring.application.name].yaml 这个文件一定会被加载，因此多环境共享配置可以写入这个文件</span>

①在 Nacos 中创建多环境共享配置文件

![创建UserService的多环境共享配置](./images/创建UserService的多环境共享配置.png)

可以看到配置列表中有两条配置记录

![配置管理列表中有两条配置记录](./images/配置管理列表中有两条配置记录.png)

②在 PatternProperties 类中添加 envSharedValue 字段，并编写一个请求接口返回 PatternProperties 对象

![PatternProperties类中添加envSharedValue字段](./images/PatternProperties类中添加envSharedValue字段.png)

```java
@Resource
private PatternProperties properties;

@GetMapping("/prop")
public PatternProperties getProperties() {
    return properties;
}
```

③至此我们就可以启动服务实例，访问接口，验证结果了

我们首先将一个服务实例在 `dev` 环境启动，然后将另一个的启动环境修改为 `test` 启动。

修改环境的方式有两种，一种是修改 `spring.profiles.active` 属性值，还有一种等效的修改方式如下：

![修改服务实例的启动环境](./images/修改服务实例的启动环境.png)

分别访问两个服务实例下的 /user/prop 接口，可以看到两个服务实例都成功读取到了共享配置

![不同环境下的服务实例的接口返回结果](./images/不同环境下的服务实例的接口返回结果.png)

另外，从两个服务实例的启动控制台中也可以看出，它们读取了不同的配置

dev 环境下：

![dev环境服务实例启动时的控制台输出](./images/dev环境服务实例启动时的控制台输出.png)

test 环境下：

![test环境服务实例启动时的控制台输出.png](./images/test环境服务实例启动时的控制台输出.png)



#### 多配置的优先级

既然一个服务实例在启动时会读取多个配置文件，那么这些配置文件的优先级又是怎样的呢？

假设在一个服务实例的多个配置文件中都存在一个相同的属性，但是它的值不同，那么服务实例最终读取到的值将是哪一个呢？

为了弄清这个问题，我们分别在本地配置、共享配置 和 dev环境配置 中添加如下属性，<span style="color:red;">并在 PatternProperties 类中添加相应字段</span>

```yaml
pattern:
  name: {环境名字}
```

此时启动服务实例，访问接口得到的返回如下

![环境配置优先级验证结果1](./images/环境配置优先级验证结果1.png)

当我们把 dev环境配置 中的属性移除后，再启动服务实例，访问接口得到的返回如下



![环境配置优先级验证结果2](./images/环境配置优先级验证结果2.png)

最后，我们再把 共享配置 中的属性移除后，再启动服务实例，访问接口得到的返回如下

![环境配置优先级验证结果3.png](./images/环境配置优先级验证结果3.png)

由此可知，多配置的优先级为：<span style="color:#f20c00">服务名-profile.yaml</span> > <span style="color:#0c8918">服务名称.yaml</span> > <span style="color:#4b5cc4">本地配置</span> 

![多环境配置的优先级图解](./images/多环境配置的优先级图解.png)



#### 总结

微服务会从 Nacos 读取的配置文件：

① [服务名]-[spring.profile.active].yaml ，环境配置

② [服务名].yaml ，默认配置，多环境共享

优先级：

① [服务名]-[环境].yaml > [服务名].yaml > 本地配置



### 搭建Nacos集群

在企业生产环境中，更强调<span style="color:#ff8c31;">**高可用**</span>，所以 Nacos 一定要做成<span style="color:red;">集群</span>。

[官方文档](https://nacos.io/zh-cn/docs/v2/guide/admin/cluster-mode-quick-start.html)

![官方Nacos集群图](./images/官方Nacos集群图.jpg)

上面是 Nacos 官方给出的集群架构图，我们将其简化一下

![简化的Nacos集群架构图](./images/简化的Nacos集群架构图.png)

图中设置了3台 Nacos 节点，为了保证它们的数据共享又设置了一个<span style="color:#0aa344;">MySQL集群</span>；而后采用 <span style="color:#00e500;">Nginx</span> 负责做请求的负载均衡。

在接下来的案例中，出于设备限制，所以会将 Nacos 节点都部署在一台设备上，MySQL集群也采用 单点 代替。



#### 搭建案例

> 基本步骤：
>
> 1.搭建数据库，初始化数据库表结构
>
> 2.下载 Nacos 安装包
>
> 3.配置 Nacos
>
> 4.启动 Nacos 集群
>
> 5.Nginx 反向代理

##### 1.初始化数据库

Nacos 默认数据存储在<span style="color:red;">内嵌数据库Derby</span>中，不属于生产可用的数据库。官方推荐的最佳实践是使用<span style="color:#0aa344;">带有主从的高可用数据库集群</span>。

这里以单点的MySQL数据库来代替讲解。

首先新建一个数据库，命名为 nacos ，而后导入我们下载的 Nacos 包中提供的sql脚本 **mysql-schema.sql** （文件所在目录为 `conf`）。

![Nacos提供的mysql脚本](./images/Nacos提供的mysql脚本.png)

##### 2.配置Nacos

将 Nacos 的 `conf` 目录下的 **cluster.conf.example** 文件重命名为 **cluster.conf** ，然后添加以下内容

<span style="color:red;">这里的IP地址不建议写127.0.0.1或localhost！！！</span>

```
# 如果是Nacos2.x版本，集群的端口不建议设为连续的端口号
192.168.0.106:8845
192.168.0.106:8855
192.168.0.106:8864
```



然后修改 application.properties 文件，添加数据库配置

![Nacos配置文件中关于数据库的配置](./images/Nacos配置文件中关于数据库的配置.png)

```properties
#*************** Config Module Related Configurations ***************#
### If use MySQL as datasource:
### Deprecated configuration property, it is recommended to use `spring.sql.init.platform` replaced.
# spring.datasource.platform=mysql
spring.sql.init.platform=mysql

### Count of DB:
db.num=1

### Connect URL of DB:
db.url.0=jdbc:mysql://localhost:3306/nacos?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true&useUnicode=true&useSSL=false&serverTimezone=Asia/Shanghai
db.user.0=root
db.password.0=1234
```

<span style="color:red;">注意：这里的mysql连接url的参数如果出现问题，那么在启动时也可能会出现“No DataSource Set”的报错。比如serverTimezone的时区值设置。</span>



##### 3.启动Nacos

将 Nacos 目录复制三份，分别命名为：nacos1、nacos2、nacos3

![将Nacos目录复制多个](./images/将Nacos目录复制多个.png)

然后分别修改三个文件夹中的 **application.properties** ，

nacos1：

```properties
server.port=8845
```

nacos2：

```properties
server.port=8855
```

nacos2：

```properties
server.port=8865
```

接着分别启动三个 Nacos 节点

```shell
startup.cmd
```



##### 4.Nginx反向代理

下载 Nginx 安装包，解压到任意非中文目录下，

![nginx目录](./images/nginx目录.png)

修改 `conf/nginx.conf` 文件，配置如下

```nginx
http {
    upstream nacos-cluster {
        server 127.0.0.1:8845;
        server 127.0.0.1:8855;
        server 127.0.0.1:8865;
    }

    server {
        listen		8000;
        server_name	localhost;

        location /nacos {
            proxy_pass http://nacos-cluster;
        }
    }
}

# stream快用于做TCP转发（Nacos2.x版本需要添加下面的配置）
stream {
    upstream nacos-cluster {
        server 127.0.0.1:9845;
        server 127.0.0.1:9855;
        server 127.0.0.1:9865;
    }
    
    server {
        listen 9000;
        proxy_connect_timeout 20s;
        proxy_timeout 5m;
        proxy_pass nacos-cluster;
    }
}
```

==~将以上内容粘贴到http模块中任意一处即可。~==

保存更改的配置后，进入到 nginx 目录下用命令行启动即可

```
start nginx.exe
```

然后打开浏览器访问 http://localhost:8000/nacos

![通过nginx负载均衡访问nacos集群](./images/通过nginx负载均衡访问nacos集群.png)

<h3 style="color:red;">到这里，我们的 Nacos 集群就搭建成功了！</h3>



##### 5.修改项目中的Nacos地址

修改 **UserService** 的 **bootstrap.yaml** 文件，让其注册到 Nacos 集群中

```yaml
spring:
  application:
    name: userservice # user服务的服务名称
  profiles:
    active: dev # 项目环境，这里是 dev
  cloud:
    nacos:
      server-addr: localhost:8000 # Nacos集群地址
      username: nacos # Nacos开启权限验证后，需要设置登录用户名
      password: nacos # Nacos开启权限验证后，需要设置登录密码
      config:
        file-extension: yaml # 文件后缀名
```

随后启动项目，然后我们就可以在Nacos集群的控制台中看到注册进来的服务

![注册到Nacos集群的服务](./images/注册到Nacos集群的服务.png)

然后我们再试试在Nacos集群控制台中添加一条配置 userservice-dev.yaml

![在Nacos集群控制台中添加一条配置](./images/在Nacos集群控制台中添加一条配置.png)

添加成功以后我们可以在 MySQL 数据库中的 **config_info** 表中看到这条记录

![在MySQL中查看添加的配置记录](./images/在MySQL中查看添加的配置记录.png)

我们可以尝试去访问一下接口 http://localhost:8864/user/prop，看看服务是否能够读取到配置

![服务读取Nacos集群中的配置](./images/服务读取Nacos集群中的配置.png)



##### 6.Nacos2.x的注意点

[参考文章](https://blog.csdn.net/weixin_55658418/article/details/131696615)

Nacos2.x最主要的变化就是<span style="color:red;">**新增了几个端口**</span>

一个是Nacos主端口+1000（比如9848），该端口用于<span style="color:red;">**Nacos客户端gRPC请求服务端**</span>，

另外两个分别是主端口+1001和-1000，都是用于<span style="color:red;">**服务端之间的请求**</span>。

> 详细参考官方文档：[Nacos 2.0.0 兼容性文档](https://nacos.io/zh-cn/docs/v2/upgrading/2.0.0-compatibility.html)

<h3>需要注意的点</h3>

根据上面的点，总共要注意这么几点

- Nacos服务端部署时，端口至少大于1000
- Nacos集群部署时，假设是在同一网段内，端口不能连续
- 如果是要做 VIP/Nginx 的话，Nginx还要额外监听源端口+1000，因为客户端还需要源端口+1000用于与服务端进行gRPC请求。

==VIP：Virtual IP Address==



## Feign远程调用

### Feign替代RestTemplate

<span style="color:red;">RestTemplate方式调用存在的问题</span>

先来看我们以前利用RestTemplate发起远程调用的代码

```java
String url = "http://userservice/user/" + order.getUserId();
User user = restTemplate.getForObject(url, User.class);
```

存在下面的问题：

- 代码可读性差，编程体验不统一
- 参数复杂的URL难以维护



#### Feign的介绍

Feign是一个声明式的http客户端，官方地址：https://github.com/OpenFeign/feign/

其作用就是帮助我们<span style="color:#1bd1a5;">优雅</span>的实现http请求的发送，解决RestTemplate存在的问题。

![Feign官方简介](./images/Feign官方简介.png)



#### 定义和使用Feign客户端

使用Feign的步骤如下

##### 1.引入依赖

```xml
<!--Feign-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```

##### 2.开启注解

在OrderService的启动类添加注解开启Feign的功能

```java
@EnableFeignClients
@MapperScan("com.djn.order.mapper")
@SpringBootApplication
public class OrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }
}
```

##### 3.编写Feign客户端

主要是基于SpringMVC的注解来声明远程调用的信息，比如：

- 服务名称：userservice
- 请求方式：GET
- 请求路径：/user/{id}
- 请求参数：Long id
- 返回值类型：User

```java
package com.djn.order.clients;

import com.djn.order.domain.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * UserService的请求客户端
 */
@FeignClient("userservice")
public interface UserClient {

    @GetMapping("/user/{id}")
    User findById(@PathVariable("id") Long id);
}
```

##### 4.注入并调用Feign客户端

```java
@Resource
private UserClient userClient;

@Override
public Order queryOrderById(Long orderId) {
    //1.查询订单
    Order order = orderMapper.findById(orderId);
    //2.用 Feign 发起远程调用
    User user = userClient.findById(order.getUserId());
    //3.封装User到Order
    order.setUser(user);
    //4.返回
    return order;
}
```

##### 5.启动服务并调用接口

可以看到 Feign <span style="color:red;">不仅实现了远程调用，而且还实现了负载均衡</span>。

![利用Feign发起远程调用](./images/利用Feign发起远程调用.png)

两个UserService实例都被远程调用了

![Feign实现了负载均衡](./images/Feign实现了负载均衡.png)

我们点进Feign的依赖文件就可以看到 Feign引入了 SpringCloud LoadBalancer

![Feign引入了SpringCloudLoadBalancer](./images/Feign引入了SpringCloudLoadBalancer.png)



### 自定义配置

Feign 可以通过自定义配置来覆盖默认配置，可以修改的配置如下：

![Feign的可配置属性](./images/Feign的可配置属性.png)

日志级别说明

- NONE：没有任何日志（默认）
- BASIC：当Feign发起一次HTTP请求时，日志会记录请求的起止时间、耗时时长等一些基本信息
- HEADERS：除了请求的基本信息以外，日志还会记录请求头和响应头的信息
- FULL：除了请求基本信息、头信息以外，日志还会记录请求体和响应体的信息，是最完整的日志记录



#### 配置Feign日志的两种方式

方式一：配置文件方式

①全局生效

```yaml
feign:
  client:
    config:
      default: # 这里用 default 就是全局配置，如果是写服务名称，则是针对某个微服务的配置
        loggerLevel: FULL # 日志级别
```

②局部生效

```yaml
feign:
  client:
    config:
      userservice: # 这里用 default 就是全局配置，如果是写服务名称，则是针对某个微服务的配置
        loggerLevel: FULL # 日志级别
```

![Full级别的Feign日志](./images/Full级别的Feign日志.png)



方式二：Java代码方式，需要先声明一个Bean

```java
public class FeignClientConfiguration {
    @Bean
    public Logger.Level feignLogLevel() {
        return Logger.Level.BASIC;
    }
}
```

①全局配置

```java
@EnableFeignClients(defaultConfiguration = FeignClientConfiguration.class)
```

②局部配置

```java
@FeignClient(value = "userservice", configuration = DefaultFeignConfiguration.class)
```



### Feign使用优化

Feign底层的客户端实现

- URLConnection：默认实现，不支持连接池
- Apache HttpClient：支持连接池（优化选择一）
- OKHttp：支持连接池（优化选择二）



因此优化Feign的性能主要包括：

①使用连接池代替默认的URLConnection

②日志级别，最好用 basic 或 none



#### 连接池配置

Feign 添加 HttpClient 的支持

##### ①引入依赖

```xml
<!--Feign性能优化：HttpClient（支持连接池）-->
<dependency>
    <groupId>io.github.openfeign</groupId>
    <artifactId>feign-httpclient</artifactId>
</dependency>
```

##### ②配置连接池

```yaml
feign:
  httpclient:
    enabled: true # 开启 Feign 对 HttpClient 的支持
    max-connections: 200 # 最大的连接数
    max-connections-per-route: 50 # 每个路径的最大连接数
```



### 最佳实践

==企业在使用 Feign 的过程中，各种踩坑，最后总结出了一套相对比较好的实用方式。==

#### 方式一：继承

<span style="color:red;">给消费者的 FeignClient 和提供者的 Controller 定义统一的父接口作为标准。</span>

- 会造成服务紧耦合
- 父接口参数列表中的映射不会被继承

![Feign最佳实践一：继承](./images/Feign最佳实践一：继承.png)



#### 方式二：抽取

<span style="color:red;">将 FeignClient 抽取为独立的模块，并且把接口有关的POJO、默认的Feign配置都放到这个模块中，提供给所有消费者使用。</span>

- 有些服务可能只需要用到部分方法，却不得不将所有方法都引入进来，这显然有些多余

![Feign最佳实践二：抽取](./images/Feign最佳实践二：抽取.png)

**实现步骤如下：**

1.首先创建一个 module ，命名为 feign-api ，然后引入 feign 的 starter 依赖

2.将  OrderService 中编写的 UserClient、User、DefaultFeignConfiguration 都复制到 feign-api 项目中

3.在 OrderService 中引入 feign-api 依赖

4.修改 OrderService 中的所有与上述三个组件有关的 import 部分，改成导入 feign-api 中的包

5.重启测试

<span style="color:#f20c00;">**当定义的 FeignClient 不在 SpringBootApplication 的扫描包范围时，这些 FeignClient 无法使用。有两种方式解决：**</span>

方式一：指定 FeignClient 所在包

```java
@EnableFeignClients(basePackages = "com.djn.feign.clients")
```

方式二：指定 FeignClient 字节码

```java
@EnableFeignClients(clients = {UserClient.class})
```



## Gateway服务网关

为什么需要网关

网关功能

- 身份认证和权限校验
- 服务路由、负载均衡
- 请求限流

![网关的功能](./images/网关的功能.png)

在 Spring Cloud 中网关的实现包括两种：

- gateway
- zuul

Zuul 是基于 Servlet 的实现，属于阻塞式编程；而 SpringCloud Gateway 则是基于 Spring5 中提供的 WebFlux ，属于响应式编程的实现，具备更好的性能。



### Gateway快速入门

#### 搭建网关服务

1.创建新的 Module ，引入 SpringCloud Gateway 的依赖 和 Nacos 的服务发现依赖

```xml
<dependencies>
    <!--Nacos服务注册发现依赖-->
    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
    </dependency>
    <!--网关Gateway依赖-->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-gateway</artifactId>
    </dependency>
    <!--
        SpringCloud2021弃用了Ribbon，
        因此Alibaba在2021版本Nacos中删除了Ribbon的jar包，
        因此无法通过lb路由指定到微服务，会出现503的情况，
        所以需要导入下面的依赖
    -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-loadbalancer</artifactId>
    </dependency>
</dependencies>
```

2.编写路由配置及 Nacos 地址，创建 application.yml 文件，并添加以下配置

```yaml
server:
  port: 10010 # 网关端口
spring:
  application:
    name: gateway # 服务名称
  cloud:
    nacos:
      server-addr: localhost:8848 # Nacos服务端地址
      username: nacos
      password: nacos
      discovery:
        namespace: 17b272d4-a486-40b1-a5a5-01503b438645 # 设置服务在Nacos中所属的命名空间ID
        group: stone1 # 设置服务在Nacos中所属的分组
        cluster-name: BJ # 配置集群名称，也就是机房位置，例如：HZ 杭州
    gateway:
      routes: # 网关路由配置
        - id: user-service # 路由id，自定义，只要唯一即可
          # uri: http://127.0.0.1:8081 # 路由的目标地址 http就是固定地址
          uri: lb://userService # 路由的目标地址 lb就是负载均衡（LoadBalancer）,后面跟服务名称
          predicates: # 路由断言，也就是判断“请求是否符合路由规则”的条件
            - Path=/user/** # 这个是按照路径匹配，只要以 /user/ 开头就符合要求
        - id: order-service
          uri: lb://orderService
          predicates:
            - Path=/order/**
```

3.创建启动类 `GatewayApplication.java`

```java
package com.djn.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}
```

4.随后启动服务和网关，并尝试通过网关去访问服务接口

http://localhost:10010/user/1

http://localhost:10010/order/1



网关工作流程

![网关的工作流程](./images/网关的工作流程.png)



#### 总结

网关搭建步骤：

1.创建项目，引入 Nacos 服务发现 和 gateway 依赖，如果是 SpringCloud 2020 以后的版本还需要引入 loadbalancer 依赖

2.配置 application.yml ，包括服务基本信息、Nacos地址、路由

路由配置包括：

1.路由id：路由的唯一标识

2.路由目标（uri）：路由的目标地址，http代表固定地址，lb代表根据服务名负载均衡

3.路由断言（predicates）：判断路由的规则

<span style="color:red;">4.路由过滤器（filters）：对请求或响应做处理</span>



### 断言工厂

路由断言工厂：Route Predicate Factory

- 我们在配置文件中写的断言规则只是字符串，这些字符串会被 Predicate Factory 读取并处理，转变为路由判断的条件
- 例如 Path=/user/** 是按照路径匹配，这个规则是由 `org.springframework.cloud.gateway.handler.predicate.PathRoutePredicateFactory` 类来处理的
- 像这样的断言工厂在 SpringCloud Gateway 中还有十几个

![Spring提供的断言工厂](./images/Spring提供的断言工厂.png)

[官方参考文档](https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#gateway-request-predicates-factories)

![断言工厂的官方文档](./images/断言工厂的官方文档.png)



#### 案例

例如，我们尝试在 OrderService 路由下加上 **After** 断言

```yaml
- id: order-service
  uri: lb://orderService
  predicates:
  - Path=/order/**
  - After=2037-01-20T17:42:47.789-07:00[America/Denver] # 表示只允许2037年1月20日 17:42:47之后访问的请求通过
```

随后重启网关服务，并尝试访问 http://localhost:10010/order/1，会发现访问结果 404

![添加After断言后访问服务网关](./images/添加After断言后访问服务网关.png)



### 过滤器工厂

GatewayFilter 是网关中提供的一种过滤器，可以对进入网关的请求和微服务返回的响应做处理：

![GatewayFilter](./images/GatewayFilter.png)

[官方参考文档](https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#gatewayfilter-factories)

![Spring提供的过滤器工厂](./images/Spring提供的过滤器工厂.png)

在路由断言工厂下面，就是过滤器工厂

![过滤器工厂的官方文档.png](./images/过滤器工厂的官方文档.png)



#### 案例

给所有进入 UserService 的请求添加一个请求头：Truth = Spring Stone is My Nick Name

实现方式：

首先，在 Gateway 中修改 application.yml 文件，给 UserService 的路由添加路由过滤器

```yaml
spring:
  cloud:
    gateway:
      routes: # 网关路由配置
        - id: user-service
          uri: lb://userService
          predicates:
            - Path=/user/**
          filters: # 过滤器
            - AddRequestHeader=Truth, Spring Stone is My Nick Name
```

然后，我们修改 UserService 的代码，去接收一下请求头参数

```java
@GetMapping("/{id}")
public User queryById(@PathVariable("id") Long id, @RequestHeader(value = "Truth", required = false) String truth) {
    System.out.println("truth: " + truth);
    return userService.queryById(id);
}
```

随后重启 UserService 服务和服务网关 Gateway，访问 http://localhost:10010/user/1，并查看 UserService 的控制台输出

![Gateway过滤器工厂案例控制台输出](./images/Gateway过滤器工厂案例控制台输出.png)

==思考：如何给所有的微服务请求都设置一个如上的请求头？==

<span style="color:red;">如果要对所有的路由都生效，则可以将过滤器工厂写到 default 下。</span>

```yaml
spring:
  cloud:
    gateway:
      routes: # 网关路由配置
        - id: user-service
          uri: lb://userService
          predicates:
            - Path=/user/**
        - id: order-service
          uri: lb://orderService
          predicates:
            - Path=/order/**
      default-filters: # 默认过滤器，会对所有的路由请求都生效
        - AddRequestHeader=Truth, Spring Stone is My Nick Name # 添加请求头
```



### 全局过滤器

全局过滤器的作用也是处理一切进入网关的请求和微服务响应，与**GatewayFilter**的作用一样。

区别在于GatewayFilter通过配置定义，处理逻辑是固定的；而GlobalFilter的逻辑需要自己写代码实现。

定义方式就是实现**GlobalFilter**接口。

![Gateway提供的GlobalFilter接口](./images/Gateway提供的GlobalFilter接口.png)



#### 案例

需求：定义全局过滤器，拦截请求，判断请求的参数是否满足下面条件

- 参数中是否有 authorization
- authorization 参数值是否为 admin

如果同时满足则放行，否则进行拦截

实现代码如下

```java
package com.djn.gateway.filters;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

// @Order 注解的作用：定义过滤器的执行顺序，设置的数字越小，过滤器的优先级越高，执行顺序也就越靠前
// @Order(-1)
@Component
public class AuthorizeFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1.获取请求参数
        ServerHttpRequest request = exchange.getRequest();
        MultiValueMap<String, String> params = request.getQueryParams();
        // 2.获取参数中的 authorization 参数
        String auth = params.getFirst("authorization");
        // 3.判断参数值是否等于 admin
        if ("admin".equals(auth)) {
            // 4.是，放行
            return chain.filter(exchange);
        }
        // 5.否，拦截
        // 5.1.设置状态码
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        // 5.2.拦截请求
        return exchange.getResponse().setComplete();
    }

    /*
    通过实现Ordered接口的getOrder()方法，可以达到与@Order注解相同的效果
     */
    @Override
    public int getOrder() {
        return -1;
    }
}
```

重启服务网关，并带 authorization 参数访问 http://localhost:10010/user/1?authorization=admin

![添加全局过滤器后的访问结果](./images/添加全局过滤器后的访问结果.png)

#### 过滤器执行顺序

请求进入网关会碰到三类过滤器：当前路由的过滤器、DefaultFilter、GlobalFilter。

请求路由后，会将当前路由过滤器和DefaultFilter、GlobalFilter，合并到一个过滤器链（集合）中，排序后依次执行每个过滤器。

![过滤器执行顺序](./images/过滤器执行顺序.png)



### 跨域问题

跨域：域名不一致就是跨域，主要包括

- 域名不同：www.taobao.com 和 www.taobao.org 和 www.jd.com 和 miaosha.jd.com
- 域名相同，端口不同：localhost:8080 和 localhost:8081

<span style="color:red;">跨域问题：浏览器禁止请求的发起者与服务端发生跨域Ajax请求，请求被浏览器拦截的问题</span>

解决方案：CORS

网关处理跨域问题采用的同样是CORS方案，并且只需要简单配置即可实现

```yaml
spring:
  cloud:
    gateway:
      #...
      globalcors: # 全局的跨域处理
        add-to-simple-url-handler-mapping: true # 解决 options 请求被网关拦截的问题
        cors-configurations:
          '[/**]':
            allowedOrigins: # 允许哪些网站的跨域请求
              - "http://localhost:8090"
              - "http://www.leyou.com"
            allowedMethods: # 允许的跨域Ajax的请求方式
              - "GET"
              - "POST"
              - "DELETE"
              - "PUT"
              - "OPTIONS"
            allowedHeaders: "*" # 允许在请求中携带的头信息
            allowedCredentials: true # 是否允许携带Cookie
            maxAge: 360000 # 这次跨域检测的有效期
```

我们尝试去用一个简单的页面来模拟一下跨域请求的情景，以此来验证我们的配置是否能够生效

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>
<body>
    <pre>
        spring:
            cloud:
                gateway:
                #...
                globalcors: # 全局的跨域处理
                    add-to-simple-url-handler-mapping: true # 解决 options 请求被网关拦截的问题
                    cors-configurations:
                    '[/**]':
                        allowedOrigins: # 允许哪些网站的跨域请求
                        - "http://localhost:8090"
                        - "http://www.leyou.com"
                        allowedMethods: # 允许的跨域Ajax的请求方式
                        - "GET"
                        - "POST"
                        - "DELETE"
                        - "PUT"
                        - "OPTIONS"
                        allowedHeaders: "*" # 允许在请求中携带的头信息
                        allowedCredentials: true # 是否允许携带Cookie
                        maxAge: 360000 # 这次跨域检测的有效期
    </pre>
</body>
<script src="https://unpkg.com/axios/dist/axios.min.js"></script>
<script>
    axios.get("http://localhost:10010/user/1?authorization=admin")
    .then(resp => console.log(resp.data))
    .catch(err => console.log(err))
</script>
</html>
```

我们尝试在VSCode中利用 **live-server** 将这个页面在 localhost:8090 地址运行

![模拟跨域请求的前端页面](./images/模拟跨域请求的前端页面.png)

当我们没有配置跨域处理时，页面的访问结果是这样的

![无跨域处理的页面访问结果](./images/无跨域处理的页面访问结果.png)

当我们配置了跨域处理后，页面的访问结果是这样的，可以看出我们的跨域处理配置确实生效了

![有跨域处理的页面访问结果](./images/有跨域处理的页面访问结果.png)



## Docker

<h4>项目部署的问题</h4>

大型项目组件较多，运行环境也较为复杂，部署时会遇到一些问题：

- 依赖关系复杂，容易出现兼容性问题（项目部署时需要安装各种运行环境）
- 开发、测试、生产环境有差异

![项目部署时需要安装各种运行环境](./images/项目部署时需要安装各种运行环境.png)

<span style="color:red;">Docker如何解决依赖的兼容问题？</span>

- 将应用的Libs（函数库）、Deps（依赖）、配置与应用一起打包
- 将每个应用放到一个隔离**容器**去运行，避免互相干扰

![Docker解决依赖兼容问题](./images/Docker解决依赖兼容问题.png)

<span style="color:red;">Docker如何解决“不同环境的操作系统不同”的问题？</span>

操作系统的结构：所有的**Linux内核操作系统**都可以被分为两层

- 内核 与硬件交互，提供操作硬件的指令
- 系统应用 封装内核指令为函数，便于程序员调用，用户程序基于系统函数库实现功能



![操作系统的结构](./images/操作系统的结构.png)

不同版本的Linux操作系统都是基于Linux内核，只是系统应用不同，提供的函数库有差异（以Ubuntu和CentOS为例）

![不同版本的Linux操作系统的区别](./images/不同版本的Linux操作系统的区别.png)

<span style="color:#f20c00;">**Docker将用户程序与所需要调用的系统（如Ubuntu）函数库一起打包<br>Docker运行到不同操作系统时，直接基于打包的库函数，借助于操作系统的Linux内核来运行**</span>

![Docker在不同的Linux系统上运行的原理](./images/Docker在不同的Linux系统上运行的原理.png)

<h4>总结</h4>

Docker如何解决大型项目依赖关系复杂，不同组件依赖的兼容性问题？

- Docker允许开发中将应用、依赖、函数库、配置一起<span style="color:red;">**打包**</span>，形成可移植镜像
- Docker应用运行在容器中，使用沙箱机制，相互<span style="color:red;">**隔离**</span>

Docker如何解决开发、测试、生产环境有差异的问题

- Docker镜像中包含完整运行环境，包括系统函数库，仅依赖系统的Linux内核，因此可以在任意Linux操作系统上运行



### 什么是Docker

<span style="color:#2697ed;">**Docker是一个快速交付应用、运行应用的技术**</span>

1、可以将程序及其依赖、运行环境一起打包为一个镜像，可以迁移到任意Linux操作系统

2、运行时利用沙箱机制形成隔离容器，各个应用互不干扰

3、启动、移除都可以通过一行命令完成，方便快捷



<h4>Docker和虚拟机的区别</h4>

虚拟机（Virtual Machine）是在操作系统中模拟硬件设备，然后运行另一个操作系统，

比如在 Windows 系统里面运行 Ubuntu 系统，这样就可以运行任意的 Ubuntu 应用了。

![Docker与虚拟机](./images/Docker与虚拟机.png)

Docker和虚拟机的差异：

- Docker是一个系统进程；虚拟机是在操作系统中的另一个操作系统
- Docker体积小、启动速度快、性能好；虚拟机体积大、启动速度慢、性能一般



<h4>Docker架构</h4>

**镜像（Image）**：Docker将应用程序及其所需的依赖、函数库、环境、配置等文件打包在一起，称为<span style="color:red;">镜像</span>。

==镜像都是只读的！==

![举例-MySQL镜像](./images/举例-MySQL镜像.png)

**容器（Container）**：镜像中的应用程序运行后形成的进程就是<span style="color:red;">容器</span>，只是Docker会给容器做隔离，对外不可见。

==容器只能将镜像中的文件拷贝下来，进行写操作。==

![举例-MySQL容器.png](./images/举例-MySQL容器.png)

**DockerHub**：一个Docker镜像的托管平台。这样的平台称为Docker Registry。

国内也有类似于 DockerHub 的公开服务，比如 网易云镜像 、阿里云镜像 等。

![DockerHub](./images/DockerHub.png)

**Docker架构**

Docker是一个CS架构的程序，由两部分组成：

- 服务端（Server）：Docker守护进程，负责处理Docker指令，管理镜像、容器等
- 客户端（Client）：通过命令或RestAPI向Docker服务端发送指令。可以在本地或远程向服务端发送指令。

![Docker架构图](./images/Docker架构图.png)



### 安装Docker

Docker 分为 CE（社区版） 和 EE（企业版） 两大版本。Docker CE 支持 64 位版本 CentOS 7，<span style="color:red;">并且要求内核版本不低于 3.10，CentOS满足最低内核的要求</span>。

#### 卸载Docker

如果之前安装过旧版本的Docker，可以使用下面的命令卸载

```sh
yum remove docker \
				  docker-client \
				  docker-client-latest \
				  docker-common \
				  docker-latest \
				  docker-latest-logrotate \
				  docker-logrotate \
				  docker-selinux \
				  docker-engine-selinux \
				  docker-engine \
				  docker-ce
```

<span style="color:green;">其中 `\` 表示命令的拼接，当我们需要将一条命令换行时可以使用它。</span>



#### 安装Docker

首先需要将虚拟机联网，安装 yum 工具

```sh
yum install -y yum-utils \
		   device-mapper-persistent-data \
		   lvm2 --skip-broken
```

然后更新本地镜像源

```sh
yum-config-manager \
	--add-repo \
	https://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo

sed -i 's/download.docker.com/mirrors.aliyun.com\/docker-ce/g' /etc/yum.repos.d/docker-ce.repo

yum makecache fast
```

然后输入命令

```sh
yum install -y docker-ce
```

docker-ce为社区免费版本。稍等片刻，Docker即可安装成功。

通过命令查看Docker版本来验证Docker是否安装成功

```shell
docker -v
```

![Linux命令查看安装的Docker版本](./images/Linux命令查看安装的Docker版本.png)



#### 启动Docker

Docker应用需要用到各种端口，逐一去修改防火墙设置非常麻烦。因此，建议直接关闭防火墙！（<span style="color:red;">当然，在企业实际应用中禁止这样做！！！</span>）

<span style="color:green;">**关闭Linux的防火墙**</span>

```shell
# 关闭
systemctl stop firewalld
# 禁止开启启动防火墙
systemctl disable firewalld
```

通过以下命令启动Docker

```shell
# 启动Docker服务
systemctl start docker

# 查看Docker服务是否启动成功
systemctl status docker

# 停止Docker服务
systemctl stop docker

# 重启Docker服务
systemctl restart docker
```



#### 配置镜像

Docker官方镜像仓库网速较差，我们需要设置国内镜像：

参考阿里云的镜像加速文档：http://cr.console.aliyun.com/cn-hangzhou/instances/mirrros

![阿里云镜像加速文档](./images/阿里云镜像加速文档.png)



### Docker的基本操作

#### 镜像操作

镜像名称一般分两部分组成：`[repository]:[tag]`，例如：

![镜像名称](./images/镜像名称.png)

<span style="color:red;">所以 mysql:5.7 和 mysql:5.6 是两个不同的镜像。</span>

<span style="color:blue;">在没有指定 tag 时，默认是 latest ，代表最新版本的镜像。</span>

1.获取镜像

- 从本地获取：通过 `docker build` 命令利用本地 Dockerfile 文件构建镜像

- 从Docker Registry拉取：通过 `docker pull` 命令从镜像服务器中拉取镜像

2.查看镜像：`docker images`

3.删除镜像：`docker rmi [镜像名称]`

4.分享镜像：

- 推送镜像到镜像服务器，供别人下载：`docker push`
- 通过移动存储设备（如U盘）拷贝给别人：`docker save` ==> 镜像压缩包tar文件 ==> `docker load`

![镜像操作命令](./images/镜像操作命令.png)

<span style="color:#f20c00;">**以上命令不需要死记硬背，可以通过 help 命令查询**</span>：`docker [命令名] --help`



##### 操作案例

==**案例**：从DockerHub中拉取一个Nginx镜像并查看==

①首先去镜像仓库搜索Nginx镜像：[DockerHub](https://hub-stage.docker.com/)

![在DockerHub中查找Nginx官方镜像](./images/在DockerHub中查找Nginx官方镜像.png)

在图示位置可以查看Linux命令

![查看Docker镜像的拉取命令](./images/查看Docker镜像的拉取命令.png)

②执行镜像拉取命令

![通过Docker命令拉取Nginx镜像](./images/通过Docker命令拉取Nginx镜像.png)

③通过 **images** 查看拉取到的镜像

![查看拉取到的Nginx镜像](./images/查看拉取到的Nginx镜像.png)



==**案例**：利用docker save将Nginx镜像导出磁盘，然后通过docker load加载回来==

①利用 `docker xx --help` 命令查看 `docker save` 和 `docker load` 的语法

![docker中的save命令和load命令](./images/docker中的save命令和load命令.png)

②利用 `docker tag` 创建新镜像 *mynginx1.0*

![Docker中利用tag命令创建新镜像](./images/Docker中利用tag命令创建新镜像.png)

③利用 `docker save` 导出镜像到磁盘

![Docker中利用save命令导出镜像](./images/Docker中利用save命令导出镜像.png)

④利用`docker rmi`删除Docker中的镜像（<span style="color:red;">这里我们把 nginx 镜像也删除掉了</span>）

![Docker中利用rmi命令删除镜像](./images/Docker中利用rmi命令删除镜像.png)

⑤利用`docker load`从磁盘中导入镜像

![Docker中利用load命令加载镜像](./images/Docker中利用load命令加载镜像.png)



##### 总结

镜像操作有如下命令：

- docker images
- docker rmi
- docker pull
- docker push
- docker save
- docker load
- docker tag
- docker build

![docker镜像操作练习-拉取Redis镜像](./images/docker镜像操作练习-拉取Redis镜像.png)

---

![Redis官方镜像](./images/Redis官方镜像.png)

---

![Docker镜像练习-Redis镜像](./images/Docker镜像练习-Redis镜像.png)



#### 容器操作

![容器操作相关命令](./images/容器操作相关命令.png)

##### 操作案例

==**案例**：创建并运行一个Nginx容器==

①去docker hub查看Nginx的容器运行命令

![dockerhub中查看nginx容器运行命令](./images/dockerhub中查看nginx容器运行命令.png)

```sh
$ docker run --name mn -p 8080:80 -d nginx
```

命令解读

- `docker run`：创建并运行一个容器
- `--name`：给容器起一个名字，比如叫做 mn（my nginx）
- `-p`：将宿主机端口与容器端口映射（宿主机端口 : 容器端口）；<span style="color:red;">容器是完全隔离的，无法直接访问到，只能通过映射端口去访问</span>
- `-d`：后台运行容器
- `nginx`：镜像名称，例如 nginx，tag标签可缺省，默认**latest**

②执行上面的命令

③查看运行的容器及状态

④访问宿主机映射端口并查看容器的运行日志

![docker创建并运行一个容器](./images/docker创建并运行一个容器.png)

⑤==如何持续的监控日志==     <span style="color:red;">通过 `-f` 选项可以持续监控日志</span>

![docker的logs命令](./images/docker的logs命令.png)



==**案例**：进入Nginx容器，修改HTML文件内容，添加“欢迎，Spring Stone！”==

①执行以下命令，进入容器

```sh
$ docker exec -it mn bash
```

命令解读：

- `docker exec`：进入容器内部，执行一个命令
- `-it`：给当前进入的容器创建一个标准输入输出终端，允许我们与容器交互
- `mn`：要进入的容器的名称
- `bash`：进入容器后执行的命令，bash是一个Linux终端交互命令

![docker中利用exec命令进入容器内部](./images/docker中利用exec命令进入容器内部.png)

<span style="color:red;">可以看出，容器内部有着一套自己的文件系统！但是最简易版本的</span>

②到Docker Hub中查找Nginx在镜像中的目录位置

![到DockerHub中确认Nginx镜像中Nginx的目录位置](./images/到DockerHub中确认Nginx镜像中Nginx的目录位置.png)

③进入镜像中的Nginx目录，查看并修改静态文件内容

```sh
# 修改页面内容
$ sed -i 's#Welcome to nginx#欢迎，Spring Stone#g' index.html
# 修改页面编码字符集，以支持中文显示
$ sed -i 's#<head>#<head><meta charset="utf-8">#g' index.html
```

![修改Nginx镜像中的Nginx静态文件](./images/修改Nginx镜像中的Nginx静态文件.png)

④重新访问Nginx

![修改静态文件后访问Nginx镜像容器](./images/修改静态文件后访问Nginx镜像容器.png)



==**容器操作的其他相关命令**==

停止运行 mn 容器

```sh
$ exit # 退出容器
$ docker stop mn
```

查看所有容器，包括停止运行的

```sh
$ docker ps -a
```

![停止运行Docker容器](./images/停止运行Docker容器.png)

启动 mn 容器

```sh
$ docker start mn
```

停止 mn 容器，并将其删除

```sh
$ docker stop mn
$ docker rm mn
# 或者
$ docker rm -f mn
```

![Docker启动容器及强制删除容器](./images/Docker启动容器及强制删除容器.png)



##### 总结

查看容器状态

- `docker ps`
- 添加 `-a` 参数查看所有状态的容器

删除容器

- `docker rm`
- 不能删除运行中的容器，除非添加`-f`参数

进入容器

- 命令是`docker exec -it [容器名] [要执行的命令]`
- exec命令可以进入容器修改文件，但是在容器内修改文件是不推荐的
    - 修改操作没有记录
    - 修改比较麻烦

![练习-Redis容器](./images/练习-Redis容器.png)

---

![Redis镜像文档](./images/Redis镜像文档.png)

```sh
# 修改命令，添加端口映射
$ docker run --name mr -p 6379:6379 -d redis redis-server --appendonly yes
```

---

![查看运行中的Redis容器并连接Redis](./images/查看运行中的Redis容器并连接Redis.png)

---

![练习-Redis容器中的Redis存值练习](./images/Redis容器中的Redis存值练习.png)

---

执行如下命令：

```sh
$ docker exec -it mr bash
# 或者用如下快捷命令
$ docker exec -it mr redis-cli
```

![Redis容器中的Redis存值练习结果](./images/Redis容器中的Redis存值练习结果.png)



#### 数据卷(容器数据管理)

在Docker容器中，**文件和数据** 都是与 **容器** 耦合在一起的，这会产生许多问题

![容器与数据耦合的问题](./images/容器与数据耦合的问题.png)

**数据卷（volume）**是一个虚拟目录，指向宿主机文件系统中的某个目录。

容器在创建后可以使用数据卷，将容器中的文件目录与数据卷进行关联（本质是在跟宿主机文件系统上的目录进行关联），从而实现数据与容器的解耦。

![数据卷与容器目录进行关联](./images/数据卷与容器目录进行关联.png)

数据卷操作命令的基本语法如下：

```sh
$ docker volume [COMMAND]
```

`docker volume`命令是数据卷操作，根据命令后跟随的command来确定具体的下一步的操作：

- **create**：创建一个volume
- **inspect**：显示一个或多个volume的信息
- **ls**：列出所有的volume
- **prune**：删除未使用的volume
- **rm**：删除一个或多个指定的volume



##### 操作案例

==**案例**：创建一个数据卷，并查看数据卷在宿主机的目录位置==

①创建数据卷

```sh
$ docker volume create html
```

②查看所有数据

```sh
$ docker volume ls
```

③查看数据卷详细信息

```sh
$ docker volume inspect
```

![数据卷操作练习](./images/数据卷操作练习.png)



#### 挂载数据卷

我们在创建容器时，可以通过 `-v` 参数来挂载一个数据卷到某个容器目录（**-v volumeName: /targetContainerPath**）

<span style="color:red;">**即使挂载一个不存在的数据卷也是可以的，它会在被挂载前被创建**</span>

![挂载数据卷说明案例](./images/挂载数据卷说明案例.png)



##### 操作案例

==**案例**：创建一个Nginx容器，修改容器内的html目录下的index.html内容==

![挂载数据卷案例需求](./images/挂载数据卷案例需求.png)

①创建容器并挂载数据卷到容器内的HTML目录

```sh
$ docker run --name mn -v html:/usr/share/nginx/html -p 8080:80 -d nginx
```

②进入html数据卷所在位置，修改HTML内容

```sh
$ docker volume inspect html
$ vim /var/lib/docker/volumes/html/_data/index.html
```

③通过映射端口访问Nginx容器，查看是否成功修改内容

![数据卷挂载案例结果验证](./images/数据卷挂载案例结果验证.png)



##### 拓展

==**案例**：创建并运行一个MySQL容器，将宿主机目录直接挂载到容器==

<span style="color:red;">提示：目录挂载与数据卷挂载的语法类似</span>

- -v [宿主机目录]:[容器内目录]
- -v [宿主机文件]:[容器内文件]

<span style="color:lightgreen;">实现思路如下</span>

1.将 mysql.tar 文件上传到虚拟机，通过 load 命令加载为镜像

```sh
$ docker load -i mysql.tar
# 或者拉取镜像
$ docker pull mysql:8.0.36
```

2.创建目录 `/tmp/mysql/data`

3.创建目录 `/tmp/mysql/conf`，将 **hmy.cnf** 文件上传到 `/tmp/mysql/conf`

```sh
$ mkdir -p /tmp/mysql/data /tmp/mysql/
```

hmy.cnf 内容如下：

```ini
[mysqld]
skip-name-resolve
character_set_server=utf8
datadir=/var/lib/mysql
server-id=1000
```

4.在Docker Hub上查阅资料，创建并运行MySQL容器，要求：

- 挂载 `/tmp/mysql/data/` 到 mysql容器 内数据存储目录
- 挂载 `/tmp/mysql/conf/hmy.cnf` 到 mysql容器 的配置文件
- 设置 MySQL 密码

![MySQL官方镜像使用文档](./images/MySQL官方镜像使用文档.png)

---

![MySQL镜像-自定义配置文件](./images/MySQL镜像-自定义配置文件.png)

---

![MySQL镜像-数据存储位置](./images/MySQL镜像-数据存储位置.png)

将文档中的命令稍加修改得到如下命令

```sh
$ docker run \
# 容器命名：mysql
$  --name mysql \
# 设置root账号密码
$  -e MYSQL_ROOT_PASSWORD=1234 \
# 端口映射
$  -p 3316:3306\
# 挂载自定义配置文件（容器的配置文件位置参考图2）
$  -v /tmp/mysql/conf/hmy.cnf:/etc/mysql/conf.d/hmy.cnf \
# 挂载数据存储目录（容器的数据存储目录参考图3）
$  -v /tmp/mysql/data:/var/lib/mysql \
# 以守护进程在后台运行
$  -d \
# 指定容器镜像
$  mysql:8.0.36
```

5.验证结果

![mysql容器创建结果](./images/mysql容器创建结果.png)



##### 两种挂载方式对比

![数据卷挂载方式对比](./images/数据卷挂载方式对比.png)

挂载容器目录的方式有两种

①数据卷挂载耦合度低，由docker来管理目录，但是目录较深，不好找

②目录挂载耦合度高，需要我们自己管理目录，不过目录容器查找



### Dockerfile自定义镜像

#### 镜像结构

<span style="color:red;">**镜像**是将应用程序及其需要的系统函数库、环境、配置、依赖打包而成的。</span>

![镜像的结构](./images/镜像的结构.png)

镜像是分层结构，每一层称为一个Layer

- BaseImage层：包含基本的系统函数库、环境变量、文件系统
- EntryPoint：入口，是镜像中应用启动的命令
- 其它：在BaseImage基础上添加依赖、安装程序、完成整个应用的安装和配置



#### Dockerfile语法

Dockerfile 就是一个文本文件，其中包含一个个的**指令（Instruction）**，用指令来说明要执行什么操作来构建镜像。每一个指令都形成一层Layer。

|    指令    |                     说明                     |             示例             |
| :--------: | :------------------------------------------: | :--------------------------: |
|    FROM    |                 指定基础镜像                 |        FROM centos:6         |
|    ENV     |        设置环境变量，可在后面指令使用        |        ENV key=value         |
|    COPY    |         拷贝本地文件到镜像的指定目录         |  COPY ./mysql-5.7.rpm /tmp   |
|    RUN     |  执行Linux的shell命令，一般是安装过程的命令  |     RUN yum install gcc      |
|   EXPOSE   | 指定容器运行时监听的端口，是给镜像使用者看的 |         EXPOSE 8080          |
| ENTRYPOINT |     镜像中应用的启动命令，容器运行时调用     | ENTRYPOINT java -jar xxx.jar |

更详细的语法说明，参考官网文档：https://docs.docker.com/engine/reference/builder/



#### 构建Java项目

==**案例**：基于Ubuntu镜像构建一个新镜像，运行一个Java项目==

##### ①新建目录

新建一个空文件夹 docker-demo

```sh
$ mkdir /tmp/docker-demo
```

##### ②拷贝jar包

将 docker-demo.jar 文件拷贝到 docker-demo 这个目录

首先，新建一个Maven项目

![docker-demo项目创建步骤1](./images/docker-demo项目创建步骤1.png)

---

![docker-demo项目创建步骤2](./images/docker-demo项目创建步骤2.png)

然后，在pom.xml中添加如下内容

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.stone</groupId>
    <artifactId>docker-demo</artifactId>
    <version>1.0-SNAPSHOT</version>

    <!--指定SpringBoot为父工程-->
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.5.6</version>
    </parent>

    <dependencies>
        <!--引入SpringBoot的Web依赖-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
            <version>2.5.6</version>
        </dependency>
    </dependencies>

    <build>
        <finalName>${project.artifactId}</finalName>
        <plugins>
            <!-- 指定Maven编译器的jdk版本，
                 如果不指定，Maven3默认使用jdk1.5，Maven2默认使用jdk1.3
            -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <source>1.8</source>
                    <target>1.8</target>
                    <encoding>UTF-8</encoding>
                </configuration>
            </plugin>
            <!--SpringBoot框架提供了一套自己的打包机制，
                是通过 spring-boot-maven-plugin 插件来实现的
            -->
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <executions>
                    <execution>
                        <goals>
                            <goal>repackage</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>

</project>
```

[maven-compiler-plugin 插件详解](https://blog.csdn.net/Java_1710/article/details/120992614)

> maven-compiler-plugin 插件是一个 Maven 插件，用来编译项目代码

==spring-boot-maven-plugin插件的作用==

> 在SpringBoot项目中，框架提供了一套自己的打包机制，是通过spring-boot-maven-plugin插件来实现的。
>
> 可以在Maven的生命周期package后重新打包，生成新的JAR包。
>
> spring-boot-maven-plugin插件将SpringBoot的项目打包成FAT-JAR，也就是说将项目启动运行所需要的JAR都包含进来了。
>
> 普通的JAR和SpringBoot打包成的FAT-JAR内部结构是不一样的。
>
> ——参考文章：https://blog.csdn.net/goodjava2007/article/details/122205769

<span style="color:red;">详细实现代码参考项目 docker-demo</span>

<span style="color:blue;">将项目打成jar包，并拷贝</span>



##### ③拷贝jdk

将 jdk8.tar.gz 文件拷贝到 docker-demo 这个目录

[JDK下载地址](https://www.oracle.com/cn/java/technologies/downloads/)

![Oracle的JDK下载](./images/Oracle的JDK下载.png)



##### ④拷贝Dockerfile

将 Dockerfile 拷贝到 docker-demo 这个目录

```dockerfile
# 指定基础镜像
FROM ubuntu:16.04
# 配置环境变量，JDK的安装目录
ENV JAVA_DIR=/usr/local

# 拷贝jdk和项目jar包
COPY ./jdk-8u401-linux-x64.tar.gz $JAVA_DIR/
COPY ./docker-demo.jar /tmp/app.jar

# 安装jdk
RUN cd $JAVA_DIR \
 && tar -xf ./jdk-8u401-linux-x64.tar.gz \
 && mv ./jdk1.8.0_401 ./java8

# 配置环境变量
ENV JAVA_HOME=$JAVA_DIR/java8
ENV PATH=$PATH:$JAVA_HOME/bin

# 暴露端口
EXPOSE 6789
# 入口，Java项目的启动命令
ENTRYPOINT java -jar /tmp/app.jar
```

<span style="color:red;">**注意：**Dockerfile里的文件目录指的都是容器的文件系统中的文件目录！！！</span>



##### ⑤进入docker-demo

```sh
$ cd /tmp/docker-demo
```



##### ⑥构建镜像

运行如下命令

```sh
$ docker build -t javaweb:1.0 .
```

<span style="color:red;">命令末尾的 “ . “ 表示在当前目录执行镜像构建，构建过程中所用到的文件都会在当前目录下查找</span>

查看生成的镜像，并运行一个对应的容器，查看容器

```sh
$ docker images
$ docker run --name web -p 6789:6789 -d javaweb:1.0
$ docker ps
```



##### ⑦访问验证

访问镜像：http://ip:6789/hello/count，结果如下

![自定义java项目镜像访问结果](./images/自定义java项目镜像访问结果.png)

##### ⑧后续优化

从 Dockerfile 中可以看出，从 0 开始构建一个java项目的Docker镜像还是比较麻烦的。

通过分析我们可以得出，Dockerfile 中的==动作层==可以分为两部分：安装jdk 、构建java项目

```dockerfile
# 指定基础镜像
FROM ubuntu:16.04
# 配置环境变量，JDK的安装目录
ENV JAVA_DIR=/usr/local

# 拷贝jdk
COPY ./jdk-8u401-linux-x64.tar.gz $JAVA_DIR/

# 安装jdk
RUN cd $JAVA_DIR \
 && tar -xf ./jdk-8u401-linux-x64.tar.gz \
 && mv ./jdk1.8.0_401 ./java8

# 配置环境变量
ENV JAVA_HOME=$JAVA_DIR/java8
ENV PATH=$PATH:$JAVA_HOME/bin

# ===========================分割线=============================

# 拷贝项目jar包
COPY ./docker-demo.jar /tmp/app.jar
# 暴露端口
EXPOSE 6789
# 入口，Java项目的启动命令
ENTRYPOINT java -jar /tmp/app.jar
```

而**安装jdk**是每一个java项目镜像构建时所必须的==动作层==，因此我们可以将这部分动作层构建成一个<span style="color:red;">基础镜像</span>，以供每次构建java项目镜像的时候调用。

这样就方便了许多，可以省去大量重复的工作，<span style="color:green;">这就是分层的好处</span>。

<span style="color:blue;">java:8-alpine镜像</span> 已经帮我们封装好了，它是一个体积非常小的 jdk镜像，基于它去构建我们自己的java项目镜像，操作就变得非常简单了。

```dockerfile
# 指定基础镜像
FROM java:8-alpine

# 拷贝项目jar包
COPY ./docker-demo.jar /tmp/app.jar
# 暴露端口
EXPOSE 6789
# 入口，Java项目的启动命令
ENTRYPOINT java -jar /tmp/app.jar
```



#### 总结

1.Dockerfile 的本质是一个文件，通过指令描述镜像的构建过程

2.Dockerfile 的第一行必须是 `FROM`，从一个基础镜像来构建

3.基础镜像可以是基本操作系统，如Ubuntu；也可以是其他人制作好的镜像，例如java:8-apline



### DockerCompose

![DockerCompose](./images/DockerCompose.png)

<span style="color:blue;">Docker Compose</span> 可以基于 **Compose文件** 帮我们快速的部署分布式应用，而无需手动一个个创建和运行容器！

Compose文件是一个文本文件，通过指令定义集群中的每个容器如何运行。【Compose文件可以看做**N**个`docker run` 命令的集合】

[DockerCompose的详细语法参考官网](https://docs.docker.com/compose/compose-file/)

Compose文件样例（是yaml格式的）

```yaml
version: "3.8"

services:
  mysql:
    image: mysql:5.7.25
    environment:
      MYSQL_ROOT_PASSWORD: 1234
    volumes:
      - /tmp/mysql/data:/var/lib/mysql
      - /tmp/myysql/conf/hmy.cnfL/etc/mysql/conf.d/hmy.cnf
  web:
    build: .
    ports:
      - 8090: 8090
```

对比`docker run`命令

```sh
# mysql容器
$ docker run \
$   --name mysql \
$   -e MYSQL_ROOT_PASSWORD=1234 \
$   -p 3306:3306
$   -v /tmp/mysql/conf/hmy.cnf:/etc/mysql/conf.d/hmy.cnf
$   -v /tmp/mysql/data:/var/lib/mysql \
$   -d \
$   mysql:5.7.25

# 构建镜像
$ docker build -t web:1.0 .
# 运行容器
$ docker run --name web -p 8090:8090 -d web:1.0
```

![Compose指令与Docker的Run命令对比](./images/Compose指令与Docker的Run命令对比.png)



#### 安装DockerCompose

##### 1.下载

```sh
$ curl -L https://github.com/docker/compose/releases/download/v2.24.6/docker-compose-`uname -s`-`uname -m` > /usr/local/bin/docker-compose
```

其中 `uname -s` 和 `uname -m` 分别是获取操作系统名称和计算机处理器类型的命令。最终得到的链接如下

>https://github.com/docker/compose/releases/download/v2.24.6/docker-compose-linux-x86_64



或者我们也可以直接在github上下载二进制文件，上传到Linux，操作如下：

==上述的下载链接就是此处的下载链接地址==

![DockerCompose二进制文件下载](./images/DockerCompose二进制文件下载.png)

将文件上传到 `/usr/local/bin` 目录即可。



##### 2.修改文件权限

为刚刚上传的文件添加执行权限

```sh
$ chmod +x /usr/local/bin/docker-compose-linux-x86_64
```



##### 3.配置自动补全命令

配置了自动补全命令后，在编写Compose文件时会有自动提示，方便编写。

```sh
$ curl -L https://raw.githubusercontent.com/docker/compose/master/contrib/completion/bash/docker-compose > /etc/bash_completion.d/docker-compose
```

如果这里始终下载不下来，需要修改自己的Linux中的hosts文件

```sh
$ echo "199.232.68.133 raw.githubusercontent.com" >> /etc/hosts
```



### Docker镜像服务

==**案例**：将之前的 cloud-demo 微服务集群利用 DockerCompose 部署==

#### 预备工作

1、准备一个 cloud-demo 文件夹，在其中编写好各个服务和网关的 Dockerfile 文件，以便后面Docker构建镜像并运行容器，文件夹结构如下：

> cloud-demo根文件夹
>
> - gateway子文件夹
>   - Dockerfile文件
> - userservice子文件夹
>   - Dockerfile文件
> - orderservice子文件夹
>   - Dockerfile文件

Dockerfile内容统一如下：

```dockerfile
FROM java:8-alpine
COPY ./app.jar /tmp/app.jar
ENTRYPOINT java -jar /tmp/app.jar
```

2、在 cloud-demo 文件夹下创建 mysql 子文件夹，并将之前的 hmy.cnf 文件拷贝进去，文件夹结构如下：

> cloud-demo根文件夹
>
> - mysql子文件夹
>   - conf子文件夹
>     - hmy.cnf
>   - data子文件夹
> - 其他子文件夹



3、编写 docker-compose.yml 文件，并上传到 cloud-demo 根文件夹下

```yaml
version: "3.2"

services:
  nacos:
    image: nacos/nacos-server
    environment:
      MODE: standalone
    ports:
      - "8848:8848"
  mysql:
    image: mysql:8.0.36
    environment:
      MYSQL_ROOT_PASSWORD: 1234
      TZ: "Asia/Shanghai" # 设置MySQL时区
    volumes:
      # $PWD 表示执行PWD命令，获取当前所在目录位置
      - "$PWD/mysql/data:/var/lib/mysql"
      - "$PWD/mysql/conf:/etc/mysql/conf.d/"
    ports:
      - "13306:3306"
  orderservice:
    build: ./orderservice
    environment:
      TZ: "Asia/Shanghai" # 设置时区，以解决jar包运行时打印的日志的时间不是中国时间的问题
  userservice:
    build: ./userservice
    environment:
      TZ: "Asia/Shanghai"
  gateway:
    build: ./gateway
    environment:
      TZ: "Asia/Shanghai"
    ports:
      - "10010:10010"
```



#### 项目打包

1、用DockerCompose进行项目部署时，所有的服务之间都可以通过服务名互相访问。

所以我们需要修改自己的 cloud-demo 项目，将数据库、Nacos地址都改为 docker-compose 中的服务名

![DockerCompose部署-修改nacos、mysql地址](./images/DockerCompose部署-修改nacos、mysql地址.png)

2、使用Maven打包工具，将项目中的每个微服务都打包为app.jar（因为上面的Dockerfile中，统一指定了包名）

在 pom.xml 文件中添加如下标签，即可配置每个模块的最终打包名称

```xml
<build>
    <!--指定最终生成的jar包名称-->
    <finalName>app</finalName>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
        </plugin>
    </plugins>
</build>
```

3、将打包好的app.jar拷贝到 cloud-demo 中的每一个对应的子目录中，最终的文件夹结构如下：

> cloud-demo根文件夹
>
> - gateway子文件夹
>     - Dockerfile文件
>     - app.jar （来自 gateway 模块）
> - userservice子文件夹
>     - Dockerfile文件
>     - app.jar （来自 userservice 模块）
> - orderservice子文件夹
>     - Dockerfile文件
>     - app.jar （来自 orderservice 模块）
> - mysql子文件夹
>     - conf子文件夹
>         - hmy.cnf
>     - data子文件夹
> - docker-compose.yml



#### 上传文件

##### 部署容器

将 cloud-demo 上传到虚拟机 **/tmp** 目录下，利用 `docker-compose up -d` 命令来部署

![docker-compose命令用法详解](./images/docker-compose命令用法详解.png)

```sh
$ cd /tm/cloud-demo
$ docker-compose up -d
```

全部部署成功后，会得到如下图的结果：

![DockerCompose镜像部署-1](./images/DockerCompose镜像部署-1.png)



##### 查看日志

随后我们可以利用 `docker-compose logs` 命令来查看容器的运行日志

![docker-compose的logs命令](./images/docker-compose的logs命令.png)

```sh
# 查看所有容器的运行日志
$ docker-compose logs -f
# 查看userservice服务容器的运行日志
$ docker-compose logs -f userservice
```

通过日志，我们可以看到userservice服务运行时候报了错

![DockerCompose镜像部署-2](./images/DockerCompose镜像部署-2.png.)

这是因为 Nacos 与 UserService 服务是同时运行的，在 UserService 服务启动的过程中会尝试去向 Nacos 注册服务，但是此时 Nacos 尚未启动成功，导致注册失败，故而报错，其它服务也是同理。



##### 同步环境

虽然 UserService 等几个服务容器启动失败了，但是 Nacos 和 MySQL 是成功运行起来了的，

我们可以先将之前的 **Nacos命名空间** 以及 **MySQL数据库** 都同步过来

![同步Nacos和MySQL](./images/同步Nacos和MySQL.png)

随后我们可以通过 `docker-compose restart` 命令重启服务

![docker-compose的restart命令](./images/docker-compose的restart命令.png)

```sh
$ docker-compose restart gateway userservice orderservice
```

全部重启成功后，会得到如下图的结果：

![DockerCompose镜像部署-3](./images/DockerCompose镜像部署-3.png)

然后我们再去查看一次 UserService 服务容器的运行日志，会发现没有任何报错了

![DockerCompose镜像部署-4](./images/DockerCompose镜像部署-4.png)



##### 访问验证

![DockerCompose镜像部署-5](./images/DockerCompose镜像部署-5.png)



### Docker镜像仓库

镜像仓库（Docker Registry）有公共的和私有的两种形式：

- 公共仓库：例如Docker官方的 Docker Hub，国内也有一些云服务商提供类似于 Docker Hub 的公开服务，比如 网易云镜像服务、DaoCloud 镜像访问、阿里云镜像服务等
- 除了使用公开仓库外，用户还可以在本地搭建私有 Docker Registry。企业自己的镜像最好是采用私有 Docker Registry 来实现。



#### 搭建私有镜像仓库

搭建镜像仓库可以基于 Docker 官方提供的 Docker Registry 来实现。

官网地址：https://hub.docker.com/_/registry



##### 1、简化版镜像仓库

Docker 官方的 Docker Registry 是一个基础版本的 Docker 镜像仓库，具备仓库管理的完整功能，<span style="color:red;">但是没有图形化界面</span>。

搭建方式比较简单，命令如下：

```sh
$ docker run -d \
$     --restart=always \
$     --name registry \
$     --p 5000:5000 \
$     -v registry-data:/var/lib/registry \
$     registry
```

命令中挂载了一个数据卷 registry-data 到容器内的 /var/lib/registry 目录，<span style="color:red;">这是私有镜像仓库存放数据的目录</span>。

访问 http://YourIP:5000/v2/_catalog 可以查看当前私有镜像服务中包含的镜像。



##### 2、图形化界面版本

使用 DockerCompose 部署带有图形化界面的 Docker Registry，命令如下：

```yaml
version: "3.0"
services:
  registry:
    image: registry
    volumes:
      - ./registry-data:/var/lib/registry
  ui:
    image: joxit/docker-registry-ui:static
    ports: 8080:80
    environment:
      - REGISTRY_TITLE="SpringStone私有仓库"
      - REGISTRY_URL=http://registry:5000
    depends_on:
      - registry #声明该服务依赖于上面的registry服务
```

该图形化界面并非 Docker 官方提供的，而是第三方个人在官方的 Docker Registry 镜像的基础上开发出来的，所以它是一个额外的服务。



##### 3、配置Docker信任地址

我们的私服采用的是 http 协议，默认不被 Docker 信任，所以需要做一个配置

```sh
# 打开要修改的文件
$ vi /etc/docker/daemon.json

# 添加内容
$ "insecure-registries": ["http://YourIP:8080"]

# 重加载
$ systemctl daemon-reload

# 重启docker
$ systemctl restart docker
```

配置好以后我们就可以把上面的 docker-compose.yml 文件拷贝到 /tmp/registry-ui 目录下，然后使用`docker-compose up -d`命令去构建了。

等到构建成功以后，我们就可以去访问图形化界面 http://YourIP:8080 验证私有镜像仓库了。



#### 拉取和推送镜像

推送镜像到私有镜像服务必须先tag，步骤如下：

①重新 tag 本地镜像，名称前缀为私有仓库的地址：YourIP:8080/

```sh
$ docker tag nginx:latest YourIP:8080/nginx:1.0
```

②推送镜像

```sh
$ docker push YourIP:8080/nginx:1.0
```

③拉取镜像

```sh
$ docker pull YourIP:8080/nginx:1.0
```



#### 总结

1、推送本地镜像到仓库前都必须<span style="color:red;">重命名（docker tag）镜像</span>，以镜像仓库地址为前缀

2、镜像仓库推送前需要把仓库地址配置到 docker 服务的 daemon.json 文件中，被 docker 信任

3、推送使用`docker push`命令

4、拉取使用`docker pull`命令



## RabbitMQ

### 初识MQ

#### 同步通讯和异步通讯

![同步通讯和异步通讯](./images/同步通讯和异步通讯.png)



##### 同步调用的问题

微服务间基于Feign的调用就属于同步方式，存在一些问题

<img src="./images/同步调用的问题.png" alt="同步调用的问题" style="zoom:50%;" />

在上图的同步调用过程中，会出现以下4种问题：

![同步调用的四大问题](./images/同步调用的四大问题.png)

同步调用的优点：

- 时效性较强，可以立即得到结果

同步调用的问题：

- 耦合度高
- 性能和吞吐能力下降
- 有额外的资源消耗
- 有级联失败问题



##### 异步调用方案

异步调用常见实现就是**事件驱动模式**

<img src="./images/异步调用方案.png" alt="异步调用方案" style="zoom:50%;" />

优势一：服务解耦

功能业务的增减不需要再修改代码，只需要增减订阅事件即可

<img src="./images/异步调用优势一.png" alt="异步调用优势一" style="zoom:50%;" />

优势二：性能提升，吞吐量提高

被请求的服务不必再等到所有它调用的服务都执行结束，就可以立即做出响应

<img src="./images/异步调用优势二.png" alt="异步调用优势二" style="zoom:50%;" />

优势三：服务没有强依赖，不必担心级联失败问题

<img src="./images/异步调用优势三.png" alt="异步调用优势三" style="zoom:50%;" />



优势四：流量削峰

高并发场景下的峰值流量都交由broker记录管理，各个服务只需要根据能力去依次处理它们

<img src="./images/异步调用优势四.png" alt="异步调用优势四" style="zoom:50%;" />

异步通信的优点：

- 耦合度低
- 吞吐量提升
- 故障隔离
- 流量削峰

异步通信的缺点：

- 依赖于Broker的可靠性、安全性、吞吐能力
- 构架复杂了，业务没有明显的流程线，不好追踪管理



#### 什么是MQ

MQ（MessageQueue），中文是消息队列，字面来看就是存放消息的队列。也就是事件驱动架构中的**Broker**。

![四种MQ框架对比](./images/四种MQ框架对比.png)



### RabbitMQ快速入门

**概述**

RabbitMQ是基于`Erlang`语言开发的开源消息通信中间件，官网地址：https://www.rabbitmq.com/



#### 部署安装

##### 单机部署

我们在Centos7虚拟机中使用Docker来安装。

1.下载镜像

方式一：在线拉取

```sh
$ docker pull rabbitmq:3-management
```



方式二：从本地加载

将mq.tar上传到虚拟机中，使用命令加载镜像即可：

```sh
$ docker load -i mq.tar
```



2.安装MQ

执行下面的命令来运行MQ容器：

```sh
$ docker run \
  -e RABBITMQ_DEFAULT_USER=stone \
  -e RABBITMQ_DEFAULT_PASS=1234 \
  --name mq \
  --hostname mq1 \
  -p 15672:15672 \
  -p 5672:5672 \
  -d \
  rabbitmq:3-management
```

命令解读

- `docker run`：创建并运行一个容器
- `-e RABBITMQ_DEFAULT_USER`：定义登录MQ管理页面的账号用户名
- `-e RABBITMQ_DEFAULT_PASS`：定义登录MQ管理页面的账号密码
- `--name`：给容器起一个名字，比如叫做 mq
- `--hostname`：配置主机名，在集群部署模式中会使用到
- `-p`：端口映射，这里开放了两个端口 15672是控制台页面的访问端口、5672是消息通信的端口
- `-d`：后台运行容器
- `rabbitmq:3-management`：镜像名称



3.访问

通过`docker ps`命令查看容器运行的状态：

<img src="./images/查看RabbitMQ容器的运行状态.png" alt="查看RabbitMQ容器的运行状态"/>

并且通过15672端口访问RabbitMQ的控制台页面：

![RabbitMQ的管理页面](./images/RabbitMQ的管理页面.png)

使用事先配置好的stone账号登录：

![RabbitMQ的控制台主页面](./images/RabbitMQ的控制台主页面.png)



##### 集群部署

TODO



#### 结构和概念

![RabbitMQ的结构和概念](./images/RabbitMQ的结构和概念.png)

- Publisher：发布者，负责发布消息到exchange

- exchange：交换机，负责路由，将Publisher发布过来的消息投递到queue中

- queue：消息队列，负责缓存消息

- VirtualHost：虚拟主机，负责对queue、exchange等资源进行逻辑分组，数据隔离

- Consumer：消费者，负责获取和处理queue中缓存的消息
- channel：消息通道，负责操作MQ的工具



#### 常见消息模型

<img src="./images/RabbitMQ官方Demo.png" alt="RabbitMQ官方Demo" style="zoom: 50%;" />

MQ的官方文档中给出了7个MQ的Demo示例，前5个对应了几种不同的用法：

- 基本消息队列（BasicQueue）

![MQ模型-基本消息队列](./images/MQ模型-基本消息队列.png)

- 工作消息队列（WorkQueue）

![MQ模型-工作消息队列](./images/MQ模型-工作消息队列.png)

- 发布订阅（Publish、Subscribe），又根据交换机类型不同分为三种：

    - Fanout Exchange：广播

    ![MQ模型-发布订阅](./images/MQ模型-发布订阅.png)

    - Direct Exchange：路由

    ![MQ模型-路由](./images/MQ模型-路由.png)

    - Topic Exchange：主题

    ![MQ模型-主题](./images/MQ模型-主题.png)



##### 1.基本消息队列

官方的HelloWorld案例是基于最基础的消息队列模型来实现的，只包括三个角色：

- publisher：消息发布者，将消息发送到队列
- queue：消息队列，负责接收并缓存消息
- consumer：消息订阅者/消息消费者，负责订阅队列，处理其中的消息

![基本消息队列包括3个角色](./images/基本消息队列包括3个角色.png)

==**案例：**完成官方Demo中的HelloWorld案例==

首先创建一个Maven父工程**mq-demo**，然后创建两个子模块**publisher**、**consumer**：

![mq-demo项目的目录结构](./images/mq-demo项目的目录结构.png)

然后在父工程mq-demo的pom.xml文件中引入依赖：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.stone</groupId>
    <artifactId>mq-demo</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>pom</packaging>
    <modules>
        <module>publisher</module>
        <module>consumer</module>
    </modules>

    <properties>
        <maven.compiler.source>8</maven.compiler.source>
        <maven.compiler.target>8</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.6.6</version>
        <relativePath/>
    </parent>

    <dependencies>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>
        <!--AMQP依赖，包含RabbitMQ-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-amqp</artifactId>
        </dependency>
        <!--单元测试-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
        </dependency>
    </dependencies>

</project>
```

接着在两个子模块中创建SpringBoot启动类**PublisherApplication**、**ConsumerApplication**

以上准备工作完成以后，我们分别在两个子模块的`test`目录下创建测试类，并编写相应的功能代码：

消息发送 **PublisherTest**

```java
package com.stone.hello;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class PublisherTest {

    @Test
    public void testSendMessage() throws IOException, TimeoutException {
        // 1.建立连接
        ConnectionFactory factory = new ConnectionFactory();
        // 1.1.设置连接参数：主机名、端口号、vhost、用户名、密码
        factory.setHost("192.168.157.128");
        factory.setPort(5672);
        factory.setVirtualHost("/");
        factory.setUsername("stone");
        factory.setPassword("1234");
        // 1.2.建立连接
        Connection connection = factory.newConnection();

        // 2.创建通道Channel
        Channel channel = connection.createChannel();

        // 3.创建队列
        String queueName = "simple.queue";
        channel.queueDeclare(queueName, false, false, false, null);

        // 4.发送消息
        String message = "hello, rabbitmq!";
        channel.basicPublish("", queueName, null, message.getBytes());
        System.out.println("发送消息成功：【" + message + "】");

        // 5.关闭通道和连接
        channel.close();
        connection.close();
    }
}
```

消息接收 **ConsumerTest**

```java
package com.stone.hello;

import com.rabbitmq.client.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConsumerTest {

    public static void main(String[] args) throws IOException, TimeoutException {
        // 1.建立连接
        ConnectionFactory factory = new ConnectionFactory();
        // 1.1.设置连接参数：主机名、端口号、vhost、用户名、密码
        factory.setHost("192.168.157.128");
        factory.setPort(5672);
        factory.setVirtualHost("/");
        factory.setUsername("stone");
        factory.setPassword("1234");
        // 1.2.建立连接
        Connection connection = factory.newConnection();

        // 2.创建通道Channel
        Channel channel = connection.createChannel();

        // 3.创建队列
        //（因为Publisher和Consumer的启动先后顺序不确定，所以这里创建队列是为了避免项目启动时队列还没有被创建出来的保险措施）
        //（如果队列已经被创建了，那么此队列不会被重复创建）
        String queueName = "simple.queue";
        channel.queueDeclare(queueName, false, false, false, null);

        channel.basicConsume(queueName, true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
                // 5.处理消息
                String message = new String(body);
                System.out.println("接收到消息：【" + message + "】");
            }
        });

        System.out.println("等待接收消息...");
    }
}
```

完成以后，我们先来测试消息发送功能：加上断点后，启动PublisherTest，依次可以看到

**Connection被创建**

![PublisherTest断点1](./images/PublisherTest断点1.png)



![PublisherTest创建连接](./images/PublisherTest创建连接.png)

**Channel被创建**

![PublisherTest断点2](./images/PublisherTest断点2.png)



![PublisherTest创建通道](./images/PublisherTest创建通道.png)

**Queue被创建**

![PublisherTest断点3](./images/PublisherTest断点3.png)



![PublisherTest创建队列](./images/PublisherTest创建队列.png)

**消息被发送**

![PublisherTest控制台打印结果](./images/PublisherTest控制台打印结果.png)



![PublisherTest发送消息](./images/PublisherTest发送消息.png)



然后我们再来测试消息订阅功能：ConsumerTest也会依次创建Connection、Channel，并找到已经被创建的Queue读取消息

![ConsumerTest控制台打印结果](./images/ConsumerTest控制台打印结果.png)



同时Queue中的消息是一旦已经被消费了，就会被从Queue中移除（“阅后即焚”）

![ConsumerTest订阅消息](./images/ConsumerTest订阅消息.png)



##### 2.总结

基本消息队列的消息发送流程：

​	1.建立Connection

​	2.创建Channel

​	3.利用Channel声明队列

​	4.利用Channel向队列发送消息



基本消息队列的消息接收流程：

​	1.建立Connection

​	2.创建Channel

​	3.利用Channel声明队列

​	4.定义Consumer的消费行为**handleDelivery()**

​	5.利用Channel将消费者与队列绑定



### Spring AMQP

#### 什么是Spring AMQP

> **AMQP**
>
> Advanced Message Queuing Protocol，是用于在应用程序或之间传递业务消息的开放标准。
>
> 该协议与语言和平台无关，更符合微服务中独立性的要求。

![AMQP和SpringAMQP](./images/AMQP和SpringAMQP.png)

Spring AMQP 是基于**AMQP**协议定义的一套API规范，提供了模板来发送和接收消息。

包含两部分，其中**spring-amqp**是基础抽象，**spring-rabbit**是底层的默认实现。

[Spring AMQP官网](https://spring.io/projects/spring-amqp)



#### 基本消息队列

==利用Spring AMQP实现基本消息队列功能==

步骤流程如下：

1.在父工程引入spring-amqp的依赖

```xml
<!--AMQP依赖，包含RabbitMQ-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>
```



2.在publisher服务中利用RabbitTemplate发送消息到simple.queue这个队列

在publisher服务中编写application.yml，添加mq连接信息：

```yaml
# mq连接信息
spring:
  rabbitmq:
    # 主机名
    host: 192.168.157.128
    # 端口
    port: 5672
    # 虚拟主机
    virtual-host: /
    # 用户名
    username: stone
    # 密码
    password: 1234
```

在publisher服务中新建一个测试类，编写测试方法：

```java
package com.stone.mq.spring;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SpringAmqpTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testSendMessage2SimpleQueue() {
        String queueName = "simple.queue";
        String message = "hello, spring amqp";

        rabbitTemplate.convertAndSend(queueName, message);
    }
}
```

成功运行后，在控制台页面查看队列消息

![RabbitTemplate发送消息](./images/RabbitTemplate发送消息.png)

<span style="color:red;">**注意！**</span>RabbitTemplate只会发送消息，不会创建队列，所以在运行测试类之前我们需要到控制台页面手动创建**simple.queue**队列，或者通过如下注解代码创建队列：

```java
@Test
@RabbitListener(queuesToDeclare = {@Queue(value = "simple.queue", durable = "false")})
public void testSendMessage2SimpleQueue() {
    String queueName = "simple.queue";
    String message = "hello, spring amqp";

    rabbitTemplate.convertAndSend(queueName, message);
}
```

其中：@RabbitListener注解是用来监听消息队列的，

queuesToDeclare属性可以定义被监听的队列如果不存在则新创建（和@Queue搭配使用，其中durable属性指定队列是否持久化）。



3.在consumer服务中编写消费逻辑，绑定并监听simple.queue这个队列

在consumer服务中编写application.yml，添加mq连接信息：

```yaml
# mq连接信息
spring:
  rabbitmq:
    # 主机名
    host: 192.168.157.128
    # 端口
    port: 5672
    # 虚拟主机
    virtual-host: /
    # 用户名
    username: stone
    # 密码
    password: 1234
```

在consumer服务中新建一个类，编写消费逻辑：

（==由于Consumer服务是一个持续监听队列的运行程序，所以我们可以通过Component注解将其放到Spring容器中管理运行==）

```java
package com.stone.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class SpringRabbitListener {

    @RabbitListener(queues = "simple.queue")
    public void listenSimpleQueue(String msg) {
        System.out.println("消费者接收到simple.queue的消息：【" + msg + "】");
    }
}
```

运行consumer服务的启动类**ConsumerApplication**，并查看控制台输出：

![运行Consumer服务控制台打印](./images/运行Consumer服务控制台打印.png)

<span style="color:red;">注意！消息一旦被消费就会从队列中删除，RabbitMQ没有消息回溯功能</span>



#### 工作队列

Work Queue，工作队列，通过绑定多个消费者，来提高消息处理速度，避免队列消息堆积。

![消息队列模型-工作队列](./images/消息队列模型-工作队列.png)

==**案例：**模拟WorkQueue，实现一个队列绑定多个消费者==

步骤流程如下：

1.在publisher服务中定义测试方法，每秒产生50条消息，发送到simple.queue

```java
@Test
public void testSendMessage2WorkQueue() throws InterruptedException {
    String queueName = "simple.queue";
    String message = "hello, message__";
    for (int i = 0; i < 50; i++) {
        rabbitTemplate.convertAndSend(queueName, message + i);
        Thread.sleep(20);
    }
}
```

2.在consumer服务中定义两个消息监听者，都监听simple.queue队列

3.消费者1每秒处理50条消息，消费者2每秒处理10条消息

```java
@RabbitListener(queues = "simple.queue")
public void listenWorkQueue1(String msg) throws InterruptedException {
    System.out.println("消费者1接收到消息：【" + msg + "】 " + LocalTime.now());
    Thread.sleep(20);
}

@RabbitListener(queues = "simple.queue")
public void listenWorkQueue2(String msg) throws InterruptedException {
    // 使用err来区分两个方法中控制台打印输出的颜色
    System.err.println("消费者2......接收到消息：【" + msg + "】 " + LocalTime.now());
    Thread.sleep(100);
}
```

最终，consumer服务的控制台打印结果如下：

![consumer服务的运行控制台打印结果正常](./images/consumer服务的运行控制台打印结果正常.png)

从结果中可以看出：消费者1花费了一秒多的时间处理了25条奇数信息，消费者2花费了两秒多的时间处理了25条偶数信息，两者处理的信息数量很平均，消费者1并没有发挥出全部的性能。

<span style="color:blue;">这是由于RabbitMQ中存在一种“**消息预取**”机制</span>，该机制使得绑定到队列的每个消费者都可以将队列中的消息预先取出，再逐个处理。这就导致所有消费者平均分配队列中的消息，并且性能较差的消费者需要花费更多的时间来处理多分配到的消息。

<span style="color:green;">通过在application.yml文件中设置**preFetch**这个值，可以控制预取消息数的上限：</span>

```yaml
# mq连接信息
spring:
  rabbitmq:
    # 主机名
    host: 192.168.157.128
    # 端口
    port: 5672
    # 虚拟主机
    virtual-host: /
    # 用户名
    username: stone
    # 密码
    password: 1234
    listener:
      simple:
        # 设置消息预取上限，每次只能获取一条消息，处理完成才能获取下一条消息
        prefetch: 1
```

重新运行服务后，consumer服务的控制台打印结果如下：消费者2只处理了10条消息，消费者1处理了其余全部消息。

![consumer服务的运行控制台最终打印结果](./images/consumer服务的运行控制台最终打印结果.png)



<span style="color:red;">**踩坑！！！**</span>

案例中的publisher服务里的消息发送测试类的完整代码如下：

```java
package com.stone.mq.spring;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SpringAmqpTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    @RabbitListener(queuesToDeclare = {@Queue(value = "simple.queue", durable = "false")})
    public void testSendMessage2SimpleQueue() {
        String queueName = "simple.queue";
        String message = "hello, spring amqp";

        rabbitTemplate.convertAndSend(queueName, message);
    }

    @Test
    public void testSendMessage2WorkQueue() throws InterruptedException {
        String queueName = "simple.queue";
        String message = "hello, message__";
        for (int i = 0; i < 50; i++) {
            rabbitTemplate.convertAndSend(queueName, message + i);
            Thread.sleep(20);
        }
    }
}
```

启动运行后，consumer服务的控制台打印如下：

![Consumer服务的运行控制台打印结果问题](./images/Consumer服务的运行控制台打印结果问题.png)

但是**testSendMessage2SimpleQueue()**并没有被启动运行，怎么会向消息队列中发送消息呢？

==经过测试后，发现问题发生在**@RabbitListener**注解上！当该注解被开启后，consumer服务的控制台打印就会出现上图中的现象；当该注解被注释掉后，consumer服务的控制台打印结果是正常的。**根本原因：使用@RabbitListener注解的方法，会在消息到达队列时自动执行！！！**==

当**testSendMessage2WorkQueue()**方法向队列发送消息后，立即进入线程休眠，此时**@RabbitListener**注解监听到了队列中的新消息，便开始占用线程执行方法。

> 问：当一个方法调用线程休眠后, 添加@RabbitListener注解的方法会自动执行
>
> ——*来自AI的解释*
>
> 答：
>
> 在Spring AMQP（RabbitMQ的Spring支持）中，**@RabbitListener注解用于标记一个方法，该方法将作为消息监听器来处理从RabbitMQ队列接收到的消息**。这些监听器方法是由Spring的消息监听容器管理的，通常会在应用程序启动时就开始监听指定的队列，并且当有消息到达时异步地执行。
>
> 关于你提到的“方法调用线程休眠后”，这里需要明确几个关键点：
>
> ‌1.线程休眠‌：如果一个线程执行到某个点并调用如Thread.sleep()方法，该线程会被挂起指定的时间。这并不影响其他线程或进程的执行。
>
> ‌2.@RabbitListener的执行‌：@RabbitListener注解的方法是由Spring的消息监听容器异步调用的。这意味着它们的执行与任何特定线程的休眠状态无关。只要RabbitMQ队列中有消息，并且监听容器是活动的，那么这些方法就会在适当的时机被调用。
>
> ‌3.并发与线程管理‌：在Spring应用中，通常会有多个线程处理不同的任务，包括处理HTTP请求、执行定时任务、以及监听和处理消息队列中的消息等。线程休眠通常用于模拟长时间运行的任务或延迟处理，但它不会阻止其他线程或任务（如消息监听器）的执行。
>
> 综上所述，如果你有一个方法调用了线程休眠，这不会直接影响通过@RabbitListener注解的方法的执行。@RabbitListener注解的方法会在消息到达时被RabbitMQ的消息监听容器自动调用，而这个调用是异步的，与任何其他线程的休眠或活动状态无关。
>
> 如果你在实际应用中遇到了与预期不符的行为，可能需要检查以下几个方面：
>
> 确保RabbitMQ服务是运行中的，并且队列中有消息可供消费。
> 检查Spring配置，确保@RabbitListener注解的方法被正确注册为消息监听器。
> 检查是否有其他配置或代码逻辑影响了消息监听器的行为，比如消息确认机制、错误处理、或消息队列的配置等。



#### 发布订阅模型

发布订阅模型与前两个队列模型的区别在于：发布订阅模型允许将同一个消息发送给多个消费者，实现方式是加入了exchange（交换机）。

![发布订阅消息模型](./images/发布订阅消息模型.png)

常见的exchange类型包括：

- Fanout：广播
- Direct：路由
- Topic：话题

不同类型的exchange，它们的消息转发规则略有差异；

<span style="color:red;">但是注意</span>：不论是哪种类型的exchange，都只负责消息转发，而不是存储，转发失败则消息丢失。



##### Fanout Exchange

Fanout Exchange会将接收到的消息路由到每一个与其绑定的Queue

![FanoutExchange模型](./images/FanoutExchange模型.png)

==案例：通过Spring AMQP演示Fanout Exchange的使用==

步骤流程如下：

![FanoutExchange演示案例](./images/FanoutExchange演示案例.png)

1.在consumer服务中，利用代码声明队列、交换机，并将两者绑定

在consumer服务中创建**FanoutConfig**类，添加**@Configuration**注解，并声明FanoutExchange、Queue和绑定关系对象Binding：

```java
package com.stone.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FanoutConfig {

    // 声明itcast.fanout交换机
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange("itcast.fanout");
    }

    // 声明第1个队列
    @Bean
    public Queue fanoutQueue1() {
        return new Queue("fanout.queue1");
    }

    // 绑定第1个队列到交换机
    @Bean
    public Binding fanoutBinding1(Queue fanoutQueue1, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(fanoutQueue1).to(fanoutExchange);
    }

    // 声明第2个队列
    @Bean
    public Queue fanoutQueue2() {
        return new Queue("fanout.queue2");
    }

    // 绑定第2个队列到交换机
    @Bean
    public Binding fanoutBinding2(Queue fanoutQueue2, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(fanoutQueue2).to(fanoutExchange);
    }
}
```

运行consumer服务后可以在RabbitMQ的控制台页面可以看到对应的交换机、队列以及绑定关系：

![FanoutExchange交换机、队列及其绑定关系](./images/FanoutExchange交换机、队列及其绑定关系.png)



2.在consumer服务中，编写两个消费者方法，分别监听fanout.queue1和fanout.queue2

```java
package com.stone.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class SpringRabbitListener {

    //@RabbitListener(queues = "simple.queue")
    //public void listenSimpleQueue(String msg) {
    //    System.out.println("消费者接收到simple.queue的消息：【" + msg + "】");
    //}

    //@RabbitListener(queues = "simple.queue")
    //public void listenWorkQueue1(String msg) throws InterruptedException {
    //    System.out.println("消费者1接收到消息：【" + msg + "】 " + LocalTime.now());
    //    Thread.sleep(20);
    //}

    //@RabbitListener(queues = "simple.queue")
    //public void listenWorkQueue2(String msg) throws InterruptedException {
    //    // 使用err来区分两个方法中控制台打印输出的颜色
    //    System.err.println("消费者2......接收到消息：【" + msg + "】 " + LocalTime.now());
    //    Thread.sleep(100);
    //}

    @RabbitListener(queues = "fanout.queue1")
    public void listenFanoutQueue1(String msg) {
        System.out.println("消费者接收到fanout.queue1的消息：【" + msg + "】");
    }

    @RabbitListener(queues = "fanout.queue2")
    public void listenFanoutQueue2(String msg) {
        System.out.println("消费者接收到fanout.queue2的消息：【" + msg + "】");
    }
}
```



3.在publisher中编写测试方法，向itcast.fanout发送消息

```java
@Test
public void testSendMessage2FanoutExchange() {
    // 交换机名称
    String exchangeName = "itcast.fanout";
    // 路由键（用于将消息从交换机路由到特定队列。）
    String routingKey = "";
    // 消息内容
    String message = "hello, fanout exchange";
    // 发送消息
    rabbitTemplate.convertAndSend(exchangeName, routingKey, message);
}
```

执行该方法并查看consumer服务的运行控制台，可以看到两个队列都收到了交换机路由过来的消息：

![通过Fanout交换机发送消息_consumer服务的运行控制台](./images/通过Fanout交换机发送消息_consumer服务的运行控制台.png)



**总结**

1、交换机的作用

- 接收publisher服务发送的消息
- 将消息按照规则路由到预支绑定的队列
- 不能缓存消息，路由失败，消息丢失
- FanoutExchange会将消息路由到所有与之绑定的队列



##### Direct Exchange

Direct Exchange会将接收到的消息根据规则路由到**指定的Queue**，因此称为路由模式（routes）。

- 每一个Queue都会与Exchange设置一个**BindingKey**
- publisher在发送消息时，通过指定**RoutingKey**将消息发送到相应的队列
- Exchange会将消息路由到BindingKey与消息中的RoutingKey一致的队列

![DirectExchange模型](./images/DirectExchange模型.png)

==案例：利用Spring AMQP演示Direct Exchange的使用==

步骤流程如下：

![DirectExchange演示案例](./images/DirectExchange演示案例.png)

1.利用@RabbitListener声明Exchange、Queue、RoutingKey

2.在consumer服务中，编写两个消费者方法，分别监听direct.queue1和direct.queue2

```java
package com.stone.listener;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class SpringRabbitListener {
    
    //...略
    
    @RabbitListener(bindings = @QueueBinding(value = @Queue(name = "direct.queue1"),
            exchange = @Exchange(name = "itcast.direct", type = ExchangeTypes.DIRECT),
            key = {"red", "blue"}))
    public void listenDirectQueue1(String msg) {
        System.out.println("消费者接收到direct.queue1的消息：【" + msg + "】");
    }

    @RabbitListener(bindings = @QueueBinding(value = @Queue(name = "direct.queue2"),
            exchange = @Exchange(name = "itcast.direct", type = ExchangeTypes.DIRECT),
            key = {"red", "yellow"}))
    public void listenDirectQueue2(String msg) {
        System.out.println("消费者接收到direct.queue2的消息：【" + msg + "】");
    }
}
```

运行consumer服务后可以在RabbitMQ的控制台页面可以看到对应的交换机、队列以及绑定关系：

![DirectExchange交换机、队列及其绑定关系](./images/DirectExchange交换机、队列及其绑定关系.png)



3.在publisher中编写测试方法，向itcast.direct发送消息

```java
@Test
public void testSendMessage2DirectExchange() {
    // 交换机名称
    String exchangeName = "itcast.direct";
    // 路由键（用于将消息从交换机路由到特定队列。）
    String routingKey = "blue";
    // 消息内容
    String message = "hello, blue";
    // 发送消息
    rabbitTemplate.convertAndSend(exchangeName, routingKey, message);
}
```

执行该方法并查看consumer服务的运行控制台，可以看到只有绑定了blue键值的队列收到了交换机路由过来的消息：

![通过Direct交换机发送消息_consumer服务的运行控制台](./images/通过Direct交换机发送消息_consumer服务的运行控制台.png)



**总结**

1、Direct交换机与Fanout交换机的差异

- Fanout交换机将消息路由给每一个与之绑定的队列
- Direct交换机根据RoutingKey判断路由给哪些队列
- 如果多个队列具有相同的RoutingKey，则与Fanout功能类似



2、基于@RabbitListener注解声明队列和交换机时，有哪些常见的注解

- @Queue
- @QueueBinding
- @Exchange



##### Topic Exchange

TopicExchange与DirectExchange类似，<span style="color:blue;">区别在于routingKey必须是多个单词的列表，并且以==.==分割</span>。

Queue与Exchange指定BindingKey时，可以使用通配符：

<span style="color:red;">#：代指0个或多个单词</span>

<span style="color:red;">*：代指一个单词</span>

![TopicExchange模型](./images/TopicExchange模型.png)

==案例：利用SpringAMQP演示TopicExchange的使用==

![TopicExchange演示案例](./images/TopicExchange演示案例.png)

步骤流程如下：

1.利用@RabbitListener声明Exchange、Queue、RoutingKey

2.在consumer服务中，编写两个消费者方法，分别监听topic.queue1和topic.queue2

```java
package com.stone.listener;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class SpringRabbitListener {

    //...略

    @RabbitListener(bindings = @QueueBinding(value = @Queue(name = "topic.queue1"),
            exchange = @Exchange(name = "itcast.topic", type = ExchangeTypes.TOPIC),
            key = "china.#"))
    public void listenTopicQueue1(String msg) {
        System.out.println("消费者接收到topic.queue1的消息：【" + msg + "】");
    }

    @RabbitListener(bindings = @QueueBinding(value = @Queue(name = "topic.queue2"),
            exchange = @Exchange(name = "itcast.topic", type = ExchangeTypes.TOPIC),
            key = "#.news"))
    public void listenTopicQueue2(String msg) {
        System.out.println("消费者接收到topic.queue2的消息：【" + msg + "】");
    }
}
```

运行consumer服务后可以在RabbitMQ的控制台页面可以看到对应的交换机、队列以及绑定关系：

![TopicExchange交换机、队列及其绑定关系](./images/TopicExchange交换机、队列及其绑定关系.png)



3.在publisher中编写测试方法，向itcast.topic发送消息

```java
@Test
public void testSendMessage2TopicExchange() {
    // 交换机名称
    String exchangeName = "itcast.topic";
    // 路由键（用于将消息从交换机路由到特定队列。）
    String routingKey = "china.news";
    // 消息内容
    String message = "hello, topic exchange";
    // 发送消息
    rabbitTemplate.convertAndSend(exchangeName, routingKey, message);
}
```

执行该方法并查看consumer服务的运行控制台，可以看到两个队列都收到了消息：

![通过Topic交换机发送消息_consumer服务的运行控制台1](./images/通过Topic交换机发送消息_consumer服务的运行控制台1.png)

但如果我们将测试方法中的**routingKey**改成“china.weather”，则可以看到只有queue1队列收到了消息：

![通过Topic交换机发送消息_consumer服务的运行控制台2](./images/通过Topic交换机发送消息_consumer服务的运行控制台2.png)



#### 消息转换器

==案例：测试发送Object类型的消息==

说明：在Spring AMQP的发送方法中，消息参数的类型是**Object**，也就是说我们可以发送任意对象类型的消息，Spring AMQP会帮我们序列化为字节后发送。

1.为了方便观察，我们不再采用@RabbitListener注解来声明队列，而是采用Config类来声明

```java
package com.stone.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FanoutConfig {
    
    //...略
    
    // 声明一个队列，用于观察队列中的消息
    @Bean
    public Queue objectQueue() {
        return new Queue("object.queue");
    }
}
```

启动consumer服务后可以在页面控制台看到被声明出来的该队列：

![声明的Object队列](./images/声明的Object队列.png)



2.在publisher服务中编写测试方法，向object.queue发送Map类型的消息

```java
@Test
public void testSendMessage2ObjectQueue() {
    String queueName = "object.queue";
    Map<String, Object> message = new HashMap<>();
    message.put("name", "柳岩");
    message.put("age", 18);
    rabbitTemplate.convertAndSend(queueName, message);
}
```

执行该方法并在页面控制台中查看队列中的消息：

![object.queue队列中的消息](./images/object.queue队列中的消息.png)

可以看到，由于RabbitMQ只支持处理字节类型的消息，所以Spring默认会使用JDK的序列化对消息中的对象进行序列化处理，而这存在以下问题：

1、性能较差；2、安全性有问题（容易被注入）；3、序列化后的数据较长，网络传输速率较慢；



##### 自定义消息转换器

Spring对消息对象的处理是由**org.springframework.amqp.support.converter.MessageConverter**来完成的。

它的默认实现是**SimpleMessageConverter**，是基于JDK的ObjectOutputStream来完成序列化的。

如果要修改，只需要定义一个MessageConverter类型的Bean覆盖即可，<span style="color:red;">推荐使用JSON方式序列化</span>，**步骤如下：**

1.在父工程中引入依赖

```xml
<dependency>
    <groupId>com.fasterxml.jackson.dataformat</groupId>
    <artifactId>jackson-dataformat-xml</artifactId>
    <version>2.13.0</version>
</dependency>
```



2.在publisher服务中创建配置类并声明MessageConverter（放在启动类中也可）

```java
@Bean
public MessageConverter messageConverter() {
    return new Jackson2JsonMessageConverter();
}
```

在页面控制台将队列中的消息清空后（Purge Messages按钮），重新运行发送消息的方法并查看队列消息：

![object.queue队列中的消息2](./images/object.queue队列中的消息2.png)

可以看到序列化的方式变了，且消息体也简单可读。



##### 接收消息

publisher将消息发送成功后，还要处理consumer接收消息的问题

同样需要声明MessageConverter，然后定义一个消费者，监听object.queue队列并消费消息

```java
@RabbitListener(queues = "object.queue")
public void listenObjectQueue(Map<String, Object> msg) {
    System.out.println("消费者接收到object.queue的消息：【" + msg + "】");
}
```

重启consumer服务后，可以在运行控制台看到如下结果：

![consumer服务消费Object类型的消息](./images/consumer服务消费Object类型的消息.png)



## ElasticSearch

### 初识ElasticSearch

<span style="color:red;">**ElasticSearch**</span>是一款非常强大的开源搜索引擎，可以帮助我们从海量数据中快速找到需要的内容。

它的可应用场景包括但不限于：

- Github内容搜索并高亮显示关键字
- 京东商品信息搜索并高亮显示关键字
- 百度、谷歌搜索
- 打车软件上的附近车辆搜索



**ElasticSearch** 结合 **Kibana**、**Logstash**、**Beats** 组成了<span style="color:red;">ElasticStack（ELK）</span>。被广泛应用在日志数据分析、实时监控等领域。

==ElasticSearch是 ELK 的核心，负责存储、搜索、分析数据。==

![ELK](./images/ELK.png)



**ElasticSearch的发展**

ES的底层是基于 <span style="color:red;">**Lucene**</span> 实现的。

> Lucene是一个Java语言的搜索引擎类库，是Apache公司的顶级项目，由*DougCutting*于1999年研发。
>
> 官网地址：https://lucene.apache.org/
>
> Lucene的优势如下
>
> - 易扩展（类库相当于一个jar包，开发人员可以基于它进行二次开发）
> - 高性能（核心API实现了 <span style="color:red;">倒排索引</span> 数据结构）
>
> Lucene的劣势如下
>
> - 只限于Java语言开发
> - 学习曲线陡峭
> - 不支持水平扩展 

2004年*ShayBanon*基于**Lucene**开发了**Compass**

2010年*ShayBanon*重写了**Compass**，取名为<span style="color:red;">**ElasticSearch**</span>

官网地址：https://www.elastic.co/cn/

相比于Lucene，ElasticSearch具备下列优势：

- 支持分布式，可水平扩展
- 提供Restful接口，可被任何语言调用



### 倒排索引

#### 正向索引

传统数据库（如MySQL）采用==正向索引==，例如给下表（tb_goods）中的id创建索引：

![tb_goods表](./images/tb_goods表.png)

当我们需要对 title 进行模糊查询的时候，索引是不生效的

![模糊查询的执行逻辑](./images/模糊查询的执行逻辑.png)



#### 倒排索引

ElasticSearch采用倒排索引：

- 文档（document）：每条数据就是一个文档
- 词条（term）：文档按照语义分成的词语

![倒排索引_创建词条](D:\StoneSpace\myElasticSearch\images\倒排索引_创建词条.png)

基于倒排索引的查询是分两步执行的，但两步都是索引级别的查询，所以总的查询效率要比模糊匹配高很多

![基于倒排索引查询的执行逻辑](D:\StoneSpace\myElasticSearch\images\基于倒排索引查询的执行逻辑.png)

<span style="color:red;">由此可见，倒排索引是相对于正向索引而“倒”的</span>：正向索引是先找到文档，然后判断文档内容是否符合条件；倒排索引是先找到符合条件的词条，再找对应的文档



#### ES于MySQL的概念对比

##### 文档

ES是面向文档存储的，可以是数据库中的一条商品数据，一个订单信息。

<span style="color:#be002f;">文档数据会被序列化为json格式后存储在ES中。</span>

![ES的JSON存储形式](D:\StoneSpace\myElasticSearch\images\ES的JSON存储形式.png)



##### 索引

- 索引（Index）：<span style="color:blue;">相同类型的文档的集合</span>
- 映射（Mapping）：<span style="color:blue;">索引中文档的字段约束信息，类似表的结构约束</span>

![ES的索引](D:\StoneSpace\myElasticSearch\images\ES的索引.png)



##### ES与MySQL的概念对比

![ES与MySQL的概念对比](D:\StoneSpace\myElasticSearch\images\ES与MySQL的概念对比.png)

MySQL：擅长事物类型操作，可以确保数据的安全和一致性

ES：擅长海量数据的搜索、分析、计算

两者可以应用在同一架构中：

![ES和MySQL架构](D:\StoneSpace\myElasticSearch\images\ES和MySQL架构.png)



### 安装ES

<span style="color:red;">ES部分后续笔记在myElasticSearch中</span>

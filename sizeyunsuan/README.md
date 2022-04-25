# SpringBoot集成mybatis

[TOC]



## 介绍

Mybatis-Plus是一个优秀的Mybatis增强工具，在 MyBatis 的基础上只做增强不做改变，为简化开发、提高效率而生。

简单来说，Mybatis-Plus是Mybatis的增强工具包，其简化了CRUD操作，提供了代码生成器,强大的条件构造器(这是我最喜欢的一个)，同时内置了多个实用插件：标配的分页插件、性能分析插件、全局拦截插件等。使得开发过程中，基本的范式代码都一句话解决了，省去了很多重复的操作。我们将介绍SpringBoot2集成Mybatis-Plus，同时介绍mybatis提供`MysqlGenerator.java`，你可以通过指定的数据库表生成对应的`bean`、`mapper.xml`、`mapper.java`、`service.java`、`serviceImpl.java`，甚至`controller`

> mybatis-plus官网：https://mp.baomidou.com/

## 依赖引入

```xml
<!-- mysql驱动 -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <scope>runtime</scope>
</dependency>
<!-- MyBatis增强插件 -->
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>3.3.0</version>
</dependency>
<!-- mybatis plus 代码生成器依赖 -->
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-generator</artifactId>
    <version>3.3.0</version>
</dependency>
<!-- 代码生成器模板 -->
<dependency>
    <groupId>org.freemarker</groupId>
    <artifactId>freemarker</artifactId>
    <version>2.3.28</version>
</dependency>
<!-- lombok -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>
<!-- commons工具 -->
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-lang3</artifactId>
    <version>3.9</version>
</dependency>
```

## 配置mybatis-plus

```yaml
server:
  port: 8080

spring:
  datasource:
    hikari:
      connection-timeout: 30000
      max-lifetime: 1800000
      maximum-pool-size: 15
      minimum-idle: 5
      connection-test-query: select 1
      pool-name: HikariCP
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://jianml.cn:3306/mybatis?useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2b8
    username: root
    password: 123456

mybatis-plus:
  type-aliases-package: cn.jianml.mybatis.entity
  mapper-locations: classpath:mapper/*.xml
  configuration:
    jdbc-type-for-null: null
  global-config:
    banner: false # 关闭 mybatis-plus的 banner
```

## 添加`@MapperScan`注解

```java
@SpringBootApplication
@MapperScan("cn.jianml.mybatis.mapper")
public class MybatisApplication {

    public static void main(String[] args) {
        SpringApplication.run(MybatisApplication.class, args);
    }

}
```

### @MapperScan注解使用

#### 1、@Mapper注解：

作用：在接口类上添加了@Mapper，在编译之后会生成相应的接口实现类
添加位置：接口类上面

```java
@Mapper
public interface UserMapper {
  //代码
}
```

如果想要每个接口都要变成实现类，那么需要在每个接口类上加上@Mapper注解，比较麻烦，解决这个问题用@MapperScan



#### 2、@MapperScan

作用：指定要变成实现类的接口所在的包，然后包下面的所有接口在编译之后都会生成相应的实现类
添加位置：是在Springboot启动类上面添加，

```java
@SpringBootApplication
@MapperScan("cn.jianml.mybatis.mapper")
public class MybatisApplication {

  public static void main(String[] args) {
    SpringApplication.run(MybatisApplication.class, args);
  }
}
```

添加@MapperScan(“com.winter.dao”)注解以后，com.winter.dao包下面的接口类，在编译之后都会生成相应的实现类

#### 3、使用@MapperScan注解多个包

（实际用的时候根据自己的包路径进行修改）

```java
@SpringBootApplication
@MapperScan({"cn.jianml.mybatis.mapper","cn.jianml.system.mapper"})
public class MybatisApplication {

  public static void main(String[] args) {
    SpringApplication.run(MybatisApplication.class, args);
  }
}
```

4、 如果dao接口类没有在SpringBoot主程序可以扫描的包或者子包下面，可以使用如下方式进行配置：

```java
@SpringBootApplication
@MapperScan("cn.jianml.*.mapper")
public class MybatisApplication {

  public static void main(String[] args) {
    SpringApplication.run(MybatisApplication.class, args);
  }
}
```

## 配置分页

添加配置文件，此处配置文件表示开启mybatis-plus分页功能

```java
@Configuration
public class MybatisPlusConfig {

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

}
```

## 创建数据库表

```sql
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`  (
  `USER_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `USERNAME` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `PASSWORD` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `SSEX` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '性别',
  `CREATE_TIME` datetime(0) NOT NULL COMMENT '创建时间',
  `MODIFY_TIME` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `LAST_LOGIN_TIME` datetime(0) NULL DEFAULT NULL COMMENT '最近访问时间',
  PRIMARY KEY (`USER_ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;
```



## 代码生成器

`AutoGenerator`是 MyBatis-Plus 的代码生成器，通过 AutoGenerator 可以快速生成 Entity、Mapper、Mapper XML、Service、Controller 等各个模块的代码，极大的提升了开发效率。

### 配置模板

MyBatis-Plus 支持 Velocity（默认）、Freemarker、Beetl等模板引擎，用户可以选择自己熟悉的模板引擎，如果都不满足您的要求，可以采用自定义模板引擎。

在resources/generator/templates 下创建模板，模板目录如下

![mark](http://image.jianml.cn/image/20200109/M8OLpKhM2AC6.png)

### 配置代码生成器`CodeGenerator`





运行 CodeGenerator 会发现，我么需要的 entity、mapper、service、controller 都有了，而且mybatis-plus 为我们封装了很对常用的方法 ，大大的提到了我们的开发效率

## 创建实体类

```java
@Data
public class User {

    // 性别男
    public static final String SEX_MALE = "0";
    // 性别女
    public static final String SEX_FEMALE = "1";
    // 性别保密
    public static final String SEX_UNKNOW = "2";

    /**
     * 用户 ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 性别 0男 1女 2 保密
     */
    private String sex;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 最近访问时间
     */
    private Date lastLoginTime;
}
```

## 创建mapper接口

```java
@Repository
public interface UserMapper extends BaseMapper<User> {

    /**
     * 通过用户名查找用户
     *
     * @param username 用户名
     * @return 用户
     */
    User findByName(String username);

    /**
     * 查找用户详细信息
     *
     * @param page 分页对象
     * @param user 用户对象，用于传递查询条件
     * @return Ipage
     */
    IPage<User> findUserDetailPage(Page page, @Param("user") User user);

    /**
     * 查找用户详细信息
     *
     * @param user 用户对象，用于传递查询条件
     * @return List<User>
     */
    List<User> findUserDetail(@Param("user") User user);
}
```

## 创建Mapper映射配置文件

```xml

```



## 创建service层类

```java
public interface IUserService extends IService<User> {

    /**
     * 通过用户名查找用户
     *
     * @param username 用户名
     * @return 用户
     */
    User findByName(String username);

    /**
     * 查找用户详细信息
     *
     * @param request request
     * @param user    用户对象，用于传递查询条件
     * @return IPage
     */
    IPage<User> findUserDetailList(User user, QueryRequest request);

    /**
     * 通过用户名查找用户详细信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    User findUserDetailList(String username);

    /**
     * 更新用户登录时间
     *
     * @param username 用户名
     */
    void updateLoginTime(String username);

    /**
     * 新增用户
     *
     * @param user user
     */
    void createUser(User user);

    /**
     * 删除用户
     *
     * @param userIds 用户 id数组
     */
    void deleteUsers(String[] userIds);

    /**
     * 修改用户
     *
     * @param user user
     */
    void updateUser(User user);

    /**
     * 重置密码
     *
     * @param usernames 用户名数组
     */
    void resetPassword(String[] usernames);

    /**
     * 注册用户
     *
     * @param username 用户名
     * @param password 密码
     */
    void regist(String username, String password);

    /**
     * 修改密码
     *
     * @param password 新密码
     */
    void updatePassword(String password);
}
```

UserService继承了ServiceImpl类,mybatis-plus通过这种方式为我们注入了UserMapper,这样可以使用service层默认为我们提供的很多方法,也可以调用我们自己在dao层编写的操作数据库的方法。Page类是mybatis-plus提供分页功能的一个model,继承了Pagination,这样我们也不需要自己再编写一个Page类,直接使用即可

## 创建controller层类

```java
@RestController
public class UserController {

    private IUserService userService;

    @GetMapping("{username}")
    public User getUser(@PathVariable String username) {
        return this.userService.findUserDetailList(username);
    }

    @GetMapping("check/{username}")
    public boolean checkUserName(@PathVariable String username, String userId) {
        return this.userService.findByName(username) == null || StringUtils.isNotBlank(userId);
    }

//    @GetMapping("list")
//    public FebsResponse userList(User user, QueryRequest request) {
//        Map<String, Object> dataTable = getDataTable(this.userService.findUserDetailList(user, request));
//        return new FebsResponse().success().data(dataTable);
//    }

    @PostMapping
    public void addUser(User user) {
        this.userService.createUser(user);
    }

    @GetMapping("delete/{userIds}")
    public void deleteUsers(@PathVariable String userIds) {
        String[] ids = userIds.split(",");
    }

    @PostMapping("update")
    public void updateUser(User user) {
        this.userService.updateUser(user);
    }

    @PostMapping("password/reset/{usernames}")
    public void resetPassword(@PathVariable String usernames) {
        String[] usernameArr = usernames.split(",");
        this.userService.resetPassword(usernameArr);
    }

    @PutMapping("password")
    public void updatePassword(String password) {
        userService.updatePassword(password);
    }

}
```

##  SQL 分析打印

### p6spy 依赖引入

```xml
<!-- SQL 分析打印 -->
<dependency>
    <groupId>p6spy</groupId>
    <artifactId>p6spy</artifactId>
    <version>3.8.7</version>
</dependency>
```

### application.yml 配置

应用`P6Spy`只需要

- 替换你的`JDBC Driver`为`com.p6spy.engine.spy.P6SpyDriver`
- 修改`JDBC Url`为`jdbc:p6spy:xxxx`
- 配置`spy.properties`

```yaml
# 数据库的配置信息
spring:
  datasource:
    hikari:
      connection-timeout: 30000
      max-lifetime: 1800000
      maximum-pool-size: 15
      minimum-idle: 5
      connection-test-query: select 1
      pool-name: HikariCP
#    driver-class-name: com.mysql.cj.jdbc.Driver
#    url: jdbc:mysql://jianml.cn:3306/mybatis?useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2b8
    driver-class-name: com.p6spy.engine.spy.P6SpyDriver
    url: jdbc:p6spy:mysql://jianml.cn:3306/mybatis?useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2b8
    username: root
    password: 123456
```

### 配置`spy.properties`

```properties
# p6spy配置，文档 https://p6spy.readthedocs.io/en/latest/configandusage.html
# 使用日志系统记录 sql
appender=com.p6spy.engine.spy.appender.Slf4JLogger
# 自定义日志打印
logMessageFormat=cn.jianml.mybatis.config.P6spySqlFormatConfig
# 是否开启慢 SQL记录
outagedetection=true
# 慢 SQL记录标准 2 秒
outagedetectioninterval=2
# 实际驱动
driverlist=com.mysql.cj.jdbc.Driver
```

### 自定义日志打印

实现`MessageFormattingStrategy`接口

```java
public class P6spySqlFormatConfig implements MessageFormattingStrategy {

    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {
        return !"".equals(sql.trim()) ? "[ " + LocalDateTime.now() + " ] --- | took "
                + elapsed + "ms | " + category + " | connection " + connectionId + "\n "
                + sql + ";" : "";
    }
}
```

> 源码地址：https://gitee.com/jianml/mybatis
# Spring第2天

## 复习：XML方式的IoC

### IoC

* IoC：控制反转，用于解耦的
* 如何解耦：我们需要一个对象，Spring帮我们创建好，交给我们使用

### 创建bean

* Spring默认通过无参构造

```xml
<bean id="唯一标识" class="全限定类名"></bean>
```

* Spring通过静态工厂方法创建

```xml
<bean id="唯一标识" class="静态工厂类的全限定类名" factory-method="静态方法"></bean>
```

* Spring通过实例化工厂方法创建

```xml
<bean id="唯一标识" factory-bean="工厂bean对象" factory-method="非静态方法"></bean>
```

### 配置bean

* 配置bean的作用范围：通过bean标签的`scope`属性配置的
  * singleton：单例的。一个Spring容器里只有一个该bean的对象
    * 何时创建：Spring容器初始化时
    * 何时销毁：Spring容器关闭时
  * prototype：多例的。一个Spring容器里有多个该bean的对象。Spring创建之后，把bean对象交给jvm管理
    * 何时创建：获取bean对象时
    * 何时销毁：长时间不用，垃圾回收
* 生命周期的方法
  * bean标签`init-method`属性：指定一个初始化方法。会在bean对象被创建时执行一次
  * bean标签`destroy-method`属性：指定一个销毁方法。会在bean对象被销毁时执行一次（prototype除外）

### 依赖注入

* set方法注入

  * 要求：bean类里依赖项要提供set方法
  * 配置：使用bean标签的子标签`property`标签配置

  ```xml
  <bean>
  	<property name="属性名" value="简单值"/>
      <property name="属性名" ref="其它bean的id"/>
  </bean>
  ```

* 构造方法注入

  * 要求：bean类要提供有参构造方法，每个构造参数就是一个依赖项
  * 配置：使用bean标签的子标签`constructor-arg`标签配置

  ```xml
  <bean>
  	<constructor-arg name="构造参数名称" value="简单值"/>
      <constructor-arg name="构造参数名称" ref="其它bean的id"/>
  </bean>
  ```

* p名称空间注入（本质是set方法注入）

  * 要求：bean类里依赖项要提供set方法
  * 配置：
    * 引入p名称空间的约束

  ```xml
  <bean id="" class="" p:属性名="简单值" p:属性名-ref="其它bean的id"></bean>
  ```

  

## 一、xml方式CURD练习

### 1. 需求描述

* 完成帐户信息的增、删、改、查操作，要求使用Spring对service层和dao层解耦

### 2. 需求分析

#### 2.1 dao层技术

##### 1) DBUtils简介

* dao层操作数据库，采用Apache的DBUtils工具包
* DBUtils：类似于JDBCTemplate，是对JDBC的简单封装，用于简化JDBC操作数据库

##### 2) DBUtils使用步骤

1. 导入jar包依赖：

   ```xml
   <dependency>
       <groupId>commons-dbutils</groupId>
       <artifactId>commons-dbutils</artifactId>
       <version>1.7</version>
   </dependency>
   ```

2. 编写代码：
   1. 创建DBUtils的核心类`QueryRunner`对象
      * 构造方法：`QueryRunner(DataSource ds)`
   2. 使用QueryRunner的方法操作数据库，常用方法有
      * 执行DML语句：`update(sql, params)`，返回int表示影响行数
      * 执行DQL语句：`query(sql, ResultSetHandler rh, argValues)`
        * 返回值由ResultHandler的实现类对象决定
          * `BeanHandler`：返回一个JavaBean对象
          * `BeanListHandler`：返回一个JavaBean集合
          * `ScalarHandler`：返回一个值

#### 2.2 实现步骤分析

1. 准备工作：
   * 创建Maven的Java项目，配置坐标，引入依赖
   * 创建JavaBean
2. 编写代码：
   * 创建service和dao的接口和实现类，并添加上：查询全部、添加帐号、修改帐号、删除帐号的功能
3. 配置文件：
   * 创建Spring核心配置文件，配置所有的bean
4. 测试
   * 创建单元测试类，测试功能是否正常

### 3. 需求实现

#### 3.1 准备工作

1. 创建Maven的Java项目，项目坐标自定，然后引入依赖如下：

   ```xml
   <dependencies>
       <!-- 数据库驱动 -->
       <dependency>
           <groupId>mysql</groupId>
           <artifactId>mysql-connector-java</artifactId>
           <version>5.1.47</version>
       </dependency>
       
       <!-- c3p0连接池（也可以用其它连接池） -->
       <dependency>
           <groupId>com.mchange</groupId>
           <artifactId>c3p0</artifactId>
           <version>0.9.5.2</version>
       </dependency>
       
       <!-- DBUtils工具包 -->
       <dependency>
           <groupId>commons-dbutils</groupId>
           <artifactId>commons-dbutils</artifactId>
           <version>1.7</version>
       </dependency>
       
       <!-- Spring -->
       <dependency>
           <groupId>org.springframework</groupId>
           <artifactId>spring-context</artifactId>
           <version>5.1.2.RELEASE</version>
       </dependency>
       
       <!-- 单元测试 -->
       <dependency>
           <groupId>junit</groupId>
           <artifactId>junit</artifactId>
           <version>4.12</version>
       </dependency>
   </dependencies>
   ```

2. 创建JavaBean：Account类如下：

   ```java
   public class Account {
       private Integer id;
       private String name;
       private Float money;
   
       public Integer getId() {
           return id;
       }
   
       public void setId(Integer id) {
           this.id = id;
       }
   
       public String getName() {
           return name;
       }
   
       public void setName(String name) {
           this.name = name;
       }
   
       public Float getMoney() {
           return money;
       }
   
       public void setMoney(Float money) {
           this.money = money;
       }
   
       @Override
       public String toString() {
           return "Account{" +
                   "id=" + id +
                   ", name='" + name + '\'' +
                   ", money=" + money +
                   '}';
       }
   }
   ```


#### 3.2 编写代码

##### 1)  Service层代码如下：

1. Service层接口：`AccountService`

   ```java
   public interface AccountService {
       List<Account> queryAll() throws SQLException;
       
       void save(Account account) throws SQLException;
       
       void edit(Account account) throws SQLException;
       
       void delete(Integer id) throws SQLException;
   }
   ```

2. Service实现类：`AccountServiceImpl`

   ```java
   public class AccountServiceImpl implements AccountService {
   
       private AccountDao accountDao;
   
       /***************业务功能方法*****************/
       
       @Override
       public List<Account> queryAll() throws SQLException {
           return accountDao.queryAll();
       }
       
       @Override
       public void save(Account account) throws SQLException {
           accountDao.save(account);
       }
       
       @Override
       public void edit(Account account) throws SQLException {
           accountDao.edit(account);
       }
       
       @Override
       public void delete(Integer id) throws SQLException {
           accountDao.delete(id);
       }
   
       
       /***************get/set方法*****************/
       public AccountDao getAccountDao() {
           return accountDao;
       }
   
       public void setAccountDao(AccountDao accountDao) {
           this.accountDao = accountDao;
       }
   }
   ```

##### 2)  dao层代码如下：

1. dao层接口：`AccountDao`

   ```java
   public interface AccountDao {
       List<Account> queryAll() throws SQLException;
       
       void save(Account account) throws SQLException;
       
       void edit(Account account) throws SQLException;
       
       void delete(Integer id) throws SQLException;
   }
   ```

2. dao实现类：`AccountDaoImpl`

   ```java
   public class AccountDaoImpl implements AccountDao {
   
       private QueryRunner runner;
   
       /***************功能方法*****************/
       
       @Override
       public List<Account> queryAll() throws SQLException {
           return runner.query("select * from account", new BeanListHandler<>(Account.class));
       }
       
       public void save(Account account) throws SQLException{
           runner.update("insert into account (id,name,money) values (?,?,?)", account.getId(), account.getName(), account.getMoney());
       }
       
       public void edit(Account account) throws SQLException{
           runner.update("update account set name = ?, money = ? where id = ?", account.getName(), account.getMoney(), account.getId());
       }
       
       public void delete(Integer id) throws SQLException{
           runner.update("delete from account where id = ?", id);
       }
       
       /***************get/set方法*****************/
   
       public QueryRunner getRunner() {
           return runner;
       }
   
       public void setRunner(QueryRunner runner) {
           this.runner = runner;
       }
   }
   ```

#### 3.3 提供配置

1. 创建Spring的核心配置文件：`applicationContext.xml`

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <beans xmlns="http://www.springframework.org/schema/beans"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://www.springframework.org/schema/beans
          http://www.springframework.org/schema/beans/spring-beans.xsd">
   
       <bean id="accountService" class="com.itheima.service.impl.AccountServiceImpl">
           <property name="accountDao" ref="accountDao"/>
       </bean>
   
       <bean id="accountDao" class="com.itheima.dao.impl.AccountDaoImpl">
           <property name="runner" ref="runner"/>
       </bean>
   
       <bean id="runner" class="org.apache.commons.dbutils.QueryRunner">
           <constructor-arg name="ds" ref="dataSource"/>
       </bean>
   
       <bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
           <property name="driverClass" value="com.mysql.jdbc.Driver"/>
           <property name="jdbcUrl" value="jdbc:mysql:///spring"/>
           <property name="user" value="root"/>
           <property name="password" value="root"/>
       </bean>
   </beans>
   ```

#### 3.4 功能测试

1. 编写单元测试类`AccountTest`如下：

   ```java
   public class AccountTest {
   
       private AccountService accountService;
   
       @Before
       public void before(){
           ApplicationContext context = 
               new ClassPathXmlApplicationContext("applicationContext.xml");
           accountService = context.getBean("accountService", AccountService.class);
       }
   
       @Test
       public void testQueryAll() throws SQLException {
           List<Account> accounts = accountService.queryAll();
           for (Account account : accounts) {
               System.out.println(account);
           }
       }
       
       @Test
       public void testSave() throws SQLException {
           Account account = new Account();
           account.setName("tom");
           account.setMoney(10000f);
           accountService.save(account);
       }
       
       @Test
       public void testEdit() throws SQLException {
       	Account account = new Account();
           account.setId(3);
           account.setName("jerry");
           account.setMoney(5000f);
           accountService.edit(account);    
       }
       
       @Test
       public void testDelete() throws SQLException {
           accountService.delete(3);
       }
   }
   ```

### 4. 补充

#### 4.1 引入`properties`文件

如果需要在`applicationContext.xml`中引入properties文件：

- 准备一个properties文件放在resources里：`jdbc.properties`

```properties
jdbc.driver=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql:///spring
jdbc.username=root
jdbc.password=root
```

- 在`applicationContext.xml`中引入并使用`jdbc.properties`
  - Spring的名称空间：`context`

```xml
<beans
       xmlns:名称空间="http://www.springframework.org/schema/名称空间"
       xsi:scehmaLocation="
          http://www.springframework.org/schema/名称空间
         http://www.springframework.org/schema/名称空间/spring-名称空间.xsd">
</beans>
```

* 使用context名称空间提供的标签，引入外部的properties文件

```xml
<!-- 注意：需要引入context名称空间，才可以使用这个标签 -->
<context:property-placeholder location="classpath:jdbc.properteis"/>

<bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
    <!-- 使用properties中的数据 -->
    <property name="driverClass" value="${jdbc.driver}"/>
    <property name="jdbcUrl" value="${jdbc.url}"/>
    <property name="user" value="${jdbc.username}"/>
    <property name="password" value="${jdbc.password}"/>
</bean>
```

#### 4.2 分模块提供配置文件

在大型项目开发中，如果把所有的配置都写在一个配置文件`applicationContext.xml`中，会导致：

- 配置文件过于臃肿
- 不利于分模块开发，不利于模块之间的解耦

Spring提供了分模块配置的方式，即：每个模块提供一个配置文件，在核心配置文件中引入模块配置：

```xml
<import resource="applicationContext-xx.xml"/>
<import resource="applicationContext-yy.xml"/>
```

### 5. 小结



## 二、基于注解的IoC（原始注解，重点）

### 1. 快速入门（重点）

#### 需求描述

- 有dao层：`UserDao`和`UserDaoImpl`
- 有service层：`UserService`和`UserServiceImpl`
- 使用注解配置bean，并注入依赖

#### 需求分析

1. 准备工作：

   创建Maven项目，导入依赖坐标

2. 编写代码并注解配置：

   编写dao层、service层代码，

   使用注解`@Component`配置bean

   使用注解`@Autowired`依赖注入

3. 配置注解扫描

   在xml中引入context名称空间约束

   开启 组件的注解扫描

4. 测试

#### 需求实现

##### 1) 准备工作

- 创建Maven项目，导入依赖坐标

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-context</artifactId>
        <version>5.1.2.RELEASE</version>
    </dependency>
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.12</version>
    </dependency>
</dependencies>
```

##### 2) 编写代码，并注解配置

- `UserDao`接口

```java
public interface UserDao {
    void save();
}
```

- `UserDaoImpl`实现类

```java
@Component("userDao")//等价于@Repository("userDao")
public class UserDaoImpl implements UserDao {
    @Override
    public void save() {
        System.out.println("save......");
    }
}
```

- `UserService`接口

```java
public interface UserService {
    void save();
}
```

- `UserServiceImpl`实现类

```java
@Component("userService")//等价于@Service("userService")
public class UserServiceImpl implements UserService {

    //注意：使用注解配置依赖注入时，不需要再有set方法
    @Autowired
    //@Qualifier("userDao")
    private UserDao userDao;

    @Override
    public void save() {
        userDao.save();
    }
}
```

##### 3) 配置注解扫描

- 创建`applicationContext.xml`，注意引入的`context`名称空间

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       http://www.springframework.org/schema/context/spring-context.xsd">

    <!--开启组件扫描-->
    <context:component-scan base-package="com.itheima"/>
</beans>
```

#### 功能测试

- 创建一个测试类，模拟web层调用Service

```java
public class UserTest {

    @Test
    public void save(){
        ApplicationContext app = new ClassPathXmlApplicationContext("applicationContext.xml");
        UserService userService = (UserService) app.getBean("userService");
        userService.save();
    }
}
```

#### 小结

1. 创建Maven项目，导入依赖

2. 创建Service和dao的接口、类

3. 在每个类上增加注解`@Component`，把这个类声明成为bean对象

4. 在需要注入的依赖项上增加注解`@Autowired`

5. 创建xml配置文件，开启组件扫描

   ```xml
   <context:component-scan base-package="com.itheima"/>
   ```

   

### 2. 常用注解简介

#### 2.1 注解介绍

**声明bean对象的注解**

| 注解          | 说明                                                        |
| ------------- | ----------------------------------------------------------- |
| `@Component`  | 用在类上，相当于bean标签                                    |
| `@Controller` | 用在web层类上，配置一个bean（是`@Component`的衍生注解）     |
| `@Service`    | 用在service层类上，配置一个bean（是`@Component`的衍生注解） |
| `@Repository` | 用在dao层类上，配置一个bean（是`@Component`的衍生注解）     |

**配置bean对象的注解**

| 注解             | 说明                               |
| ---------------- | ---------------------------------- |
| `@Scope`         | 相当于bean标签的scope属性          |
| `@PostConstruct` | 相当于bean标签的init-method属性    |
| `@PreDestroy`    | 相当于bean标签的destory-method属性 |

**依赖注入的注解**

| 注解         | 说明                                       |
| ------------ | ------------------------------------------ |
| `@Autowired` | 相当于property标签的ref                    |
| `@Qualifier` | 结合`@Autowired`使用，用于根据名称注入依赖 |
| `@Resource`  | 相当于`@Autowired + @Qualifier`            |
| `@Value`     | 相当于property标签的value                  |

#### 2.2 配置开启组件扫描

* 在Spring中如果要使用注解开发，就需要在`applicationContext.xml`中开启注解扫描，配置如下：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        http://www.springframework.org/schema/context/spring-context.xsd">

    <!-- 注意：必须要引入context名称空间之后，才可以使用这个标签 -->
    <!-- 开启注解扫描，让Spring容器扫描com.itheima包下的注解 -->
    <context:component-scan base-package="com.itheima"/>
    
</beans>
```

### 3. 注解使用详解

#### 3.1 声明bean的注解

* `@Component`：类级别的一个注解，用于声明一个bean，使用不多
  * `value`属性：bean的唯一标识。如果不配置，默认以首字母小写的变量名为id
* `@Controller, @Service, @Repository`，作用和`@Component`完全一样，但更加的语义化，使用更多
  * `@Controller`：用于web层的bean
  * `@Service`：用于Service层的bean
  * `@Repository`：用于dao层的bean

#### 3.2 依赖注入的注解

##### 注入bean对象

* `@Autowired`：用于注入bean对象(byType注入)
  * 按依赖类型，从Spring容器中查找要注入的bean对象
    * 如果找到一个，直接注入
    * 如果找到多个，则以变量名为id，查找bean对象并注入（byName注入）
    * 如果找不到，抛异常
* `@Qualifier`：是按id注入，但必须和`@Autowired`配合使用。
* `@Resource`：(是jdk提供的)用于注入bean对象(byName注入)，相当于`@Autowired + @Qualifier`

> 绝大多数情况下，只要使用`@Autowired`注解注入即可
>
> 注解注入时，不需要set方法了

##### 注入普通值

* `@Value`：注入简单类型值，例如：基本数据类型和String

```java
@Service("userService")
public class UserServiceImpl implements UserService{
    
    @Value("zhangsan")//直接注入字符串值
    private String name;
    
    //从properties文件中找到key的值，注入进来
    //注意：必须在applicationContext.xml中加载了properties文件才可以使用
    @Value("${properties中的key}")
    private String abc;
    
    //...
}
```

#### 3.3 配置bean的注解

* `@Scope`：配置bean的作用范围，相当于bean标签的scope。常用值有：

  * `singleton`：单例的，容器中只有一个该bean对象
    * 何时创建：容器初始化时
    * 何时销毁：容器关闭时
  * `prototype`：多例的，每次获取该bean时，都会创建一个bean对象
    * 何时创建：获取bean对象时
    * 何时销毁：长时间不使用，垃圾回收

  ```java
  @Scope("prototype")
  @Service("userService")
  public class UserServiceImpl implements UserService{
      //...
  }
  ```

* `@PostConstruct`和`@PreDestroy`：是方法级别的注解，用于指定bean的初始化方法和销毁方法

  ```java
  @Service("userService")
  public class UserServiceImpl implements UserService {
  
      @PostConstruct
      public void init(){
          System.out.println("UserServiceImpl对象已经创建了......");
      }
      
      @PreDestroy
      public void destroy(){
          System.out.println("UserServiceImpl对象将要销毁了......");
      }
   
      //......
  }
  ```

### 4. 注解方式CURD练习

#### 4.1 需求描述

* 使用注解开发帐号信息的CURD功能

#### 4.2 需求分析

* 使用注解代替某些XML配置，能够代替的有：
  * dao层bean对象，可以在类上增加注解`@Repository`
  * service层bean对象，可以在类上增加注解`@Service`
  * Service层依赖于dao层，可以使用注解注入依赖`@AutoWired`
* 不能使用注解代替，仍然要使用XML配置的的有：
  * QueryRunner的bean对象，是DBUtils工具包里提供的类，我们不能给它的源码上增加注解
  * 连接池的bean对象，是c3p0工具包里提供的类，我们不能修改源码增加注解

#### 4.3 需求实现

##### 1)  JavaBean

```java
public class Account {
    private Integer id;
    private String name;
    private Double money;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", money=" + money +
                '}';
    }
}
```

##### 2)  dao层代码

* `AccountDao`接口

```java
public interface AccountDao {
    Account findById(Integer id) throws SQLException;

    List<Account> queryAll() throws SQLException;

    void save(Account account) throws SQLException;

    void edit(Account account) throws SQLException;

    void delete(Integer id) throws SQLException;
}
```

* `AccountDaoImpl`实现类

```java
@Repository("accountDao")
public class AccountDaoImpl implements AccountDao {
    
    @Autowired
    private QueryRunner runner;
    
    @Override
    public Account findById(Integer id) throws SQLException {
        return runner.query("select * from account where id = ?", new BeanHandler<>(Account.class), id);
    }

    @Override
    public List<Account> queryAll() throws SQLException {
        return runner.query("select * from account", new BeanListHandler<>(Account.class));
    }

    @Override
    public void save(Account account) throws SQLException {
        runner.update("insert into account(id,name,money) values (?,?,?)", account.getId(), account.getName(), account.getMoney());
    }

    @Override
    public void edit(Account account) throws SQLException {
        runner.update("update account set name = ?, money = ? where id = ?", account.getName(), account.getMoney(), account.getId());
    }

    @Override
    public void delete(Integer id) throws SQLException {
        runner.update("delete from account where id = ?", id);
    }
}
```

##### 3)  service层代码

* `AccountService`接口

```java
public interface AccountService {
    Account findById(Integer id) throws SQLException;

    List<Account> queryAll() throws SQLException;

    void save(Account account) throws SQLException;

    void edit(Account account) throws SQLException;

    void delete(Integer id) throws SQLException;
}
```

* `AccountServiceImpl`接口

```java
@Service("accountService")
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountDao accountDao;

    @Override
    public Account findById(Integer id) throws SQLException {
        return accountDao.findById(id);
    }

    @Override
    public List<Account> queryAll() throws SQLException {
        return accountDao.queryAll();
    }

    @Override
    public void save(Account account) throws SQLException {
        accountDao.save(account);
    }

    @Override
    public void edit(Account account) throws SQLException {
        accountDao.edit(account);
    }

    @Override
    public void delete(Integer id) throws SQLException {
        accountDao.delete(id);
    }
}
```

##### 4)  提供配置

* 创建`applicationContext.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       http://www.springframework.org/schema/context/spring-context.xsd">

    <!--开启注解扫描-->
    <context:component-scan base-package="com.itheima"/>

    <!--配置QueryRunner-->
    <bean id="runner" class="org.apache.commons.dbutils.QueryRunner">
        <constructor-arg name="ds" ref="dataSource"/>
    </bean>
    <!--配置连接池-->
    <bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
        <property name="driverClass" value="com.mysql.jdbc.Driver"/>
        <property name="jdbcUrl" value="jdbc:mysql:///spring"/>
        <property name="user" value="root"/>
        <property name="password" value="root"/>
    </bean>
</beans>
```

#### 4.4  功能测试

```java
public class AccountTest {

    @Test
    public void queryAll() throws SQLException {
        ApplicationContext app = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        AccountService accountService = app.getBean("accountService", AccountService.class);
        List<Account> accounts = accountService.queryAll();
        for (Account account : accounts) {
            System.out.println(account);
        }
    }

    @Test
    public void findById() throws SQLException {
        ApplicationContext app = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        AccountService accountService = app.getBean("accountService", AccountService.class);
        Account account = accountService.findById(2);
        System.out.println(account);
    }

    @Test
    public void save() throws SQLException {
        ApplicationContext app = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        AccountService accountService = app.getBean("accountService", AccountService.class);

        Account account = new Account();
        account.setName("jerry");
        account.setMoney(2000d);
        accountService.save(account);
    }

    @Test
    public void edit() throws SQLException {
        ApplicationContext app = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        AccountService accountService = app.getBean("accountService", AccountService.class);

        Account account = new Account();
        account.setId(2);
        account.setName("tom");
        account.setMoney(10000d);

        accountService.edit(account);
    }

    @Test
    public void delete() throws SQLException {
        ApplicationContext app = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        AccountService accountService = app.getBean("accountService", AccountService.class);

        accountService.delete(3);
    }
}
```

### 5. 小结

* 声明bean的注解
  * `@Component`：声明成bean对象，加在类上`@Component("bean的名称")`
  * `@Controller`：把web层的类声明成为bean对象
  * `@Service`：把service层的类声明成为bean对象
  * `@Repository`：把dao层的类声明成为bean对象
* 依赖注入的注解
  * `@Autowired`：byType注入
    * 如果只找到一个，直接注入
    * 如果找到多个，byName注入
    * 如果找不到，抛异常
  * `@Qualifier`：和`@Autowired`配合使用，用于byName注入
  * `@Resource`：JDK提供的注解，相当于`@Autowired` + `@Qualifier`
* 配置bean的注解
  * `@Scope`：用于配置bean的作用范围
    * `singleton`：单例的，默认的
      * 何时创建：Spring容器初始化时
      * 何时销毁：Spring容器关闭时
      * 整个Spring容器里只有一个该bean的对象
    * `prototype`：多例的
      * 何时创建：获取bean对象时
      * 何时销毁：长时间不用，垃圾回收


## 三、纯注解开发IoC（新注解）

​	在上边的CURD练习中，仍然有部分配置需要使用`applicationContext.xml`，那么能不能使用注解替换掉所有的xml呢？Spring提供了一些新注解，可以达到这个目标。

> 请注意：Spring提供的这部分新注解，并非为了完全代替掉XML，只是提供了另外一种配置方案

### 1. 注解简介

| 注解              | 说明                                                         |
| ----------------- | ------------------------------------------------------------ |
| `@Configuration`  | 被此注解标记的类，是配置类                                   |
| `@ComponentScan`  | 用在配置类上，开启注解扫描。使用basePackage属性指定扫描的包  |
| `@PropertySource` | 用在配置类上，加载properties文件。使用value属性指定properties文件路径 |
| `@Bean`           | 用在配置类的方法上，把返回值声明为一个bean。用name/value属性指定bean的id |
| `@Import`         | 用在配置类上，引入子配置类。用value属性指定子配置类的Class   |

### 2. 注解详解

#### 2.1 配置类上的注解

* `@Configuration`把一个Java类声明为核心配置类
  * 加上Java类上，这个Java类就成为了Spring的核心配置类，用于代替`applicationContext.xml`
  * 是`@Component`的衍生注解，所以：核心配置类也是bean，里边可以注入依赖
* `@ComponentScan`配置组件注解扫描
  * `basePackages`属性：指定扫描的基本包
* `@PropertySource`用于加载properties文件
  * `value`属性：指定propertis文件的路径，从类加载路径里加载
* `@Import`用于导入其它配置类
* Spring允许提供多个配置类（模块化配置），在核心配置类里加载其它配置类
* 相当于`xml`中的`<import resource="模块化xml文件路径"/>`标签

#### 2.2 `@Bean`注解

##### 1) `@Bean`定义bean

###### 简介

* `@Bean`注解：方法级别的注解
  * 用于把方法返回值声明成为一个bean，作用相当于`<bean>`标签
  * 可以用在任意bean对象的方法中，但是通常用在`@Configuration`标记的核心配置类中
* `@Bean`注解的属性：
  * `value`属性：bean的id。如果不设置，那么方法名就是bean的id

###### 示例

```java
@Configuration
public class AppConfig {
    @Bean
    public MyService myService() {
        return new MyServiceImpl();
    }
}
```

##### 2) `@Bean`的依赖注入

- `@Bean`注解的方法可以有任意参数，这些参数即是bean所需要的依赖
  - Spring默认采用byType方式注入依赖
  - 可以在方法参数上增加`@Qualifier`注解，使用byName注入

```java
@Configuration
public class AppConfig {
    @Bean
    public MyService myService(@Qualifier("myDao") MyDao myDao) {
        //把myDao通过构造方法设置给MyService       
        //return new MyServiceImpl(myDao);
        
        //通过set方法把myDao设置给MyService
        MyService myService = new MyServiceImpl();
        myService.setMyDao(myDao);
        return myServie;
    }
}
```

#### 2.3 小结

* 代替`applicationContext.xml`配置文件：

  * 创建一个类，在类上增加注解`@Configuration`

* 代替`<context:component-scan/>`标签

  * 在配置类上增加注解`@ComponentScan(basePackages="")`

* 代替`<context:property-placeholder/>`标签

  * 在配置类上增加注解`@PropertySource(location="classpath:jdbc.properties")`

* 代替`<bean>`标签，把第三方的类，声明成为bean对象

  * 在配置类里增加方法，在方法上增加注解`@Bean("bean的id")`
  * 把方法的返回值，声明成为bean对象

  ```java
  public class AppConfig{
  
      @Bean("runner")
      public QueryRunner runner(@Qualifier("ds")DataSource dataSource){
          return new QueryRunner(dataSource);
      }
  }
  ```

* 代替`<import>`标签
  * 在配置类上增加注解`@Import({模块1配置类.class, 模块2配置类.class})`

### 3. 使用示例

##### 1) 需求描述

* 使用Spring的新注解，代替CURD练习里，`applicationContext.xml`的所有配置

##### 2) 需求实现 

* 提供jdbc配置文件：jdbc.properties

```properties
jdbc.driver=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql:///spring
jdbc.username=root
jdbc.password=root
```

* 提供核心配置类：SpringConfig

```java
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;

@Configuration //声明当前类是一个配置类
@ComponentScan("com.itheima")//开启注解扫描
@PropertySource("classpath:jdbc.properties")//加载properties资源文件
public class SpringConfig {

    @Value("${jdbc.driver}")//注入properties中，jdbc.driver的值
    private String driver;

    @Value("${jdbc.url}")//注入properties中，jdbc.url的值
    private String url;

    @Value("${jdbc.username}")//注入properties中，jdbc.username的值
    private String username;

    @Value("${jdbc.password}")//注入properties中，jdbc.password的值
    private String password;

    //声明一个bean对象：数据库连接池对象，id是dataSource
    @Bean("dataSource")
    public DataSource dataSource() throws PropertyVetoException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setDriverClass(driver);
        dataSource.setJdbcUrl(url);
        dataSource.setUser(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    //声明一个bean对象：QueryRunner对象，id是runner。
    //方法参数DataSource，需要dataSource，使用@Qualifier注入依赖
    @Bean("runner")
    public QueryRunner queryRunner(@Qualifier("dataSource") DataSource dataSource){
        return new QueryRunner(dataSource);
    }
}
```

##### 4) 测试

```java
public class AccountTest {

    @Test
    public void queryAll() throws SQLException {
        ApplicationContext app = new AnnotationConfigApplicationContext(MyConfig.class);
        AccountService accountService = app.getBean("accountService", AccountService.class);
        List<Account> accounts = accountService.queryAll();
        for (Account account : accounts) {
            System.out.println(account);
        }
    }

    @Test
    public void findById() throws SQLException {
        ApplicationContext app = new AnnotationConfigApplicationContext(MyConfig.class);
        AccountService accountService = app.getBean("accountService", AccountService.class);
        Account account = accountService.findById(2);
        System.out.println(account);
    }

    @Test
    public void save() throws SQLException {
        ApplicationContext app = new AnnotationConfigApplicationContext(MyConfig.class);
        AccountService accountService = app.getBean("accountService", AccountService.class);

        Account account = new Account();
        account.setName("jerry");
        account.setMoney(2000d);
        accountService.save(account);
    }

    @Test
    public void edit() throws SQLException {
        ApplicationContext app = new AnnotationConfigApplicationContext(MyConfig.class);
        AccountService accountService = app.getBean("accountService", AccountService.class);

        Account account = new Account();
        account.setId(2);
        account.setName("tom");
        account.setMoney(10000d);

        accountService.edit(account);
    }

    @Test
    public void delete() throws SQLException {
        ApplicationContext app = new AnnotationConfigApplicationContext(MyConfig.class);
        AccountService accountService = app.getBean("accountService", AccountService.class);

        accountService.delete(3);
    }
}
```

### 4. 小结



## 四、Spring整合Junit（掌握）

​	在上边的CURD中，单元测试类里还需要我们自己去创建`ApplicationContext`，并自己去获取bean对象。Spring提供了整合Junit的方法，让单元测试更简洁方便。

### 1. 注解简介

| 注解                    | 说明                                                         |
| ----------------------- | ------------------------------------------------------------ |
| `@RunWith`              | 用在测试类上，用于声明不再使用Junit，而是使用Spring提供的运行环境 |
| `@ContextConfiguration` | 用于指定Spring配置类、或者Spring的配置文件                   |

> Spring提供了单元测试的运行环境：SpringJunit4ClassRunner，配置到`@RunWith`注解上：
>
> `@RunWith(SpringJunit4ClassRunner.class)`

* 要使用以上注解，需要导入jar包依赖：`spring-test` 和 `junit`

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-test</artifactId>
    <version>5.1.2.RELEASE</version>
</dependency>

<dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>4.12</version>
</dependency>
```

### 2. 使用示例

#### 2.1 步骤

1. 在pom.xml文件中增加依赖：`spring-test` 和 `junit`

2. 修改单元测试类

   1. 在单元测试类上增加注解：`@RunWith(SpringJunit4ClassRunner.class)`

      目的：使用Spring的单元测试运行器，替换Junit原生的运行器

   2. 在单元测试类上增加注解：`@ContextConfiguration()`

      目的：指定配置文件或配置类

#### 2.2 实现

1. 在pom.xml文件中增加依赖：`spring-test` 和 `junit`

   ```xml
   <dependency>
       <groupId>org.springframework</groupId>
       <artifactId>spring-test</artifactId>
       <version>5.1.2.RELEASE</version>
   </dependency>
   
   <dependency>
       <groupId>junit</groupId>
       <artifactId>junit</artifactId>
       <version>4.12</version>
   </dependency>
   ```

2. 修改单元测试类

   ```java
   import com.itheima.config.SpringConfig;
   import com.itheima.domain.Account;
   import com.itheima.service.AccountService;
   import org.junit.Test;
   import org.junit.runner.RunWith;
   import org.springframework.beans.factory.annotation.Autowired;
   import org.springframework.test.context.ContextConfiguration;
   import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
   
   import java.sql.SQLException;
   import java.util.List;
   
   @RunWith(SpringJUnit4ClassRunner.class)//使用Spring的单元测试运行器
   @ContextConfiguration(classes = SpringConfig.class)//指定核心配置类/核心配置文件
   public class AccountTest {
   
       @Autowired
       private AccountService accountService;
   
       @Test
       public void queryAll() throws SQLException {
           List<Account> accounts = accountService.queryAll();
           for (Account account : accounts) {
               System.out.println(account);
           }
       }
       
   	//......
   }
   
   ```

### 3. 小结

# 内容回顾

# 动态代理

### 1. JDK的动态代理

* 基于接口的，创建的代理对象是接口的实现类对象

```java
Proxy.newProxyInsance(
	ClassLoader loader,
    Class[] interfaces,
    new InvocationHandler(){
        public Object invoke(Object proxy, Method method, Object[] args){
            //写代理对象的行为
            return 结果
        }
    }
);
```

### 2. cglib动态代理

* 基于子类的，创建的代理对象是目标类的子类对象

```java
Enhancer.create(
	Class superClass,
    new MethodInterceptor(){
        public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy){
            
            //第一种方式调用目标对象
            Object result = method.invoke(目标对象, args);
            
            //第二种方式调用目标对象（推荐）
            Object result = methodProxy.invokeSuper(proxy, args);
            
            return result;
        }
    }
);
```


## 概述

  mybatis框架的优点，就不用多说了，今天这边干货主要讲mybatis的逆向工程，以及springboot的集成技巧，和分页的使用

  因为在日常的开发中，当碰到特殊需求之类会手动写一下sql语句，大部分的时候完全可以用mybatis的逆向工程替代。
  
## mybatis逆向工程

相比较而言，代码形式的逆向工程，更加灵活方便，简单，易于管理，而且可以上传到git中存储。而且也只需要简单的三步就能完成自动生成代码。下面介绍如果搭建并生成xml和代码
 ### 第一步：搭建工程
**本项目使用的是maven搭建的工程，在你的pom文件中加入以下依赖：（这里以mysql数据库为例）** 

```    <dependencies>
        <!-- https://mvnrepository.com/artifact/org.mybatis/mybatis -->
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
            <version>3.4.0</version>
        </dependency>
        <!-- https://mvnrepository.com/artifact/mysql/mysql-connector-java -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.34</version>
        </dependency>
        <!-- https://mvnrepository.com/artifact/log4j/log4j -->
        <dependency>
            <groupId>log4j</groupId>
            <artifactId>log4j</artifactId>
            <version>1.2.17</version>
        </dependency>
        <!-- https://mvnrepository.com/artifact/org.mybatis.generator/mybatis-generator-core -->
        <dependency>
            <groupId>org.mybatis.generator</groupId>
            <artifactId>mybatis-generator-core</artifactId>
            <version>1.3.5</version>
        </dependency>
    </dependencies>
```
### 第二步：配置generatorConfig.xml

该xml文件是mybatis的配置项 这里记录了数据库连接的配置，

```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
        PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">

<generatorConfiguration>
    <context id="testTables" targetRuntime="MyBatis3">
        <commentGenerator>
            <!-- 是否去除自动生成的注释 true：是 ： false:否 -->
            <property name="suppressAllComments" value="true" />
        </commentGenerator>
        <!--数据库连接的信息：驱动类、连接地址、用户名、密码 -->
        <jdbcConnection driverClass="com.mysql.jdbc.Driver"
                        connectionURL="jdbc:mysql://localhost:3306/database" userId="root"
                        password="123456">
        </jdbcConnection>

        <!-- 默认false，把JDBC DECIMAL 和 NUMERIC 类型解析为 Integer，为 true时把JDBC DECIMAL和NUMERIC类型解析为java.math.BigDecimal -->
        <javaTypeResolver>
            <property name="forceBigDecimals" value="false" />
        </javaTypeResolver>

        <!-- targetProject:生成PO类的位置，重要！！ -->
        <javaModelGenerator targetPackage="springboot.modal.vo"
                            targetProject=".\src">
            <!-- enableSubPackages:是否让schema作为包的后缀 -->
            <property name="enableSubPackages" value="false" />
            <!-- 从数据库返回的值被清理前后的空格 -->
            <property name="trimStrings" value="true" />
        </javaModelGenerator>
        <!-- targetProject:mapper映射文件生成的位置，重要！！ -->
        <sqlMapGenerator targetPackage="springboot.dao"
                         targetProject=".\src">
            <property name="enableSubPackages" value="false" />
        </sqlMapGenerator>
        <!-- targetPackage：mapper接口生成的位置，重要！！ -->
        <javaClientGenerator type="XMLMAPPER"
                             targetPackage="springboot.dao"
                             targetProject=".\src">
            <property name="enableSubPackages" value="false" />
        </javaClientGenerator>
        <!-- 指定数据库表，要生成哪些表，就写哪些表，要和数据库中对应，不能写错！ -->
        <table tableName="t_contents" domainObjectName="ContentVo" mapperName="ContentVoMapper" ></table>

    </context>
</generatorConfiguration>
```
### 第三步：启动类
启动类主要设置main方法以及，制定配置文件generatorConfig.xml的路径

```
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class Generator {
    public static void main(String args[]) throws  Exception{
        List<String> warnings = new ArrayList<String>();
        boolean overwrite  = true;
        File configFile = new File("./src/main/resources/generatorconfig.xml");
        System.out.println(configFile.exists());
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(configFile);
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        myBatisGenerator.generate(null);
    }
}
```  
当然如果有你想要设置日志输出的话，可以加一个log4j.properties,简单配置一下日志输出：
```
# Global logging configuration
log4j.rootLogger=DEBUG, stdout
# MyBatis logging configuration...
log4j.logger.org.mybatis.example.BlogMapper=TRACE
# Console output...
log4j.appender.stdout=org.apache.log4j.ConsoleAppender
log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
log4j.appender.stdout.layout.ConversionPattern=%5p [%t] - %m%n
```


**最后运行启动类Generator，运行之前得确保，在数据库中先创建好表**

工程已经搭建好了，可以参考github上：
[mybatis逆向工程GitHub](https://github.com/JayTange/mybatisgenetate)

## 逆向工程如何使用
### mapper中的方法
逆向工程生成完毕后，mybatis会在mapper中提供一些默认的接口和参数，下面就介绍一下这些方法的使用：

方法                                                    | 功能说明  
-------                                                | ------- 
int countByExample(UserExample example)               | 按条件计数
int deleteByPrimaryKey(Integer id)                | 按主键删除
int deleteByExample(UserExample example)            | 按条件删除
String/Integer insert(User record)|插入数据，返回值的ID
String/Integer insertSelective(User record) |插入一条数据,只插入不为null的字段
User selectByPrimaryKey(Integer id) |按主键查询
List selectByExample(UserExample example) |按条件查询
List selectByExampleWithBLOGs(UserExample example) |按条件查询（包括BLOB字段）。只有当数据表中的字段类型有为二进制的才会产生。
int updateByPrimaryKey(User record)|按主键更新
int updateByPrimaryKeySelective(User record)|按主键更新值不为null的字段
int updateByExample(User record, UserExample example) |按条件更新
int updateByExampleSelective(User record, UserExample example) |按条件更新值不为null的字段


### example类中的方法
mybatis的逆向工程中会生成实例及实例对应的example，example用于添加条件，相当where后面的部分
```
xxxExample example = new xxxExample();
Criteria criteria = new Example().createCriteria();
```

下表是常用方法

方法 | 说明 
------- | -------  
example.setOrderByClause(“字段名 ASC”); | 添加升序排列条件，DESC为降序 
example.setDistinct(false) | 去除重复，boolean型，true为选择不重复的记录。
criteria.andXxxIsNull| 添加字段xxx为null的条件
criteria.andXxxIsNotNull | 添加字段xxx不为null的条件
criteria.andXxxNotEqualTo(value)|添加xxx字段不等于value条件
criteria.andXxxGreaterThan(value)| 	添加xxx字段大于value条件
criteria.andXxxGreaterThanOrEqualTo(value)|添加xxx字段大于等于value条件
criteria.andXxxLessThan(value)|添加xxx字段小于value条件
criteria.andXxxLessThanOrEqualTo(value)| 	添加xxx字段小于等于value条件
criteria.andXxxIn(List<？>)|添加xxx字段值在List<？>条件
criteria.andXxxNotIn(List<？>)| 	添加xxx字段值不在List<？>条件
criteria.andXxxLike(“%”+value+”%”)|添加xxx字段值为value的模糊查询条件
criteria.andXxxNotLike(“%”+value+”%”)| 	添加xxx字段值不为value的模糊查询条件
criteria.andXxxBetween(value1,value2)| 	添加xxx字段值在value1和value2之间条件
criteria.andXxxNotBetween(value1,value2)| 	添加xxx字段值不在value1和value2之间条件

## sringboot整合mybatis
springboot整合mybatis很简单 只需要简单的配置即可以。
这里使用时xml方式，注解方式相对而言是清爽一些，但是sql全都堆砌在java文件中，并不利于阅读，而且也没有xml方式灵活。

### 项目构建
这里使用的是maven来构建项目，下面是pom文件：

```
  <properties>
        <java.version>1.8</java.version>
    </properties>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>1.5.1.RELEASE</version>
        <relativePath/>
    </parent>

    <dependencies>
        <!-- 数据库连接池-->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid</artifactId>
            <version>1.0.18</version>
        </dependency>

        <!-- mysql -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.35</version>
            <scope>runtime</scope>
        </dependency>

        <!-- spring boot 配置 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
      
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>1.2.0</version>
        </dependency>
    </dependencies>
```
### 工程搭建
搭建springboot第一件事就是使用配置application.properties。整合mybatis的时候需要配置jdbc的信息，这里还用了阿里的连接池Druid.下面是详细的配置信息：
```
server.port=80
#spring.datasource.type=com.alibaba.druid.pool.DruidDataSource
spring.datasource.driverClassName=com.mysql.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/bootmybatis?useSSL=false&useUnicode=true&characterEncoding=utf-8&autoReconnect=true
#用户名
spring.datasource.username=root
#密码
spring.datasource.password=123456
spring.datasource.initialSize=20
spring.datasource.minIdle=10
spring.datasource.maxActive=100
# 输出mybatis日志 sql语句方便调试
logging.level.com.dao=DEBUG
```

下图是工程结构图：
![alt](/upload/2018/04/2jd09vcng8ihbr19vm1kqfrh31.jpg)

这里的UserVoMapper,UserVo,UserVoExample,都是使用的逆向工程生成的
启动类代码：

```
package com;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@SpringBootApplication
@ComponentScan
@EnableAutoConfiguration
@MapperScan("com.dao")
public class StartApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication app = new SpringApplication(StartApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }

    // datasource注入
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return new DruidDataSource();
    }

    //mybatis SQLSession注入
    @Bean
    public SqlSessionFactory sqlSessionFactoryBean() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource());
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        // 这里设置mybatis xml文件的地址
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:/mapper/*.xml"));
        return sqlSessionFactoryBean.getObject();
    }
}

```  

### 测试
在IndexController代码如下：
```
package com.controller;

import com.dao.UserVoMapper;
import com.domain.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class IndexController {

    @Autowired
    UserVoMapper userDao;

    @GetMapping(value = "")
    @ResponseBody
    public UserVo index(){
        UserVo userVo = new UserVo();
        userVo.setUsername("SELECTIVE");
        userVo.setPassword("123456");
        userVo.setAddress("北京");
        userDao.insertSelective(userVo);
        userVo = userDao.selectByPrimaryKey(1);
        return userVo;
    }
}

```  
启动startApplication,在浏览器中输入http://127.0.0.1,即可查看到结果。

如果有不明白的可以去git上查看源码，[传送门](https://github.com/JayTange/springbootMybatis)
喜欢的话，给个star

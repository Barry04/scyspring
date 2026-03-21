# ScySpring

> 一个基于 Spring Boot 的学习项目，展示了现代 Java Web 开发的最佳实践。

## 📋 项目概述

ScySpring 是一个个人学习专用项目，主要用于深入学习和实践 Spring Boot、MyBatis Plus、AOP、异步处理、事务管理等企业级开发技术。

## 🛠️ 技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Java | 1.8 | 编程语言 |
| Spring Boot | 2.7.11 | Web 框架 |
| MyBatis Plus | 3.5.8 | ORM 框架 |
| MySQL | 8.0.31 | 关系型数据库 |
| Log4j2 | Latest | 日志框架 |
| Lombok | Latest | 代码生成工具 |
| AspectJ | 1.9.6 | AOP 框架 |
| PageHelper | 5.3.2 | 分页插件 |
| FastJSON | 1.2.83 | JSON 解析库 |
| Oracle JDBC | 19.3.0.0 | Oracle 数据库支持 |

## 📁 项目结构

```
src/
├── main/
│   ├── java/org/scy/scyspring/
│   │   ├── ScySpringApplication.java          # 启动类
│   │   ├── controller/
│   │   │   └── HelloController.java           # 控制器
│   │   ├── core/
│   │   │   ├── domain/                        # 实体类
│   │   │   │   ├── AsyncFuture.java
│   │   │   │   ├── Log.java
│   │   │   │   ├── UserInfo.java
│   │   │   │   └── SomeObj.java
│   │   │   ├── mapper/                        # Mapper 接口
│   │   │   │   ├── LogMapper.java
│   │   │   │   └── UserInfoMapper.java
│   │   │   ├── service/                       # 业务层
│   │   │   │   ├── AsyncService.java
│   │   │   │   ├── LogService.java
│   │   │   │   ├── UserInfoService.java
│   │   │   │   └── impl/                      # 实现类
│   │   │   │       ├── AsyncServiceImpl.java
│   │   │   │       ├── LogServiceImpl.java
│   │   │   │       ├── UserInfoServiceImpl.java
│   │   │   │       └── TransactionServiceImpl.java
│   │   ├── interceptor/
│   │   │   └── MyAspect.java                  # AOP 切面
│   │   └── utils/
│   │       └── ExecutorsUtils.java            # 工具类
│   └── resources/
│       ├── application.properties             # 应用配置
│       ├── log4j2.xml                         # 日志配置
│       └── mapper/
│           └── UserInfoMapper.xml             # MyBatis 映射文件
└── test/
    └── java/org/scy/scyspring/
        └── ScySpringApplicationTests.java     # 测试类
```

## ✨ 主要功能

### 1. **用户信息管理**
- 用户信息的增删改查
- 基于 MyBatis Plus 的 CRUD 操作
- 分页查询支持

### 2. **日志管理**
- 系统日志的记录和查询
- 支持多种日志级别
- Log4j2 日志框架集成

### 3. **异步处理**
- 异步任务执行
- AsyncService 异步服务
- 异步结果回调（Rocall 模式）

### 4. **AOP 切面**
- MyAspect 切面处理
- 方法拦截和增强
- 日志记录和性能监控

### 5. **事务管理**
- Spring 声明式事务支持
- 多种事务实现方案
- TransactionServiceImpl 和 TransactionServiceTwoImpl

### 6. **REST API**
- HelloController 提供的各种 REST 端点
- JSON 格式的数据交互
- FastJSON 用于 JSON 序列化

## 🚀 快速开始

### 前提条件
- JDK 1.8 或更高版本
- Maven 3.6+
- MySQL 5.7 或更高版本

### 安装步骤

1. **克隆项目**
   ```bash
   git clone <repository-url>
   cd scyspring
   ```

2. **修改数据库配置**
   编辑 `src/main/resources/application.properties`：
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/your_database?useUnicode=true&characterEncoding=utf-8&useSSL=false
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

3. **构建项目**
   ```bash
   mvn clean install
   ```

4. **运行应用**
   ```bash
   mvn spring-boot:run
   ```
   
   应用将在 `http://localhost:7181` 启动

## 📝 API 示例

### 查询用户信息
```
GET /api/users
```

### 创建日志
```
POST /api/logs
```

### 异步任务
```
POST /api/async/task
```

## ⚙️ 配置说明

### application.properties
```properties
# 数据库配置
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://...
spring.datasource.username=root
spring.datasource.password=...

# MyBatis Plus 配置
mybatis-plus.mapper-locations=classpath:mapper/*xml
mybatis-plus.type-aliases-package=org.scy.scyspring.core.domain

# 服务器配置
server.port=7181

# Spring Boot 配置
spring.jmx.enabled=true
management.endpoints.jmx.exposure.include=*
```

### log4j2.xml
- 支持控制台和文件日志输出
- 可配置日志级别和格式

## 🔑 核心类说明

| 类名 | 说明 |
|------|------|
| `ScySpringApplication` | 应用启动类，配置 MyBatis Mapper 扫描和事务管理 |
| `HelloController` | REST 控制器，提供各种 API 端点 |
| `UserInfoService` | 用户服务接口 |
| `UserInfoServiceImpl` | 用户服务实现 |
| `AsyncService` | 异步服务接口 |
| `AsyncServiceImpl` | 异步服务实现 |
| `LogService` | 日志服务接口 |
| `LogServiceImpl` | 日志服务实现 |
| `MyAspect` | AOP 切面类 |
| `ExecutorsUtils` | 线程池工具类 |

## 📊 数据库支持

- ✅ MySQL（主要使用）
- ✅ Oracle（配置支持）

## 🧪 测试

运行单元测试：
```bash
mvn test
```

## 📚 学习资源

本项目涵盖的主要学习内容：
- Spring Boot 框架原理
- MyBatis Plus ORM 框架
- AOP 面向切面编程
- 异步任务处理
- 事务管理
- 日志框架集成
- RESTful API 设计

## 🎯 注意事项

- 这是个人学习项目，不适合直接用于生产环境
- 数据库连接信息需要根据实际环境修改
- 某些密码和敏感信息已被脱敏

## 📖 相关文档

- [SQL 优化说明](./img/sql优化说明.md)
- 更多内容参考 `src/main/resources/` 下的文档

## 📞 联系方式

如有任何问题或建议，欢迎提出 Issue 或 Pull Request。

## 📄 许可证

本项目仅供学习使用，版权归作者所有。

---

**最后更新**: 2024年

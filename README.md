# PgHBA 规则管理器

一个基于 Spring Boot 和 Vue 3 的 PostgreSQL `pg_hba.conf` 配置文件管理工具，提供 Web 界面来管理、审计和同步 HBA 规则。

## 功能特性

### 🔧 核心功能
- **规则管理**: 通过 Web 界面创建、编辑、删除 PostgreSQL HBA 规则
- **规则解析**: 支持解析标准的 `pg_hba.conf` 规则语法
- **实时同步**: 自动将规则变更同步到 PostgreSQL 配置文件
- **配置重载**: 一键重载 PostgreSQL 配置，无需重启服务

### 📊 审计功能
- **操作审计**: 完整记录所有规则操作（创建、更新、删除、激活、停用）
- **配置审计**: 跟踪配置管理操作（同步、重载、权限检查）
- **详细日志**: 记录操作用户、IP地址、操作时间、详细变更内容
- **统计分析**: 提供操作统计和趋势分析图表

### 🖥️ 配置管理
- **在线编辑**: 直接在 Web 界面编辑 `pg_hba.conf` 文件
- **在线预览**: 实时预览配置文件内容
- **权限检查**: 验证应用程序对配置文件的写入权限
- **状态监控**: 实时监控 PostgreSQL 连接状态和版本信息

### 🎨 用户界面
- **现代化设计**: 基于 Element Plus 的响应式界面
- **Tab 管理**: 分标签页展示规则审计和配置审计
- **搜索过滤**: 支持多条件搜索和时间范围过滤
- **分页展示**: 大量数据的分页浏览

## 技术栈

### 后端 (Spring Boot)
- **框架**: Spring Boot 2.7.x
- **数据库**: PostgreSQL + MyBatis
- **连接池**: HikariCP
- **解析器**: ANTLR4 语法解析
- **构建工具**: Gradle 8.5

### 前端 (Vue 3)
- **框架**: Vue 3 + Composition API
- **路由**: Vue Router
- **UI 组件**: Element Plus
- **图表**: ECharts
- **构建工具**: Vite
- **HTTP 客户端**: Axios

## 本地部署

### 环境要求
- **Java**: JDK 8
- **Node.js**: 16.x 或更高版本
- **PostgreSQL**: 10.x 或更高版本
- **Gradle**: 8.5 (可选，项目包含 wrapper)

### 数据库准备
```sql
-- 创建数据库
CREATE DATABASE pghba_db;

-- 创建用户（可选）
CREATE USER pghba_user WITH PASSWORD 'your_password';
GRANT ALL PRIVILEGES ON DATABASE pghba_db TO pghba_user;
```


### 后端配置

1. 编辑backend/src/main/resources/application.yaml:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/pghba_db
    username: postgres
    password: your_password

pg:
  hba:
    config:
      path: /etc/postgresql/12/main/pg_hba.conf  # 根据实际路径调整

```

2. 确保应用程序有权限访问和修改 pg_hba.conf 文件


### 构建和运行

1. 使用 Gradle Wrapper (推荐)

```bash
# 克隆项目
git clone <repository-url>
cd pg-hba-manager

# 构建整个项目
./gradlew buildAll

# 运行后端服务
./gradlew :backend:bootRun

# 运行前端开发服务器
./gradlew :ui:serve

```

2. 手动构建

```yaml
# 构建后端
cd backend
./gradlew build

# 构建前端
cd ../ui
npm install
npm run build

# 运行应用
cd ../backend
./gradlew bootRun
```

### 访问应用

使用gradle wapper构建后会自动打开浏览器 http://localhost:12000



### 项目结构

```yaml
pg-hba-manager/
├── backend/                    # 后端服务
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── pg/hba/
│   │   │   │       ├── controller/     # REST API 控制器
│   │   │   │       ├── entity/         # 数据实体
│   │   │   │       ├── mapper/         # MyBatis Mapper
│   │   │   │       ├── parser/         # ANTLR 解析器
│   │   │   │       ├── repository/     # 数据访问层
│   │   │   │       ├── service/        # 业务逻辑层
│   │   │   │       └── PgHbaManagerApplication.java
│   │   │   └── resources/
│   │   │       ├── mapper/             # XML Mapper 文件
│   │   │       ├── application.yml      # 配置文件
│   │   │       └── schema.sql          # 数据库初始化脚本
│   │   └── test/                       # 测试代码
│   └── build.gradle                    # 后端构建配置
├── ui/                         # 前端应用
│   ├── src/
│   │   ├── assets/                     # 静态资源
│   │   ├── components/                 # Vue 组件
│   │   ├── services/                   # API 服务
│   │   ├── views/                      # 页面视图
│   │   ├── router/                     # 路由配置
│   │   ├── App.vue                     # 根组件
│   │   └── main.js                     # 入口文件
│   ├── package.json                    # 前端依赖
│   └── vite.config.js                  # Vite 配置
├── gradle/                     # Gradle Wrapper
├── build.gradle                # 根项目构建配置
├── settings.gradle             # 模块配置
└── gradlew                     # Gradle Wrapper 脚本

```


### API 接口

#### 规则管理

1. GET /api/hba-rules/active - 获取所有活跃规则
2. GET /api/hba-rules/all - 获取所有规则
3. POST /api/hba-rules/parse-and-save - 解析并保存规则
4. PUT /api/hba-rules/{id} - 更新规则
5. DELETE /api/hba-rules/{id} - 删除规则

#### 配置管理

1. POST /api/hba-rules/sync-config - 同步配置到 pg_hba.conf
2. POST /api/hba-rules/reload-postgresql - 重载 PostgreSQL 配置
3. GET /api/hba-rules/config-status - 获取配置文件状态
4. GET /api/hba-rules/check-permission - 检查写入权限
5. GET /api/hba-rules/config-content - 读取配置文件内容
6. PUT /api/hba-rules/config-content - 更新配置文件内容

#### 审计日志

1. GET /api/audit-logs/latest - 获取最新规则审计日志
2. POST /api/audit-logs/search - 搜索规则审计日志
3. GET /api/config-logs/latest - 获取最新配置审计日志
4. POST /api/config-logs/search - 搜索配置审计日志

### 开发指南

后端开发

```bash
# 运行单元测试
./gradlew :backend:test

# 代码质量检查
./gradlew :backend:check

# 生成 ANTLR 解析器
./gradlew :backend:generateGrammarSource

```

前端开发

```bash
# 开发模式运行
cd ui
npm run dev

# 构建生产版本
npm run build

# 预览构建结果
npm run preview

```



数据库迁移

```sql
-- 查看表结构
\d hba_rules
\d audit_logs
\d config_operation_logs

-- 清理测试数据
DELETE FROM config_operation_logs;
DELETE FROM audit_logs;
DELETE FROM hba_rules;

```


### 安全考虑
1. 文件权限: 确保应用程序有足够的权限读写 pg_hba.conf 文件
2. 数据库权限: 应用程序用户应具有适当的数据库权限
3. 网络安全: 在生产环境中使用 HTTPS 和身份验证
4. 操作审计: 所有关键操作都会被记录和审计


### 故障排除

##### 常见问题

1. 数据库连接失败: 检查 application.yml 中的数据库配置
2. 文件权限错误: 确保应用程序有权限访问配置文件目录
3. 端口冲突: 修改 application.yml 中的服务器端口
4. 构建失败: 确保安装了正确的 Java 和 Node.js 版本


##### 日志查看

```bash
# 查看后端日志
tail -f backend/logs/application.log

# 查看数据库日志
tail -f /var/log/postgresql/postgresql-*.log
```


### 贡献指南

1. Fork 项目
2. 创建功能分支 (git checkout -b feature/AmazingFeature)
3. 提交更改 (git commit -m 'Add some AmazingFeature')
4. 推送到分支 (git push origin feature/AmazingFeature)
5. 开启 Pull Request

### 许可证

本项目采用 MIT 许可证 - 查看 LICENSE 文件了解详情


### 总结

🚀 开始管理您的 PostgreSQL HBA 规则吧！
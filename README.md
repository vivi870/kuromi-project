# Kuromi 项目

一个以 Kuromi（酷洛米）为主题的全栈 Web 应用，包含商城、社区笔记、活动、视频等功能模块，并配备完整的管理员后台。

---

## 技术栈

### 前端
| 技术 | 版本 | 说明 |
|------|------|------|
| Vue 3 | ^3.5.24 | 前端框架（Composition API） |
| Vite | ^7.2.4 | 构建工具 |
| Vue Router | ^4.6.4 | 路由管理 |
| Pinia | ^3.0.4 | 状态管理 |
| Element Plus | ^2.13.0 | UI 组件库 |
| Axios | ^1.13.2 | HTTP 请求 |
| Mock.js | ^1.1.0 | 接口模拟 |
| qrcode.vue | ^3.8.0 | 二维码生成 |
| dayjs | ^1.11.19 | 日期处理 |

### 后端
| 技术 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 3.2.5 | 后端框架 |
| Java | 17 | 运行环境 |
| MyBatis-Plus | 3.5.7 | ORM 框架 |
| MySQL | 8.x | 关系型数据库 |
| Druid | 1.2.20 | 数据库连接池 |
| Spring Security | 随 Boot 版本 | 认证与密码加密 |
| Alipay EasySDK | 2.2.0 | 支付宝支付集成 |
| ZXing | 3.5.2 | 二维码生成 |
| Lombok | — | 简化实体类 |

---

## 项目结构

```
klm/
├── backend/
│   └── Kuromi/                  # Spring Boot 后端
│       ├── src/main/java/com/example/kuromi/
│       │   ├── controller/      # 接口控制器
│       │   ├── service/         # 业务逻辑层
│       │   ├── mapper/          # MyBatis-Plus Mapper
│       │   ├── entity/          # 数据库实体类
│       │   ├── dto/             # 数据传输对象
│       │   ├── vo/              # 视图对象
│       │   ├── config/          # 配置类（跨域、Security 等）
│       │   ├── common/          # 公共工具类
│       │   └── KuromiApplication.java
│       └── pom.xml
├── Kuromi/                      # Vue 3 前端
│   ├── src/
│   │   ├── components/          # 页面组件
│   │   │   ├── Home.vue         # 首页
│   │   │   ├── Login.vue        # 登录/注册
│   │   │   ├── LoginPage.vue    # 登录页面
│   │   │   ├── Community.vue    # 社区/笔记广场
│   │   │   ├── Shopping.vue     # 商城
│   │   │   ├── Cart.vue         # 购物车
│   │   │   ├── Checkout.vue     # 结算
│   │   │   ├── OrderList.vue    # 订单列表
│   │   │   ├── Detail.vue       # 商品详情
│   │   │   ├── Video.vue        # 视频页
│   │   │   ├── Surroundings.vue # 周边
│   │   │   ├── Hddetail.vue     # 活动详情
│   │   │   ├── PublishNoteModal.vue   # 发布笔记弹窗
│   │   │   ├── NoteDetailModal.vue    # 笔记详情弹窗
│   │   │   ├── alipay-pay.vue   # 支付宝支付
│   │   │   ├── Header.vue       # 顶部导航
│   │   │   ├── Footer.vue       # 底部
│   │   │   ├── PaginationNav.vue # 分页组件
│   │   │   ├── MySearchInput.vue # 搜索框
│   │   │   └── admin/           # 管理员后台
│   │   │       ├── AdminLogin.vue
│   │   │       ├── AdminLayout.vue
│   │   │       ├── Dashboard.vue
│   │   │       ├── UserManage.vue
│   │   │       ├── ProductManage.vue
│   │   │       ├── OrderManage.vue
│   │   │       ├── ActivityManage.vue
│   │   │       ├── VideoManage.vue
│   │   │       └── NoteManage.vue
│   │   ├── axios/               # Axios 请求封装
│   │   │   ├── request.js
│   │   │   ├── product.js
│   │   │   └── note.js
│   │   ├── store/               # Pinia 状态管理
│   │   │   ├── index.js
│   │   │   ├── cart.js
│   │   │   ├── orderStore.js
│   │   │   └── noteDetailStore.js
│   │   ├── mock/                # Mock 数据
│   │   ├── router/              # 路由配置
│   │   │   └── router.js
│   │   ├── assets/              # 静态资源（图片、视频、CSS）
│   │   ├── App.vue
│   │   └── main.js
│   ├── index.html
│   ├── vite.config.js
│   └── package.json
└── kuromi.sql                   # 数据库初始化脚本
```

---

## 数据库

数据库名：`kuromi`，MySQL 8.x

| 表名 | 说明 |
|------|------|
| `sys_user` | 普通用户表 |
| `admin` | 管理员表 |
| `user_audit` | 用户审核记录 |
| `product` | 商品表 |
| `user_cart` | 购物车表 |
| `sys_order` | 订单表 |
| `sys_order_item` | 订单明细表 |
| `note` | 社区笔记表 |
| `note_comment` | 笔记评论表 |
| `hd` | 活动表 |
| `video` | 视频表 |

---

## 主要功能

### 用户端
- **首页**：轮播图、热门内容展示
- **商城**：商品列表、商品详情、购物车、结算、订单管理
- **支付**：集成支付宝支付（沙箱/正式环境）
- **社区**：笔记广场、发布笔记、笔记详情、评论
- **活动**：活动列表与详情
- **视频**：视频列表、弹幕播放
- **用户中心**：注册/登录、修改用户名、上传头像
- **周边**：周边内容展示

### 管理员后台
- 管理员登录（独立入口）
- 用户管理
- 商品管理
- 订单管理
- 活动管理
- 视频管理
- 笔记管理（审核/删除）
- 数据概览 Dashboard

---

## 快速启动

### 环境要求
- Node.js >= 18
- Java 17
- Maven 3.6+
- MySQL 8.x

### 1. 数据库初始化

```sql
CREATE DATABASE kuromi CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

然后导入数据库脚本：

```bash
mysql -u root -p kuromi < kuromi.sql
```

### 2. 启动后端

```bash
cd backend/Kuromi
# 修改 src/main/resources/application.yml 中的数据库连接信息
mvn spring-boot:run
```

后端默认端口：`8080`

后端登录账号：admin 密码：admin123

### 3. 启动前端

```bash
cd Kuromi
npm install
npm run dev
```

前端默认端口：`5173`，浏览器访问 [http://localhost:5173](http://localhost:5173)

前端先注册后登录

---

## 配置说明

### 后端数据库配置

修改 `backend/Kuromi/src/main/resources/application.yml`（或 `application.properties`）：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/kuromi?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
    username: your_username
    password: your_password
```

### 支付宝配置

在后端配置文件中填入支付宝沙箱/正式环境的 AppId、私钥、公钥等信息。

---

## Git 提交记录（近期）

| 提交 | 说明 |
|------|------|
| 9aa39c9 | 修改了代码格式 |
| ba45eb9 | 新增管理员后台 |
| 5a862e4 | 实现了发布笔记功能 |
| fdd8870 | 修复更改用户名后登录及评论用户名同步问题 |
| 09c60af | 修复笔记评论头像同步问题 |
| 63834a2 | 修复上传头像代码 |

---

## License

本项目仅供学习交流使用。

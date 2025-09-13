# UserHub: 用户信息管理演示

## 开发环境

基于Java & PostgreSQL开发容器进行开发，首次创建容器时，[init_users.sql](initdb/init_users.sql)会初始化数据表。

主要技术依赖：Spring Data JDBC、Spring Web、Lombok、Java JWT、Thymeleaf

## 数据表设计

```sql
CREATE TYPE gender_enum AS ENUM ('M','F');
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    fullname VARCHAR(50) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE CHECK (username ~ '^[a-zA-Z0-9]+$'),
    password_hash CHAR(32) NOT NULL,
    birthdate DATE,
    gender gender_enum
);
```

## Web API 设计

### 用户管理

#### 新增用户

Endpoint: `POST /api/users`  
Request Body:

```json
{
  "name": "张三",
  "username": "zhangsan",
  "password": "123456",
  "birth_date": "1990-01-01",
  "gender": "M"
}
```

Response:

```json
{
  "message": "用户创建成功",
  "user_id": 1
}
```

---

#### 查询用户列表

Endpoint: `GET /api/users`  
Response:

```json
[
  {
    "id": 1,
    "name": "张三",
    "username": "zhangsan",
    "birth_date": "1990-01-01",
    "gender": "M"
  },
  {
    "id": 2,
    "name": "李四",
    "username": "lisi",
    "birth_date": "1992-02-02",
    "gender": "F"
  }
]
```

---

#### 查询单个用户

Endpoint: `GET /api/users/{id}`  
Response:

```json
{
  "id": 1,
  "name": "张三",
  "username": "zhangsan",
  "birth_date": "1990-01-01",
  "gender": "M"
}
```

---

#### 更新用户信息

Endpoint: `PUT /api/users/{id}`  
Request Body（仅更新Request提供的字段）:

```json
{
  "name": "张三丰",
  "birth_date": "1990-01-01",
  "gender": "M"
}
```

Response:

```json
{
  "message": "用户信息更新成功"
}
```

---

#### 删除用户

Endpoint: `DELETE /api/users/{id}`  
Response:

```json
{
  "message": "用户删除成功"
}
```

---

### 登录功能

#### 用户登录

Endpoint: `POST /api/login`  
Request Body:

```json
{
  "username": "zhangsan",
  "password": "123456"
}
```

Response (成功):

```json
{
  "message": "登录成功",
  "user_id": 1,
  "token": "jwt-token-string"
}
```

Response (错误示例):

账号不存在：

```json
{
  "message": "账号不存在"
}
```

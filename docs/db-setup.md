# MySQL DB Setup

## Required Connection Info

| Key | Value |
|---|---|
| Host | `HOST` |
| Port | `3306` |
| Database name | `hackathon` |
| Username | `hackathon_user` |
| Password | `팀 전용 비밀번호` |

## Spring Boot Environment Variables

```env
DB_URL=jdbc:mysql://HOST:PORT/hackathon?serverTimezone=Asia/Seoul&characterEncoding=UTF-8
DB_USERNAME=hackathon_user
DB_PASSWORD=팀전용비밀번호
```

## Create Database And Team User

Run this with a MySQL admin account. Do not share the admin or root account with the team.

```sql
CREATE DATABASE IF NOT EXISTS hackathon
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

CREATE USER IF NOT EXISTS 'hackathon_user'@'%'
  IDENTIFIED BY 'CHANGE_THIS_PASSWORD';

GRANT ALL PRIVILEGES ON hackathon.* TO 'hackathon_user'@'%';

FLUSH PRIVILEGES;
```

## Local MySQL Example

```env
DB_URL=jdbc:mysql://localhost:3306/hackathon?serverTimezone=Asia/Seoul&characterEncoding=UTF-8
DB_USERNAME=hackathon_user
DB_PASSWORD=CHANGE_THIS_PASSWORD
```

Local MySQL requires external access and firewall settings if a deployed server needs to connect to it. A cloud MySQL instance is preferred for deployment.

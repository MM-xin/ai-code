# 数据库初始化

-- 创建库
create database if not exists ai_code;

-- 切换库
use ai_code;

-- 用户表
-- 以下是建表语句

-- 用户表
create table if not exists user
(
    id            bigint auto_increment comment 'id' primary key,
    user_account  varchar(256)                           not null comment '账号',
    user_password varchar(512)                           not null comment '密码',
    user_name     varchar(256)                           null comment '用户昵称',
    user_avatar   varchar(1024)                          null comment '用户头像',
    user_profile  varchar(512)                           null comment '用户简介',
    user_role     tinyint      default 0                 not null comment '角色：0-普通用户, 1-管理员, 2-超级管理员',
    edit_time     datetime     default CURRENT_TIMESTAMP not null comment '编辑时间',
    create_time   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete     tinyint      default 0                 not null comment '是否删除',
    unique key uk_user_account (user_account),
    index idx_user_name (user_name)
    ) comment '用户' collate = utf8mb4_unicode_ci;

-- 角色表
create table if not exists role
(
    id          bigint auto_increment comment 'id' primary key,
    role_name   varchar(256)                           not null comment '角色名称',
    role_code   varchar(128)                           not null comment '角色编码',
    description varchar(512)                           null comment '角色描述',
    create_time datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete   tinyint      default 0                 not null comment '是否删除',
    unique key uk_role_code (role_code)
    ) comment '角色表' collate = utf8mb4_unicode_ci;


create database if not exists user;
create table if not exists user(
                                   id int primary key auto_increment comment'用户主键id',
                                   name varchar(20) comment'用户姓名',
                                   avatar varchar(1000) comment'用户头像',
                                   account varchar(20) not null comment'用户账号',
                                   password varchar(50) not null comment'用户密码',
                                   email varchar(50) comment'用户邮箱',
                                   profile varchar(200) comment'用户个人介绍',
                                   create_time datetime default current_timestamp comment'用户创建时间',
                                   update_time datetime default current_timestamp on update current_timestamp comment'用户更新时间'
);

ALTER TABLE user
    add constraint account_unique unique(account);
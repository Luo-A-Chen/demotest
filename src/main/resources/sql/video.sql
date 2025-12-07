create table if not exists video(
                                    id bigint primary key auto_increment,
                                    title varchar(20) comment'视频标题',
                                    description varchar(200) comment'视频描述',
                                    user_id int comment'视频上传用户id',
                                    cover varchar(1000) comment'视频封面',
                                    video_url varchar(1000) comment'视频地址',
                                    duration int comment'视频时长',
                                    status int default 0 comment'视频状态 0-处理中 1-正常 2-删除 3-处理失败',
                                    type int default 0 comment'视频类型 0-普通视频 1-广告视频',
                                    create_time datetime default current_timestamp comment'视频创建时间',
                                    update_time datetime default current_timestamp on update current_timestamp comment'视频更新时间'
);
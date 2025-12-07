create table if not exists video_stat(
                                         video_id bigint comment '视频id',
                                         play_count int default 0 comment '播放次数',
                                         comment_count int default 0 comment '评论次数',
                                         like_count int default 0 comment '点赞次数'
)
package com.atguigu.community.mapper;

import com.atguigu.community.entity.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface CommentMapper extends BaseMapper<Comment> {

    void incCommentCount(Comment parentComment);
}

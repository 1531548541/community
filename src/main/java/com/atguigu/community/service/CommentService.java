package com.atguigu.community.service;

import com.atguigu.community.entity.Comment;
import com.atguigu.community.entity.User;
import com.atguigu.community.enums.CommentTypeEnum;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface CommentService extends IService<Comment> {
    void addComment(Comment comment, User user);

    List<Comment> findAll(Long questionId, CommentTypeEnum question);

    List<Comment> listByTargetId(Long id, CommentTypeEnum comment);
}

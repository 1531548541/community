package com.atguigu.community.service.impl;

import com.atguigu.community.entity.Comment;
import com.atguigu.community.entity.Question;
import com.atguigu.community.entity.User;
import com.atguigu.community.enums.CommentTypeEnum;
import com.atguigu.community.mapper.CommentMapper;
import com.atguigu.community.mapper.QuestionMapper;
import com.atguigu.community.mapper.UserMapper;
import com.atguigu.community.service.CommentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public void addComment(Comment comment, User user) {
        if (CommentTypeEnum.COMMENT.getType().equals(comment.getType())) {
            // 回复评论
            Comment dbComment = commentMapper.selectById(comment.getParentId());
            if(dbComment!=null){
                // 回复问题
                Question question = questionMapper.selectById(dbComment.getParentId());
                if (question != null) {
                    commentMapper.insert(comment);
                    // 增加评论数
                    Comment parentComment = new Comment();
                    parentComment.setId(comment.getParentId());
                    parentComment.setCommentCount(1);
                    commentMapper.incCommentCount(parentComment);
                    // 创建通知
//                    createNotify(comment, dbComment.getCommentator(), commentator.getName(), question.getTitle(), NotificationTypeEnum.REPLY_COMMENT, question.getId());
                }
            }
        } else {
            // 回复问题
            Question question = questionMapper.selectById(comment.getParentId());
            if (question != null) {
                comment.setCommentCount(0);
                commentMapper.insert(comment);
                question.setCommentCount(1);
                questionMapper.updateById(question);
                // 创建通知
//                createNotify(comment, question.getCreator(), commentator.getName(), question.getTitle(), NotificationTypeEnum.REPLY_QUESTION, question.getId());
            }
        }
    }

    @Override
    public List<Comment> findAll(Long questionId, CommentTypeEnum type) {
        QueryWrapper<Comment> commentQueryWrapper=new QueryWrapper<>();
        commentQueryWrapper.eq("parent_id",questionId);
        commentQueryWrapper.eq("type",type.getType());
        commentQueryWrapper.orderByDesc("gmt_create");
        List<Comment> comments = commentMapper.selectList(commentQueryWrapper);

        if (comments.size() == 0) {
            return new ArrayList<>();
        }
        // 获取去重的评论人
        Set<Long> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        List<Long> userIds = new ArrayList();
        userIds.addAll(commentators);

        // 获取评论人并转换为 Map
        List<User> users = userMapper.selectBatchIds(userIds);
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));


        List<Comment> commentDTOS = comments.stream().map(comment -> {
            comment.setUser(userMap.get(comment.getCommentator()));
            return comment;
        }).collect(Collectors.toList());

        return commentDTOS;
    }

    @Override
    public List<Comment> listByTargetId(Long id, CommentTypeEnum type) {
        QueryWrapper<Comment> commentQueryWrapper=new QueryWrapper<>();
        commentQueryWrapper.eq("parent_id",id);
        commentQueryWrapper.eq("type",type.getType());
        commentQueryWrapper.orderByDesc("gmt_create");
        List<Comment> comments = commentMapper.selectList(commentQueryWrapper);
        if (comments.size() == 0) {
            return new ArrayList<>();
        }
        // 获取去重的评论人
        Set<Long> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        List<Long> userIds = new ArrayList();
        userIds.addAll(commentators);

        // 获取评论人并转换为 Map
        List<User> users = userMapper.selectBatchIds(userIds);
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));


        // 转换 comment 为 commentDTO
        List<Comment> commentDTOS = comments.stream().map(comment -> {
            comment.setUser(userMap.get(comment.getCommentator()));
            return comment;
        }).collect(Collectors.toList());

        return commentDTOS;
    }
}

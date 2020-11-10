package com.atguigu.community.controller;

import com.atguigu.community.entity.Comment;
import com.atguigu.community.entity.Question;
import com.atguigu.community.entity.User;
import com.atguigu.community.enums.CommentTypeEnum;
import com.atguigu.community.mapper.QuestionMapper;
import com.atguigu.community.service.CommentService;
import com.atguigu.community.service.QuestionService;
import com.atguigu.community.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * Created by codedrinker on 2019/5/21.
 */
@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

//    @Autowired
//    private CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") String id, Model model) {
        Long questionId = null;
        try {
            questionId = Long.parseLong(id);
        } catch (NumberFormatException e) {
//            throw new CustomizeException(CustomizeErrorCode.INVALID_INPUT);
        }
        Question question = questionService.getById(questionId);
        User user = userService.getById(question.getCreator());
        question.setUser(user);
        List<Question> relatedQuestions = questionService.selectRelated(question);
        List<Comment> comments = commentService.findAll(questionId, CommentTypeEnum.QUESTION);
        //累加阅读数
        questionService.incView(questionId);
        model.addAttribute("question", question);
        model.addAttribute("comments", comments);
        model.addAttribute("relatedQuestions", relatedQuestions);
        return "question";
    }
}

package com.atguigu.community.service.impl;

import com.atguigu.community.entity.Question;
import com.atguigu.community.mapper.QuestionMapper;
import com.atguigu.community.service.QuestionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Override
    public void incView(Long questionId) {
        Question question = new Question();
        question.setId(questionId);
        question.setViewCount(1);
        questionMapper.incView(question);
    }
}

package com.atguigu.community.service;

import com.atguigu.community.entity.Question;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface QuestionService extends IService<Question> {
    void incView(Long questionId);

    List<Question> selectRelated(Question question);
}

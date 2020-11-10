package com.atguigu.community.mapper;

import com.atguigu.community.entity.Question;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface QuestionMapper extends BaseMapper<Question> {

    void incView(Question question);

    List<Question> selectRelated(Question question);
}

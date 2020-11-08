package com.atguigu.community.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class Comment {
    private Long id;
    private Long parentId;
    private Integer type;
    private Long commentator;
    private Long gmtCreate;
    private Long gmtModified;
    private Long likeCount;
    private String content;
    private Integer commentCount;

    @TableField(exist = false)
    private User user;
}
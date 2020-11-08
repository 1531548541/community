package com.atguigu.community.controller;

import com.atguigu.community.dto.CommentCreateDTO;
import com.atguigu.community.dto.R;
import com.atguigu.community.entity.Comment;
import com.atguigu.community.entity.User;
import com.atguigu.community.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @ResponseBody
    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public R post(@RequestBody CommentCreateDTO commentCreateDTO,
                  HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return R.error("请先登录!");
        }

        if (commentCreateDTO == null || StringUtils.isBlank(commentCreateDTO.getContent())) {
            return R.error("评论的帖子消失了!");
        }

        Comment comment = new Comment();
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setContent(commentCreateDTO.getContent());
        comment.setType(commentCreateDTO.getType());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setCommentator(user.getId());
        comment.setLikeCount(0L);
        commentService.addComment(comment, user);
        return R.ok();
    }

//    @ResponseBody
//    @RequestMapping(value = "/comment/{id}", method = RequestMethod.GET)
//    public ResultDTO<List<CommentDTO>> comments(@PathVariable(name = "id") Long id) {
//        List<CommentDTO> commentDTOS = commentService.listByTargetId(id, CommentTypeEnum.COMMENT);
//        return ResultDTO.okOf(commentDTOS);
//    }
}

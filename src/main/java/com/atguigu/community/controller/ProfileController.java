package com.atguigu.community.controller;

import com.atguigu.community.dto.NotificationDTO;
import com.atguigu.community.entity.Notification;
import com.atguigu.community.entity.Question;
import com.atguigu.community.entity.User;
import com.atguigu.community.enums.NotificationStatusEnum;
import com.atguigu.community.enums.NotificationTypeEnum;
import com.atguigu.community.service.NotificationService;
import com.atguigu.community.service.QuestionService;
import com.atguigu.community.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ProfileController {
    @Autowired
    private UserService userService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/profile/{action}")
    public String profile(HttpServletRequest request,
                          @PathVariable(name = "action") String action,
                          Model model,
                          @RequestParam(name = "page", defaultValue = "1") Integer page,
                          @RequestParam(name = "pageSize", defaultValue = "5") Integer pageSize) {

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }

        if ("questions".equals(action)) {
            model.addAttribute("section", "questions");
            model.addAttribute("sectionName", "我的提问");
            //分页
            PageHelper.startPage(page,pageSize);
            List<Question> questionList = questionService.list(null);
            for (Question question : questionList) {
                User u = userService.getById(question.getCreator());
                question.setUser(u);
            }
            PageInfo pageInfo = new PageInfo(questionList);
            model.addAttribute("questions", questionList);
            model.addAttribute("total", pageInfo.getTotal());
            model.addAttribute("page", page);
        } else if ("replies".equals(action)) {
            model.addAttribute("section", "replies");
            model.addAttribute("sectionName", "最新回复");
            //分页
            PageHelper.startPage(page,pageSize);
            QueryWrapper<Notification> queryWrapper=new QueryWrapper<>();
            queryWrapper.orderByAsc("status");
            queryWrapper.orderByDesc("gmt_create");
            List<Notification> notificationList = notificationService.list(queryWrapper);
            List<NotificationDTO> notificationDTOList=new ArrayList<>();
            for (Notification notification : notificationList) {
                NotificationDTO notificationDTO = new NotificationDTO();
                notificationDTO.setId(notification.getId());
                notificationDTO.setNotifierName(notification.getNotifierName());
                notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
                notificationDTO.setOuterTitle(notification.getOuterTitle());
                notificationDTO.setStatus(notification.getStatus());
                notificationDTOList.add(notificationDTO);
            }
            PageInfo pageInfo = new PageInfo(notificationList);
            model.addAttribute("notifications", notificationDTOList);
            model.addAttribute("total", pageInfo.getTotal());
            model.addAttribute("page", page);
        }
        return "profile";
    }
}

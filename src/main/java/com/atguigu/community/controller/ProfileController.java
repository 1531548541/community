package com.atguigu.community.controller;

import com.atguigu.community.entity.Question;
import com.atguigu.community.entity.User;
import com.atguigu.community.service.QuestionService;
import com.atguigu.community.service.UserService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by codedrinker on 2019/5/15.
 */
@Controller
public class ProfileController {
    @Autowired
    private UserService userService;

    @Autowired
    private QuestionService questionService;
//    @Autowired
//    private NotificationService notificationService;

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
            List<Question> questionList = questionService.list(null);
            for (Question question : questionList) {
                User u = userService.getById(question.getCreator());
                question.setUser(u);
            }
            //分页
            List<Question> questionList1 = new ArrayList<>();
            PageInfo pageInfo = new PageInfo(questionList);
            if(pageInfo.getTotal()>=page*pageSize){
                questionList1 = questionList.subList((page - 1) * pageSize, page * pageSize);
            }else{
                questionList1 = questionList.subList((page - 1) * pageSize, (int)pageInfo.getTotal());
            }
            model.addAttribute("questions", questionList1);
            model.addAttribute("total", questionList.size());
            model.addAttribute("page", page);
        } else if ("replies".equals(action)) {

            model.addAttribute("section", "replies");
//            model.addAttribute("notifications", notificationList);
            model.addAttribute("sectionName", "最新回复");
        }
        return "profile";
    }
}

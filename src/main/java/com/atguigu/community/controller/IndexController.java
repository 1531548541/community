package com.atguigu.community.controller;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.atguigu.community.entity.Question;
import com.atguigu.community.entity.User;
import com.atguigu.community.service.QuestionService;
import com.atguigu.community.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private UserService userService;

    @Autowired
    private QuestionService questionService;

    @RequestMapping(value = {"/","index"})
    public String toIndex(@RequestParam(value = "page",defaultValue = "1") Integer page,
                          @RequestParam(value = "pageSize",defaultValue = "5")Integer pageSize,
                          @RequestParam(value = "keyWord",defaultValue = "") String keyWord,
                            HttpServletRequest request,
                          Model model){
        //分页
        PageHelper.startPage(page,pageSize);
//        User u = (User) request.getSession().getAttribute("user");
//        if (u == null) {
//            return "redirect:/";
//        }
        //查找文章列表
        QueryWrapper<Question> questionQueryWrapper=new QueryWrapper<>();
        questionQueryWrapper.like("title",keyWord);
        questionQueryWrapper.like("description",keyWord);
        List<Question> questionList = questionService.list(questionQueryWrapper);
        for (Question question : questionList) {
            User user = userService.getById(question.getCreator());
            question.setUser(user);
        }
        //分页
//        List<Question> questionList1 = new ArrayList<>();
        PageInfo pageInfo = new PageInfo(questionList);
//        if(pageInfo.getTotal()>=page*pageSize){
//            questionList1 = questionList.subList((page - 1) * pageSize, page * pageSize);
//        }else{
//            questionList1 = questionList.subList((page - 1) * pageSize, (int)pageInfo.getTotal());
//        }
        model.addAttribute("questions",questionList);
        model.addAttribute("page",page);
        model.addAttribute("total",pageInfo.getTotal());
        return "index";
    }
}

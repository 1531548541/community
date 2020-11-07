package com.atguigu.community.controller;

import com.atguigu.community.entity.User;
import com.atguigu.community.mapper.UserMapper;
import com.atguigu.community.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    @Autowired
    private UserService userService;

    @RequestMapping("/")
    public String toIndex(HttpServletRequest request){
        //拿到cookie中的token
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals("token")){
                String token = cookie.getValue();
                //在数据库中查找该token是否存在
                QueryWrapper<User> queryWrapper=new QueryWrapper<User>();
                queryWrapper.eq("token",token);
                User user = userService.getOne(queryWrapper);
                if(user!=null){
                    //存入session
                    request.getSession().setAttribute("user",user);
                }
                break;
            }
        }
        return "index";
    }
}

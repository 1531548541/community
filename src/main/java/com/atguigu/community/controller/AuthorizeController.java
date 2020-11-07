package com.atguigu.community.controller;

import com.atguigu.community.dto.AccessTokenDTO;
import com.atguigu.community.dto.GithubUser;
import com.atguigu.community.entity.User;
import com.atguigu.community.provider.GithubProvider;
import com.atguigu.community.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * github社交登录
 */
@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    @Autowired
    private UserService userService;

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirect.uri}")
    private String redirectUri;


    @RequestMapping("/callback")
    public String callback(@RequestParam String code,
                           @RequestParam String state,
                           HttpServletResponse response){
        AccessTokenDTO accessTokenDTO=new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = githubProvider.getUser(accessToken);
        if (githubUser != null) {
            //存入数据库
            QueryWrapper<User> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("account_id",String.valueOf(githubUser.getId()));
            User user = userService.getOne(queryWrapper);
            String token=String.valueOf(UUID.randomUUID());
            if(user==null){
                User user1=new User();
                user1.setAccountId(String.valueOf(githubUser.getId()));
                user1.setName(githubUser.getName());
                user1.setToken(token);
                user1.setGmtCreate(System.currentTimeMillis());
                user1.setGmtModified(user1.getGmtCreate());
//            user1.setBio();
                user1.setAvatarUrl(githubUser.getAvatarUrl());
                userService.save(user1);
            }else {
                user.setName(githubUser.getName());
                user.setToken(token);
                user.setGmtModified(user.getGmtCreate());
                userService.updateById(user);
            }
            //token写入cookie
            response.addCookie(new Cookie("token",token));
            return "redirect:/";
        }
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response) {
        request.getSession().removeAttribute("user");
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }
}

package com.atguigu.community.controller;

import com.atguigu.community.dto.AccessTokenDTO;
import com.atguigu.community.dto.GithubUser;
import com.atguigu.community.entity.User;
import com.atguigu.community.provider.GithubProvider;
import com.atguigu.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
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
            User u=new User();
            u.setAccountId(String.valueOf(githubUser.getId()));
            u.setName(githubUser.getName());
            u.setToken(String.valueOf(UUID.randomUUID()));
            u.setGmtCreate(System.currentTimeMillis());
            u.setGmtModified(u.getGmtCreate());
//            u.setBio();
            u.setAvatarUrl(githubUser.getAvatarUrl());
            userService.saveOrUpdate(u);
            //token写入cookie
            response.addCookie(new Cookie("token",u.getToken()));
            return "redirect:/";
        }
        return "redirect:/";
    }
}

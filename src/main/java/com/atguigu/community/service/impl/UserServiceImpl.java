package com.atguigu.community.service.impl;

import com.atguigu.community.entity.User;
import com.atguigu.community.mapper.UserMapper;
import com.atguigu.community.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService{

}

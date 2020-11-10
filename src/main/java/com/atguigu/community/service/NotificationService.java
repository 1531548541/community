package com.atguigu.community.service;

import com.atguigu.community.entity.Notification;
import com.atguigu.community.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

public interface NotificationService extends IService<Notification> {
    Notification read(Long id, User user);

    Integer unreadCount(Long id);
}

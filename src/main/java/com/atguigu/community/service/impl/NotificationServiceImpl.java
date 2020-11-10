package com.atguigu.community.service.impl;

import com.atguigu.community.entity.Notification;
import com.atguigu.community.entity.User;
import com.atguigu.community.enums.NotificationStatusEnum;
import com.atguigu.community.mapper.NotificationMapper;
import com.atguigu.community.service.NotificationService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification> implements NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;

    @Override
    public Notification read(Long id, User user) {
        Notification notification = notificationMapper.selectById(id);
        if (notification == null) {
//            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
        if (!Objects.equals(notification.getReceiver(), user.getId())) {
//            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
        }

        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateById(notification);
        return notification;
    }

    @Override
    public Integer unreadCount(Long userId) {
        QueryWrapper<Notification> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("receiver",userId);
        queryWrapper.eq("status",NotificationStatusEnum.UNREAD.getStatus());
        return notificationMapper.selectCount(queryWrapper);
    }
}

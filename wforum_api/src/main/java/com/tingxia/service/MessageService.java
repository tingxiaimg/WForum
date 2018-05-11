package com.tingxia.service;

import com.github.pagehelper.PageHelper;
import com.tingxia.dao.MessageMapper;
import com.tingxia.pojo.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    @Autowired
    private MessageMapper messageMapper;

    public List<Message> getMessages(Integer userId,Boolean isRead,Integer pageNo,Integer pageSize){
        PageHelper.startPage(pageNo,pageSize);
        return messageMapper.selectByUser(userId,isRead);
    }

    public Boolean setRead(Integer userId, Integer id) {
        return messageMapper.updateReaded(userId,id)>0;
    }
}

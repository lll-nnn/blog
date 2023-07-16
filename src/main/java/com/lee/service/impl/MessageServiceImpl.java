package com.lee.service.impl;

import com.lee.mapper.MessageMapper;
import com.lee.pojo.Message;
import com.lee.pojo.MessageExample;
import com.lee.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Override
    public void insertFollow(long sender, long receiver) {
        Message message = new Message();
        message.setChannel("STATION_LETTER");
        message.setType("FOLLOW_USER");
        message.setRead("NO");
        message.setSenderType("USER_ID");
        message.setSender(sender+"");
        message.setReceiverType("USER_ID");
        message.setReceiver(receiver+"");
        message.setTitle("");
        message.setContentType("TEXT");
        message.setContent("");
        message.setIsDelete((byte) 0);
        message.setCreateAt(new Date());
        message.setUpdateAt(new Date());
        messageMapper.insert(message);
    }

    @Override
    public List<Message> getByReceiver(long receiver) {
        MessageExample example = new MessageExample();
        example.createCriteria().andReceiverEqualTo(receiver+"");
        return messageMapper.selectByExample(example);
    }
}

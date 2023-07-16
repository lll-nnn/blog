package com.lee.service;

import com.lee.pojo.Message;

import java.util.List;

public interface MessageService {
    void insertFollow(long sender, long receiver);

    List<Message> getByReceiver(long receiver);
}

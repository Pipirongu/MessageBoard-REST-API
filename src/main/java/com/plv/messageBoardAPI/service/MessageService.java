package com.plv.messageBoardAPI.service;

import com.plv.messageBoardAPI.entity.Message;

import java.util.List;

public interface MessageService {

    public void createMessage(Message message);
    public void updateMessage(Message message);
    public void deleteMessage(Message message);
    public List<Message> getMessages();

    /*
        Check for duplicate message in database by id
    */
    public Boolean isMessageExistById(Long messageId);

    /*
        Retrieve a message for a given owner
    */
    public Message getMessage(Long messageId, String owner);
}

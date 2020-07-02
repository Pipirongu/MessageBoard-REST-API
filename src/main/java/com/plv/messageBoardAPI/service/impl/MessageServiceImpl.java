package com.plv.messageBoardAPI.service.impl;

import com.plv.messageBoardAPI.entity.Message;
import com.plv.messageBoardAPI.repository.MessageRepository;
import com.plv.messageBoardAPI.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public void setMessageRepository(MessageRepository messageRepository) { this.messageRepository = messageRepository; }

    public void createMessage(Message message) { messageRepository.save(message); }
    public void updateMessage(Message message) { messageRepository.save(message); }
    public void deleteMessage(Message message) { messageRepository.delete(message); }
    public List<Message> getMessages() { return messageRepository.findAll(); }

    public Boolean isMessageExistById(Long messageId){
        Optional<Message> optMsg = messageRepository.findById(messageId);
        if(optMsg.isPresent()) {
            return true;
        }else {
            return false;
        }
    }

    public Message getMessage(Long messageId, String owner) {
        Optional<Message> optMsg = messageRepository.findById(messageId);
        if(optMsg.isPresent()) {
            Message msg = optMsg.get();
            if(msg.getOwner().equals(owner)){
                return msg;
            }
        }
        return null;
    }
}

package com.plv.messageBoardAPI.controller;

import com.plv.messageBoardAPI.entity.Message;
import com.plv.messageBoardAPI.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class MessageRestController {

    @Autowired
    private MessageService messageService;

    public void setMessageService(MessageService messageService){ this.messageService = messageService; }

    /*
        Creates a message in the service for given client
     */
    @PostMapping("/api/messageBoard/message")
    public void createMessage(@RequestBody Message message){
        if(!messageService.isMessageExistById(message.getId())){
            messageService.createMessage(message);
        }else{
            //Duplicate resource
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }

    /*
        Modify a given client's existing message
     */
    @PutMapping("/api/messageBoard/message/{messageId}/{owner}")
    public void updateMessage(@RequestBody String message, @PathVariable(name="messageId")Long messageId,  @PathVariable(name="owner")String owner) {
        // Fetch existing message
        Message msg = messageService.getMessage(messageId, owner);
        if(msg != null) {
            msg.setMessage(message);
            messageService.updateMessage(msg);
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    /*
        Delete a message for a given client
     */
    @DeleteMapping("/api/messageBoard/message/{messageId}/{owner}")
    public void deleteMessage(@PathVariable(name="messageId")Long messageId,  @PathVariable(name="owner")String owner){
        // Fetch existing message
        Message msg = messageService.getMessage(messageId, owner);
        if (msg != null) {
            messageService.deleteMessage(msg);
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    /*
        Retrieve all messages from the service
     */
    @GetMapping("/api/messageBoard/messages")
    public List<Message> getMessages() {
        List<Message> messages = messageService.getMessages();
        return messages;
    }

}

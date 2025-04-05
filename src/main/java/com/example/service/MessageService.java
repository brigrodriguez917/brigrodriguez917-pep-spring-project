package com.example.service;

import com.example.entity.Message;
import com.example.exception.ResourceNotFoundException;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private AccountRepository accountRepository;


public Message createNewMessage(Message newMessage){

    if (newMessage.getMessageText() == null || newMessage.getMessageText().trim().isEmpty()) {
        throw new IllegalArgumentException("Message text cannot be blank");
    }
    if (newMessage.getMessageText().length() > 255) {
        throw new IllegalArgumentException("Message text cannot exceed 255 characters");
    }


    if (!accountRepository.existsById(newMessage.getPostedBy())) {
        throw new IllegalArgumentException("Invalid account ID: " + newMessage.getPostedBy());
    }

    if (newMessage.getTimePostedEpoch() == null) {
        newMessage.setTimePostedEpoch(System.currentTimeMillis() / 1000);
    }


    return messageRepository.save(newMessage);
}


    public List <Message> getAllMessages() {
        return messageRepository.findAll(); 
    }
    public Message getMessageById(int messageId) throws ResourceNotFoundException {
        return messageRepository.findById(messageId)
        .orElseThrow(() -> new ResourceNotFoundException("Message not found"));
    }

    public int deleteMessage(int messageId) {
            if (messageRepository.existsById(messageId)) {
                messageRepository.deleteById(messageId);
                return 1;
            }
            return 0;
        }


    public int updateMessageText(int messageId, String newText ) {

        if (newText == null || newText.trim().isEmpty()) {
            throw new IllegalArgumentException("Message text cannot be blank");
        }
        if (newText.length() > 255) {
            throw new IllegalArgumentException("Message text cannot exceed 255 characters");
        }

        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("Message not found"));

        message.setMessageText(newText);
        messageRepository.save(message);
        return 1;
    

    }

    public List <Message> getMessageByUser(int accountId)  {
        return messageRepository.findByPostedBy(accountId);
    }
    }
